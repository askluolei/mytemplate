package com.luolei.template.common.annotation;

import java.lang.annotation.*;

/**
 * 权限说明
 * @author luolei
 * @email askluolei@gmail.com
 * @date 2017/10/29 18:29
 */
@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthzExplain {
    String value() default "";
}
