package com.luolei.template.common.api;

/**
 * api响应码
 *
 * @author luolei
 * @email askluolei@gmail.com
 * @date 2017/10/12 22:42
 */
public enum  RestError {

    SUCCESS("success", "成功"),

    INTERVAL_ERROR("interval_error", "服务端错误")
    ;
    private String code;
    private String message;
    RestError(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
