package com.luolei.template.common.exception;

/**
 * 自定义异常
 *
 * @author luolei
 * @email askluolei@gmail.com
 * @date 2017/10/12 22:36
 */
public class TException extends RuntimeException {

    public TException() {
        super();
    }

    public TException(String message) {
        super(message);
    }

    public TException(String message, Throwable cause) {
        super(message, cause);
    }

    public TException(Throwable cause) {
        super(cause);
    }
}
