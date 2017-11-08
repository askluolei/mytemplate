package com.luolei.template.common.validation.validator;

import com.luolei.template.common.validation.annotation.QQ;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * @author 罗雷
 * @date 2017/11/8 0008
 * @time 17:16
 */
public class QQValidator implements ConstraintValidator<QQ, String> {

    private static final String patternStr = "^[1-9][0-9]{4,9}$";
    private Pattern pattern;

    @Override
    public void initialize(QQ constraintAnnotation) {
        pattern = Pattern.compile(patternStr);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return pattern.matcher(value).matches();
    }
}
