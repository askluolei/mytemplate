package com.luolei.template.common.api;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
    private Map<String, Object> data;

    public R(String code, String message, Map<String, Object> data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public R(RestError error, Map<String, Object> data) {
        this.code = error.getCode();
        this.message = error.getMessage();
        this.data = data;
    }

    public R with(String key, Object value) {
        if (Objects.isNull(data)) {
            data = new HashMap<>(5);
        }
        data.put(key, value);
        return this;
    }

    public static R ok() {
        return new R(RestError.SUCCESS, null);
    }

    public static R ok(Map<String, Object> data) {
        return new R(RestError.SUCCESS, data);
    }

    public static R error(RestError error) {
        return new R(error, null);
    }

    public static R error(RestError error, Map<String, Object> data) {
        return new R(error, data);
    }

    public static R error(String code, String message) {
        return new R(code, message, null);
    }

    public static R error(String code, String message, Map<String, Object> data) {
        return new R(code, message, data);
    }

}
