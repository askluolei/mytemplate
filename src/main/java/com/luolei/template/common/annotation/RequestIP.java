package com.luolei.template.common.annotation;

import java.lang.annotation.*;

/**
 * 请求的 IP
 *
 * @author luolei
 * @email askluolei@gmail.com
 * @date 2017/10/16 21:40
 */
@Documented
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestIP {

    boolean require() default true;
}
