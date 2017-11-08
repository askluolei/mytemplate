package com.luolei.template.common.validation.annotation;

import com.luolei.template.common.validation.validator.CnValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 自定义 IP 校验（示例）
 *
 * @author luolei
 * @email askluolei@gmail.com
 * @date 2017/10/14 17:27
 */
@Documented
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {CnValidator.class})
public @interface Cn {

    String message() default "不合法的 只能有汉字";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
