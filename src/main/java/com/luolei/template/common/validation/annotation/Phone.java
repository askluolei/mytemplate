package com.luolei.template.common.validation.annotation;

import com.luolei.template.common.validation.validator.PhoneValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author 罗雷
 * @date 2017/11/8 0008
 * @time 16:40
 */
@Documented
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {PhoneValidator.class})
public @interface Phone {

    String message() default "不合法的 手机号";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
