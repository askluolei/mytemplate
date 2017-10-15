package com.luolei.template.common.annotation;

import java.lang.annotation.*;

/**
 * 用来在方法参数中获取 accessToken
 *
 * @author luolei
 * @email askluolei@gmail.com
 * @date 2017/10/15 21:58
 */
@Documented
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface AccessToken {

    boolean require() default true;
}
