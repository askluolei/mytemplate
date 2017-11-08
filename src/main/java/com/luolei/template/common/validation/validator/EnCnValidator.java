package com.luolei.template.common.validation.validator;

import com.luolei.template.common.validation.annotation.EnCn;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * @author 罗雷
 * @date 2017/11/8 0008
 * @time 17:16
 */
public class EnCnValidator implements ConstraintValidator<EnCn, String> {

    private static final String patternStr = "^[\\u4E00-\\u9FA5A-Za-z0-9_]{3,20}$";
    private Pattern pattern;

    @Override
    public void initialize(EnCn constraintAnnotation) {
        pattern = Pattern.compile(patternStr);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return pattern.matcher(value).matches();
    }
}
