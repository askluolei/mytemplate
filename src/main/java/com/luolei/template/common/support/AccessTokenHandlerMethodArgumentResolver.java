package com.luolei.template.common.support;

import com.luolei.template.common.annotation.AccessToken;
import com.luolei.template.common.exception.TException;
import com.luolei.template.common.utils.Constant;
import com.xiaoleilu.hutool.util.StrUtil;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * @author luolei
 * @email askluolei@gmail.com
 * @date 2017/10/15 22:01
 */
@Component
public class AccessTokenHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(String.class) && parameter.hasMethodAnnotation(AccessToken.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        boolean require = parameter.getMethodAnnotation(AccessToken.class).require();
        //从header中获取token
        String token = webRequest.getHeader(Constant.TOKEN_HEADER_KEY);

        //如果header中不存在token，则从参数中获取token
        if(StrUtil.isBlank(token)){
            token = webRequest.getParameter(Constant.TOKEN_HEADER_KEY);
        }
        if (StrUtil.isBlank(token) && require) {
            throw new TException("获取token失败");
        }
        return token;
    }
}
