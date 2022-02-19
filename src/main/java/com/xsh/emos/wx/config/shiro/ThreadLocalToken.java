package com.xsh.emos.wx.config.shiro;

import org.springframework.stereotype.Component;

/**
 * @author : xsh
 * @create : 2022-01-17 - 0:31
 * @describe: 用于存取令牌的线程类
 */
@Component
public class ThreadLocalToken {

    private ThreadLocal<String> local = new ThreadLocal();

    public String getToken() {
        return (String)local.get();
    }

    public void setToken(String token) {
        local.set(token);
    }

    public void clear() {
        local.remove();
    }
}
