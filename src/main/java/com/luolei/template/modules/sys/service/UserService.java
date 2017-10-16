package com.luolei.template.modules.sys.service;

import com.luolei.template.common.exception.TException;
import com.luolei.template.common.security.PasswordEncoder;
import com.luolei.template.common.security.TokenGenerator;
import com.luolei.template.common.utils.Constant;
import com.luolei.template.config.TemplateProperties;
import com.luolei.template.modules.sys.dao.UserDao;
import com.luolei.template.modules.sys.dao.UserTokenDao;
import com.luolei.template.modules.sys.entity.UserEntity;
import com.luolei.template.modules.sys.entity.UserTokenEntity;
import com.luolei.template.modules.sys.entityenum.AuthPlatform;
import com.luolei.template.modules.sys.entityenum.AuthType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @author luolei
 * @email askluolei@gmail.com
 * @date 2017/10/15 21:12
 */
@Service
@Transactional
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserTokenDao tokenDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenGenerator tokenGenerator;

    @Autowired
    private TemplateProperties properties;

    /**
     * 用户名密码登陆
     * @param username
     * @param password
     * @param authType
     * @param platform
     * @param requestIp
     * @return
     */
    public UserTokenEntity login(String username, String password, AuthType authType, String platform, String requestIp) {
        UserEntity user = userDao.findByUsername(username);
        if (Objects.isNull(user) || !passwordEncoder.matches(password, user.getPassword())) {
            throw new TException("登录失败");
        }
        AuthPlatform authPlatform = AuthPlatform.from(platform);
        clearToken(user.getId(), authPlatform);
        String accessToken = tokenGenerator.generateToken(user.getId());
        String refreshToken = tokenGenerator.generateRefreshToken(user.getId());
        UserTokenEntity tokenEntity = new UserTokenEntity();
        tokenEntity.setAccessToken(accessToken);
        tokenEntity.setRefreshToken(refreshToken);
        tokenEntity.setAuthPlatform(authPlatform);
        tokenEntity.setLoginIp(requestIp);
        tokenEntity.setAuthType(authType);
        tokenEntity.setUser(user);
        long expireSecond = Objects.isNull(properties.getExpireTime()) ? Constant.TOKEN_EXPIRE_TIME : properties.getExpireTime();
        tokenEntity.setExpireTime(LocalDateTime.now().plusSeconds(expireSecond));
        tokenDao.save(tokenEntity);
        return tokenEntity;
    }

    /**
     * refreshToken 登陆
     * @param refreshToken
     * @param authType
     * @param platform
     * @return
     */
    public UserTokenEntity loginByToken(String refreshToken, AuthType authType, String platform) {
        UserTokenEntity tokenEntity = tokenDao.findByRefreshToken(refreshToken);
        if (Objects.isNull(tokenEntity)) {
            throw new TException("登录失败");
        }
        long userID = tokenGenerator.getUserID(refreshToken);
        if (tokenEntity.getUser().getId() != userID || tokenEntity.getAuthPlatform() != AuthPlatform.from(platform)) {
            //这里要注意一下
            throw new TException("登录失败");
        }
        clearToken(userID, AuthPlatform.from(platform));

        String accessToken = tokenGenerator.generateToken(userID);
        tokenEntity.setAuthType(authType);
        tokenEntity.setAccessToken(accessToken);
        long expireSecond = Objects.isNull(properties.getExpireTime()) ? Constant.TOKEN_EXPIRE_TIME : properties.getExpireTime();
        tokenEntity.setExpireTime(LocalDateTime.now().plusSeconds(expireSecond));
        tokenDao.save(tokenEntity);
        return tokenEntity;
    }

    /**
     * 刷新 AccessToken
     * @param accessToken
     * @param platform
     * @param requestIp
     * @return
     */
    public UserTokenEntity refreshAccessToken(String accessToken, String platform, String requestIp) {
        UserTokenEntity tokenEntity = tokenDao.findByAccessToken(accessToken);
        if (Objects.isNull(tokenEntity)) {
            throw new TException("token 刷新失败");
        }
        long userID = tokenGenerator.getUserID(accessToken);
        AuthPlatform authPlatform = AuthPlatform.from(platform);
        if (tokenEntity.getUser().getId() != userID || tokenEntity.getAuthPlatform() != authPlatform || !tokenEntity.getLoginIp().equalsIgnoreCase(requestIp)) {
            throw new TException("不合法的token");
        }
        UserTokenEntity newToken = new UserTokenEntity();
        newToken.setAccessToken(accessToken);
        newToken.setRefreshToken(tokenEntity.getRefreshToken());
        newToken.setAuthType(tokenEntity.getAuthType());
        newToken.setAuthPlatform(authPlatform);
        newToken.setLoginIp(requestIp);
        newToken.setUser(tokenEntity.getUser());
        long expireSecond = Objects.isNull(properties.getExpireTime()) ? Constant.TOKEN_EXPIRE_TIME : properties.getExpireTime();
        newToken.setExpireTime(LocalDateTime.now().plusSeconds(expireSecond));
        tokenDao.save(newToken);
        return newToken;
    }

    /**
     * 登出
     * @param accessToken
     */
    public void logout(String accessToken, String platform) {
        long userID = tokenGenerator.getUserID(accessToken);
        clearToken(userID, AuthPlatform.from(platform));
    }

    private void clearToken(long userID, AuthPlatform authPlatform) {
        UserEntity user = new UserEntity();
        user.setId(userID);
        List<UserTokenEntity> tokens = tokenDao.findAllByUserAndAuthPlatform(user, authPlatform);
        tokenDao.delete(tokens);
    }
}
