package com.luolei.template.common.security;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.luolei.template.common.utils.Constant;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;

import com.xiaoleilu.hutool.util.StrUtil;

/**
 * @author luolei
 * @email askluolei@gmail.com
 * @date 2017/10/14 0:20
 */
public class OAuth2Filter extends AuthenticatingFilter {

    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) throws Exception {
        //获取请求token
        String token = getRequestToken((HttpServletRequest) request);

        if(StrUtil.isBlank(token)){
            return null;
        }

        return new OAuth2Token(token);
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        //获取请求token，如果token不存在，直接返回401
        String token = getRequestToken((HttpServletRequest) request);
        if(StrUtil.isBlank(token)){
            throw new AuthenticationException("没找到token,uri:" + ((HttpServletRequest) request).getRequestURI());
        }

        return executeLogin(request, response);
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        if (servletRequest.getMethod().equalsIgnoreCase("options") || StrUtil.isNotBlank(servletRequest.getHeader("debug"))) {
            return true;
        }

        return false;
    }

    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request,
                                     ServletResponse response) {
        throw e;
    }

    /**
     * 获取请求的token
     */
    private String getRequestToken(HttpServletRequest httpRequest){
        //从header中获取token
        String token = httpRequest.getHeader(Constant.TOKEN_HEADER_KEY);

        //如果header中不存在token，则从参数中获取token
        if(StrUtil.isBlank(token)){
            token = httpRequest.getParameter(Constant.TOKEN_HEADER_KEY);
        }

        return token;
    }

}
