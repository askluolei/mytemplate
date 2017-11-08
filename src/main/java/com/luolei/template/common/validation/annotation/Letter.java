package com.luolei.template.common.validation.annotation;

import com.luolei.template.common.validation.validator.LetterValidator;

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
@Constraint(validatedBy = {LetterValidator.class})
public @interface Letter {

    String message() default "不合法的 大小写字母";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
