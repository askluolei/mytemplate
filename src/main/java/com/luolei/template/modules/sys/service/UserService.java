package com.luolei.template.modules.sys.service;

import com.luolei.template.common.exception.TException;
import com.luolei.template.common.security.PasswordEncoder;
import com.luolei.template.common.security.TokenGenerator;
import com.luolei.template.common.utils.Constant;
import com.luolei.template.common.utils.Sequence;
import com.luolei.template.config.TemplateProperties;
import com.luolei.template.modules.sys.dao.UserDao;
import com.luolei.template.modules.sys.dao.UserTokenDao;
import com.luolei.template.modules.sys.entity.UserEntity;
import com.luolei.template.modules.sys.entity.UserTokenEntity;
import com.luolei.template.modules.sys.entityenum.AuthPlatform;
import com.luolei.template.modules.sys.entityenum.AuthType;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.luolei.template.common.security.TokenGenerator.RANDOM_KEY;

/**
 * @author luolei
 * @email askluolei@gmail.com
 * @date 2017/10/15 21:12
 */
@Service
@Transactional(rollbackFor = Throwable.class)
public class UserService {

    private static final long TEMP_TOKEN_EXPIRE_SECOND = 10;

    private Logger logger = LoggerFactory.getLogger(getClass());

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

    @Autowired
    private Sequence sequence;

    /**
     * 用户名密码登陆
     * @param username
     * @param password
     * @param authType
     * @param platform
     * @param requestIp
     * @return
     */
    public Map<String, Object> login(String username, String password, AuthType authType, String platform, String requestIp, boolean rememberMe) {
        Map<String, Object> result = new HashMap<>();
        UserEntity user = userDao.findByUsername(username);
        if (Objects.isNull(user) || !passwordEncoder.matches(password, user.getPassword())) {
            throw new TException("登录失败");
        }
        result.put("user", user);
        long userId = user.getId();
        long random = sequence.nextId();

        UserTokenEntity tokenEntity = new UserTokenEntity();
        AuthPlatform authPlatform = AuthPlatform.from(platform);
        String accessToken = tokenGenerator.generateToken(userId, random);
        if (rememberMe) {
            String refreshToken = tokenGenerator.generateRefreshToken(user.getId(), random);
            tokenEntity.setRefreshToken(refreshToken);
        }
        tokenEntity.setAccessToken(accessToken);
        tokenEntity.setAuthPlatform(authPlatform);
        tokenEntity.setLoginIp(requestIp);
        tokenEntity.setAuthType(authType);
        tokenEntity.setUser(user);
        long expireSecond = Objects.isNull(properties.getExpireTime()) ? Constant.TOKEN_EXPIRE_TIME : properties.getExpireTime();
        tokenEntity.setExpireTime(LocalDateTime.now().plusSeconds(expireSecond));
        tokenEntity = tokenDao.save(tokenEntity);

        clearToken(userId, authPlatform);

        List<UserTokenEntity> tokens = tokenDao.findAllByUserAndAuthPlatform(user, authPlatform);
        result.put("token", tokenEntity);
        result.put("onlineCount", tokens.stream().filter(this::isValid).map(UserTokenEntity::getRandom).distinct().count());
        //生成临时token，用来踢人
        result.put("tempToken", tokenGenerator.genTempToken(TEMP_TOKEN_EXPIRE_SECOND));
        return result;
    }

    private boolean isValid(UserTokenEntity token) {
        long expireSecond = Objects.isNull(properties.getExpireTime()) ? Constant.TOKEN_EXPIRE_TIME : properties.getExpireTime();
        return token.getCreateTime().isAfter(LocalDateTime.now().plusSeconds(-expireSecond));
    }

    /**
     * refreshToken 登陆
     * @param refreshToken
     * @param authType
     * @param platform
     * @return
     */
    public Map<String, Object> loginByToken(String refreshToken, AuthType authType, String platform, String requestIP) {
        Map<String, Object> result = new HashMap<>();
        UserTokenEntity tokenEntity = tokenDao.findByRefreshToken(refreshToken);
        if (Objects.isNull(tokenEntity)) {
            throw new TException("登录失败");
        }
        long userId = tokenGenerator.getUserID(refreshToken);
        if (tokenEntity.getUser().getId() != userId || tokenEntity.getAuthPlatform() != AuthPlatform.from(platform)) {
            //这里要注意一下
            throw new TException("登录失败");
        }
        result.put("user", tokenEntity.getUser());
        long random = tokenGenerator.getClaimByToken(refreshToken).get(RANDOM_KEY, long.class);
        UserTokenEntity token = new UserTokenEntity();
        AuthPlatform authPlatform = AuthPlatform.from(platform);
        String accessToken = tokenGenerator.generateToken(userId, random);
        token.setAuthType(authType);
        token.setAccessToken(accessToken);
        token.setAuthPlatform(authPlatform);
        token.setLoginIp(requestIP);
        token.setRefreshToken(refreshToken);
        token.setRandom(random);
        long expireSecond = Objects.isNull(properties.getExpireTime()) ? Constant.TOKEN_EXPIRE_TIME : properties.getExpireTime();
        token.setExpireTime(LocalDateTime.now().plusSeconds(expireSecond));
        token = tokenDao.save(token);
        result.put("token", token);

        clearToken(userId, authPlatform);
        return result;
    }

    /**
     * 刷新 AccessToken 直接新增记录就行了
     * 以前的记录不删除，前端只需要提交1-2分钟刷新AccessToken
     * 那么原来的token，跟现在的token都是可以使用的
     * 刷新token的时候维持 random 不变
     *
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
        Claims claim = tokenGenerator.getClaimByToken(accessToken);
        long random = claim.get(RANDOM_KEY, long.class);
        AuthPlatform authPlatform = AuthPlatform.from(platform);

        if (tokenEntity.getUser().getId() != userID || tokenEntity.getAuthPlatform() != authPlatform || !tokenEntity.getLoginIp().equalsIgnoreCase(requestIp)) {
            throw new TException("不合法的token");
        }

        UserTokenEntity newToken = new UserTokenEntity();
        newToken.setAccessToken(tokenGenerator.generateToken(userID, random));
        newToken.setAuthType(tokenEntity.getAuthType());
        newToken.setAuthPlatform(authPlatform);
        newToken.setLoginIp(requestIp);
        newToken.setUser(tokenEntity.getUser());
        newToken.setRandom(random);
        long expireSecond = Objects.isNull(properties.getExpireTime()) ? Constant.TOKEN_EXPIRE_TIME : properties.getExpireTime();
        newToken.setExpireTime(LocalDateTime.now().plusSeconds(expireSecond));
        newToken = tokenDao.save(newToken);
        return newToken;
    }

    /**
     * 登出
     * @param accessToken
     */
    public void logout(String accessToken, String platform, UserEntity user) {
        Claims claim = tokenGenerator.getClaimByToken(accessToken);
        long random = claim.get(RANDOM_KEY, long.class);
        AuthPlatform authPlatform = AuthPlatform.from(platform);
        List<UserTokenEntity> tokens = tokenDao.findAllByUserAndAuthPlatform(user, authPlatform);
        List<UserTokenEntity> toDelete = tokens.stream().filter(token -> token.getRandom() == random).collect(Collectors.toList());
        tokenDao.delete(toDelete);
    }

    /**
     * 踢出其他在线用户
     * @param accessToken
     * @param platform
     */
    public void forceOffline(String accessToken, String platform, UserEntity user) {
        Claims claim = tokenGenerator.getClaimByToken(accessToken);
        long random = claim.get(RANDOM_KEY, Long.class);
        AuthPlatform authPlatform = AuthPlatform.from(platform);
        List<UserTokenEntity> tokens = tokenDao.findAllByUserAndAuthPlatform(user, authPlatform);
        //注意，只保留登录用户的random，其他的全部删除，这样 refreshToken也失效
        List<UserTokenEntity> toDelete = tokens.stream().filter(token -> token.getRandom() != random).collect(Collectors.toList());
        tokenDao.delete(toDelete);
    }

    /**
     * 清理过期token
     */
    private void clearToken(long userID, AuthPlatform authPlatform) {
        UserEntity user = new UserEntity();
        user.setId(userID);
        List<UserTokenEntity> tokens = tokenDao.findAllByUserAndAuthPlatform(user, authPlatform);
        List<UserTokenEntity> toDelete = tokens.stream().filter(token -> !isValid(token)).collect(Collectors.toList());
        tokenDao.delete(toDelete);
    }
}
