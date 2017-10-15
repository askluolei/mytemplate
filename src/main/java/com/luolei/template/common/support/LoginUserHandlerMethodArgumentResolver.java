package com.luolei.template.common.support;

import com.luolei.template.common.annotation.LoginUser;
import com.luolei.template.common.exception.TException;
import com.luolei.template.common.security.TokenGenerator;
import com.luolei.template.common.utils.Constant;
import com.luolei.template.modules.sys.dao.UserDao;
import com.luolei.template.modules.sys.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Objects;

/**
 * 处理带有 @LoginUser 注解 ,UserEntity 类型的 参数
 *
 * @author luolei
 * @email askluolei@gmail.com
 * @date 2017/10/15 21:40
 */
@Component
public class LoginUserHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    private UserDao userDao;

    @Autowired
    private TokenGenerator tokenGenerator;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(UserEntity.class) && parameter.hasMethodAnnotation(LoginUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        boolean require = parameter.getMethodAnnotation(LoginUser.class).require();
        String token = webRequest.getHeader(Constant.TOKEN_HEADER_KEY);
        long userId = tokenGenerator.getUserID(token);
        UserEntity loginUser = userDao.findOne(userId);
        if (Objects.isNull(loginUser) && require) {
            throw new TException("token异常，无法解析出登录的用户");
        }
        return loginUser;
    }
}
