package com.xsh.emos.wx.config.shiro;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author : xsh
 * @create : 2022-01-23 - 15:29
 * @describe:
 */
@Component
@Scope("prototype")//声明为多例，如果是单例则保存数据到ThreadLocal中会出问题
@Slf4j
public class OAuth2Filter extends AuthenticatingFilter {

    @Autowired
    private ThreadLocalToken threadLocalToken;

    @Value("${emos.jwt.cache-expire}")
    private int cacheExpire;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 拦截请求之后，用于把令牌字符串封装成令牌对象
     */
    @Override
    protected AuthenticationToken createToken(ServletRequest request,
                                              ServletResponse response) throws Exception {
        HttpServletRequest req=(HttpServletRequest)request;
        //获取请求token
        String token = getRequestToken(req);
        if(StrUtil.isBlank(token)){
            log.warn("token为null");
            return null;
        }
        return new OAuth2Token(token);
    }

    /**
     * 获取token方法；如果不在请求头里则在参数里面获取
     * @param request
     * @return
     */
    private String getRequestToken(HttpServletRequest request){
        String token = request.getHeader("token");
        if(StrUtil.isBlank(token)){
            token=request.getParameter("token");
        }
        return token;
    }

    /**
     * 用于控制哪些请求需要被shiro处理，哪些请求不需要处理
     * @return 返回true则表示直接放行，此次请求不会由shiro处理
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        HttpServletRequest req=(HttpServletRequest)request;
        //因为post请求分两次，第一次类型为OPTIONS用于检查服务是否可用；当为OPTIONS类型的请求直接放行
        if(RequestMethod.OPTIONS.name().equals(req.getMethod())){
            return true;
        }
        return false;
    }

    /**
     * 是否是拒绝登录
     * @return 返回false则为认证授权失败
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        //转为http请求
        HttpServletRequest req=(HttpServletRequest)request;
        HttpServletResponse resp=(HttpServletResponse)response;
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Access-Control-Allow-Credentials","true");
        resp.setHeader("Access-Control-Allow-Origin",req.getHeader("Origin"));
        threadLocalToken.clear();
        String token = getRequestToken(req);
        if(StrUtil.isBlank(token)){
            //token为空，返回错误响应码
            resp.setStatus(HttpStatus.SC_UNAUTHORIZED);
            resp.getWriter().print("无效的令牌");
            return false;
        }
        //token不为空，则验证是否有效，是否过期
        try {
            jwtUtil.verifierToken(token);
        } catch (TokenExpiredException e) {
            //token过期异常
            if(redisTemplate.hasKey(token)){
                //如果redis里面有该token存在但是抛出了TokenExpiredException过期异常，
                // 则说明只是客户端保存的令牌过期了而服务端的令牌没有过期；此时需要刷新令牌重新生成一个新令牌(先删除老令牌再生成新的)
                redisTemplate.delete(token);
                int userId = jwtUtil.getUserId(token);
                token = jwtUtil.createToken(userId);
                redisTemplate.opsForValue().set(token,String.valueOf(userId),cacheExpire, TimeUnit.DAYS);
                threadLocalToken.setToken(token);
            }else {
                //抛出了TokenExpiredException过期异常，且redis缓存里面也没有数据
                //则说明客户端和服务端token都过期了；此时需要重新登录
                resp.setStatus(HttpStatus.SC_UNAUTHORIZED);
                resp.getWriter().print("令牌已过期");
                return false;
            }
        }catch (JWTDecodeException e){
            //token内容异常(可能是客户端伪造的令牌)
            resp.setStatus(HttpStatus.SC_UNAUTHORIZED);
            resp.getWriter().print("无效的令牌");
            return false;
        }
        boolean bool = executeLogin(request, response);
        return bool;
    }

    /**
     * 判断用户登录失败执行的方法(onAccessDenied方法执行返回false执行的方法)
     * @return
     */
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        //用于返回一些认证失败的详细信息和状态码
        HttpServletRequest req=(HttpServletRequest)request;
        HttpServletResponse resp=(HttpServletResponse)response;
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Access-Control-Allow-Credentials","true");
        resp.setHeader("Access-Control-Allow-Origin",req.getHeader("Origin"));
        resp.setStatus(HttpStatus.SC_UNAUTHORIZED);
        try {
            //返回具体的错误信息
            resp.getWriter().print(e.getMessage());
        } catch (Exception exception) {

        }
        return false;
    }

    /**
     * 管理拦截请求和响应的方法
     */
    @Override
    public void doFilterInternal(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        super.doFilterInternal(request, response, chain);
    }
}
