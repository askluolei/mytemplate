package com.luolei.template.common.validation.validator;

import com.luolei.template.common.validation.annotation.Phone;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * @author 罗雷
 * @date 2017/11/8 0008
 * @time 16:40
 */
public class PhoneValidator implements ConstraintValidator<Phone, String> {

    private static final String phonePattern = "^(0|86|17951)?(13[0-9]|15[012356789]|18[0-9]|14[57]|17[678])[0-9]{8}$";
    private Pattern pattern;

    @Override
    public void initialize(Phone phone) {
        pattern = Pattern.compile(phonePattern);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return pattern.matcher(value).matches();
    }
}
