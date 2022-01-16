package com.xsh.emos.wx.config.shiro;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author : xsh
 * @create : 2022-01-16 - 17:26
 * @describe:
 */
@Component
@Slf4j
public class JwtUtil {

    @Value("${emos.jwt.secret}")
    private String secret;
    @Value("${emos.jwt.expire}")
    private int expire;

    /**
     * 根据userId生成token
     * @param userId
     * @return
     */
    public String createToken(int userId) {
        //当前日期后五天
        Date date = DateUtil.offset(new Date(), DateField.DAY_OF_YEAR, expire);
        Algorithm algorithm = Algorithm.HMAC256(secret);
        JWTCreator.Builder builder = JWT.create();
        //根据字段，加密密钥，过期时间构建JWT
        String token = builder.withClaim("userId", userId).withExpiresAt(date).sign(algorithm);
        log.info("生成token:{}", token);
        return token;
    }

    /**
     * 解析token里的userId
     * @param token
     * @return
     */
    public int getUserId(String token){
        //解码token
        DecodedJWT jwt=JWT.decode(token);
        //获取token里的userId值
        int userId = jwt.getClaim("userId").asInt();
        log.info("解析token:{},解析userId:{}", token,userId);
        return userId;
    }

    /**
     * 验证token内容有效性与是否过期(当token已过期或无效，verify方法会抛出RuntimeException异常)
     * @param token
     */
    public void verifierToken(String token){
        Algorithm algorithm=Algorithm.HMAC256(secret);
        JWTVerifier verifier = JWT.require(algorithm).build();
        verifier.verify(token);
    }
}
