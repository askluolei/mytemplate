package com.luolei.template.common.exception;

import com.luolei.template.common.api.R;
import com.luolei.template.common.api.RestError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * rest api 全局异常处理
 * @author luolei
 * @email askluolei@gmail.com
 * @date 2017/10/12 22:37
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 处理系统自定义异常 TException
     * @param e
     * @param request
     * @return
     */
    @ExceptionHandler(TException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public R handleTException(TException e, HttpServletRequest request) {
        logger.error("uri:" + request.getRequestURI(), e);
        return R.error(RestError.INTERVAL_ERROR).with("exception", e.getClass().getName()).with("message", e.getMessage());
    }

    /**
     * 处理参数校验异常
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R handlerMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        logger.error("uri:" + request.getRequestURI(), e);
        return R.error(RestError.INTERVAL_ERROR).with("exception", e.getClass().getName()).with("message", e.getMessage());
    }

    /**
     * 未知的异常在这里处理
     * @param e
     * @param request
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public R handleAll(Exception e, HttpServletRequest request) {
        logger.error("uri:" + request.getRequestURI(), e);
        return R.error(RestError.INTERVAL_ERROR).with("exception", e.getClass().getName()).with("message", e.getMessage());
    }


}
