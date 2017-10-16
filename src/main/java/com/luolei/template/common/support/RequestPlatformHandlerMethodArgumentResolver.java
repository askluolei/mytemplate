package com.luolei.template.common.support;

import com.luolei.template.common.annotation.RequestPlatform;
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
 * @date 2017/10/16 21:47
 */
@Component
public class RequestPlatformHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(String.class) && parameter.hasMethodAnnotation(RequestPlatform.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        boolean require = parameter.getMethodAnnotation(RequestPlatform.class).require();
        String platform = webRequest.getHeader(Constant.REQUEST_PLATFORM);
        if (StrUtil.isBlank(platform) && require) {
            throw new IllegalArgumentException("HTTP Header 没有 ：" + Constant.REQUEST_PLATFORM);
        }
        return platform;
    }
}
