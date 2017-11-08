package com.luolei.template.common.validation.validator;

import com.luolei.template.common.validation.annotation.StrongPass;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * @author 罗雷
 * @date 2017/11/8 0008
 * @time 17:16
 */
public class StrongPassValidator implements ConstraintValidator<StrongPass, String> {

    private static final String patternStr = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,16}$";
    private Pattern pattern;

    @Override
    public void initialize(StrongPass constraintAnnotation) {
        pattern = Pattern.compile(patternStr);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return pattern.matcher(value).matches();
    }
}
