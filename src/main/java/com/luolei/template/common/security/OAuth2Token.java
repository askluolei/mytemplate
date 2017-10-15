package com.luolei.template.common.security;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * token 令牌
 *
 * @author luolei
 * @email askluolei@gmail.com
 * @date 2017/10/14 0:17
 */
public class OAuth2Token implements AuthenticationToken {
    private static final long serialVersionUID = 1L;

    private String token;

    public OAuth2Token(String token){
        this.token = token;
    }

    public Object getPrincipal() {
        return token;
    }

    public Object getCredentials() {
        return token;
    }

}
