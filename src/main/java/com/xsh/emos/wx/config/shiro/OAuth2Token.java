package com.xsh.emos.wx.config.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author : xsh
 * @create : 2022-01-16 - 18:37
 * @describe: 把token封装成认证对象
 */
public class OAuth2Token implements AuthenticationToken {

    private String token;

    public OAuth2Token(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
