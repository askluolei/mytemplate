package com.luolei.template.common.annotation;

import java.lang.annotation.*;

/**
 * 用在controller的方法上，
 * 获取当前登录用户信息
 *
 * @author luolei
 * @email askluolei@gmail.com
 * @date 2017/10/15 21:40
 */
@Documented
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginUser {

    boolean require() default true;
}
