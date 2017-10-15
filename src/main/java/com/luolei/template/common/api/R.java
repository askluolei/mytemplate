package com.luolei.template.common.api;

import lombok.Getter;

/**
 * Rest api的返回对象
 * 前端来的所有请求都返回这个对象
 * 固定住返回数据的格式
 *
 * @author luolei
 * @email askluolei@gmail.com
 * @date 2017/10/12 22:39
 */
@Getter
public class R {

    private String code;
    private String message;
    private Object data;

    public R(String code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public R(RestError error, Object data) {
        this.code = error.getCode();
        this.message = error.getMessage();
        this.data = data;
    }

    public static R ok() {
        return new R(RestError.SUCCESS, null);
    }

    public static R ok(Object data) {
        return new R(RestError.SUCCESS, data);
    }

    public static R error(RestError error) {
        return new R(error, null);
    }

    public static R error(RestError error, Object data) {
        return new R(error, data);
    }

    public static R error(String code, String message) {
        return new R(code, message, null);
    }

    public static R error(String code, String message, Object data) {
        return new R(code, message, data);
    }

}
