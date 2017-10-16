package com.luolei.template.common.annotation;

import java.lang.annotation.*;

/**
 * 请求来自的平台
 *
 * @author luolei
 * @email askluolei@gmail.com
 * @date 2017/10/16 21:41
 */
@Documented
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestPlatform {

    boolean require() default true;

}
