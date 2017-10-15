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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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

    public UserTokenEntity login(String username, String password) {
        UserEntity user = userDao.findByUsername(username);
        if (Objects.isNull(user) || !passwordEncoder.matches(password, user.getPassword())) {
            throw new TException("登录失败");
        }
        String accessToken = tokenGenerator.generateToken(user.getId());
        UserTokenEntity tokenEntity = new UserTokenEntity();
        tokenEntity.setToken(accessToken);
        tokenEntity.setUser(user);
        long expireSecond = Objects.isNull(properties.getExpireTime()) ? Constant.TOKEN_EXPIRE_TIME : properties.getExpireTime();
        tokenEntity.setExpireTime(LocalDateTime.now().plusSeconds(expireSecond));
        tokenDao.save(tokenEntity);
        return tokenEntity;
    }

    public void logout(String accessToken) {
        UserTokenEntity tokenEntity = tokenDao.findByToken(accessToken);
        tokenDao.delete(tokenEntity);
    }
}
