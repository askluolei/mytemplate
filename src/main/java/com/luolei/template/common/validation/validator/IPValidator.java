package com.luolei.template.common.validation.validator;

import com.luolei.template.common.validation.annotation.IP;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * IP 校验
 *
 * @author luolei
 * @email askluolei@gmail.com
 * @date 2017/10/14 17:28
 */
public class IPValidator implements ConstraintValidator<IP, String> {

    private static final String ipPattern = "\\d+\\.\\d+\\.\\d+\\.\\d+";
    private Pattern pattern;

    @Override
    public void initialize(IP constraintAnnotation) {
        pattern = Pattern.compile(ipPattern);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return pattern.matcher(value).matches();
    }
}
