package com.xsh.emos.wx.aop;

import com.xsh.emos.wx.common.util.R;
import com.xsh.emos.wx.config.shiro.ThreadLocalToken;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author : xsh
 * @create : 2022-02-20 - 21:24
 * @describe: 利用aop，拦截所有的web方法，在返回的R对象中添加更新后的令牌，将更新的令牌返回给客户端
 */
@Aspect
@Component
public class TokenAspect {
    @Autowired
    private ThreadLocalToken threadLocalToken;

    @Pointcut("execution(public * com.xsh.emos.wx.controller.*.*(..))")
    public void aspect(){

    }

    @Around("aspect()")
    public Object around(ProceedingJoinPoint point) throws Throwable{
        R r=(R)point.proceed();
        String token=threadLocalToken.getToken();
        if(token!=null){
            r.put("token",token);
            threadLocalToken.clear();
        }
        return r;
    }
}
