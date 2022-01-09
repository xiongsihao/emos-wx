package com.xsh.emos.wx.common.util;

import org.apache.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : xsh
 * @create : 2022-01-10 - 2:24
 * @describe: 封装R对象，用于项目统一数据返回格式
 * 包含：业务姿态码，业务消息，业务数据；
 * 其中继承HashMap是为了更好的放置数据
 */
public class R extends HashMap<String, Object> {

    /**
     * 定义一个构造函数放默认返回成功的数据
     */
    public R() {
        put("code", HttpStatus.SC_OK);
        put("msg", "success");
    }

    /**
     * 虽然继承了HashMap有了put方法，但是没办法进行链式调用,不方便
     * 此处再声明一个put方法，使用super.put()并return this返回自己本身，可实现链式调用；
     * 链式调用：new R().put("","").put("","").put("",""); 可以一直往后.操作一个对象增加数据
     *
     * @param key
     * @param value
     * @return
     */
    public R put(String key, Object value) {
        super.put(key, value);
        return this;
    }

    /**
     * 静态工厂方法，返回默认成功
     *
     * @return
     */
    public static R ok() {
        return new R();
    }

    /**
     * 重载方法，对默认成功数据的msg执行修改功能
     *
     * @return
     */
    public static R ok(String msg) {
        R r = new R();
        r.put("msg", msg);
        return r;
    }

    /**
     * 重载方法，入参一个Map执行添加其它key值的数据
     *
     * @return
     */
    public static R ok(Map<String, Object> map) {
        R r = new R();
        r.putAll(map);
        return r;
    }

    /**
     * 定义错误业务返回数据的方法
     * @param code
     * @param msg
     * @return
     */
    public static R error(int code,String msg){
        R r = new R();
        r.put("code",code);
        r.put("msg",msg);
        return r;
    }

    /**
     * 重载方法，不传code则默认500
     * @param msg
     * @return
     */
    public static R error(String msg){
        return error(HttpStatus.SC_INTERNAL_SERVER_ERROR,msg);
    }

    /**
     * 重载方法，code和msg都不传则默认500和错误提示固定
     * @return
     */
    public static R error(){
        return error(HttpStatus.SC_INTERNAL_SERVER_ERROR,"未知异常，请联系管理员！");
    }
}
