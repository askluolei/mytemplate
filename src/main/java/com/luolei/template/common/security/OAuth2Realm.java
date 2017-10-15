package com.luolei.template.common.security;

import com.luolei.template.modules.sys.dao.UserDao;
import com.luolei.template.modules.sys.dao.UserTokenDao;
import com.luolei.template.modules.sys.entity.UserEntity;
import com.luolei.template.modules.sys.entity.UserTokenEntity;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author luolei
 * @email askluolei@gmail.com
 * @date 2017/10/14 0:27
 */
@Component
public class OAuth2Realm extends AuthorizingRealm {

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserTokenDao tokenDao;

    @Autowired
    private TokenGenerator tokenGenerator;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof OAuth2Token;
    }

    /**
     * 授权(验证权限时调用)
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        UserEntity user = (UserEntity) principalCollection.getPrimaryPrincipal();
        Set<String> roles = new HashSet<>();
        Set<String> permissions = new HashSet<>();
        user.getRoles().forEach(role -> {
            roles.add(role.getRoleName());
            role.getPermsiions().forEach(p -> {
                permissions.add(p.getPermission());
            });
        });
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo(roles);
        info.setStringPermissions(permissions);
        return info;
    }

    /**
     * 认证(登录时调用)
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String accessToken = (String) authenticationToken.getPrincipal();
        UserTokenEntity token = tokenDao.findByToken(accessToken);//需要检查这里的过期时间吗?
        if (Objects.isNull(token)) {
            throw new AuthenticationException("认证失败");
        }
        try {
            long userID = tokenGenerator.getUserID(accessToken);//这里会解析token里面的过期时间
            if (token.getUser().getId() != userID) {
                throw new AuthenticationException("认证失败,不合法的token");
            }
            SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(token.getUser(), accessToken, getName());
            return info;
        } catch (NumberFormatException e) {
            throw new AuthenticationException("认证失败,不合法的token");
        }
    }
}
