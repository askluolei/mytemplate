package com.luolei.template.common.validation.validator;

import com.luolei.template.common.validation.annotation.Cn;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * @author 罗雷
 * @date 2017/11/8 0008
 * @time 17:16
 */
public class CnValidator implements ConstraintValidator<Cn, String> {

    private static final String patternStr = "^[\\u4e00-\\u9fa5]{0,}$";
    private Pattern pattern;

    @Override
    public void initialize(Cn constraintAnnotation) {
        pattern = Pattern.compile(patternStr);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return pattern.matcher(value).matches();
    }
}
