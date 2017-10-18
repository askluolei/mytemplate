package com.luolei.template.modules.sys.dao;

import com.luolei.template.common.jpa.BaseDao;
import com.luolei.template.modules.sys.entity.UserEntity;
import com.luolei.template.modules.sys.entity.UserTokenEntity;
import com.luolei.template.modules.sys.entityenum.AuthPlatform;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author luolei
 * @email askluolei@gmail.com
 * @date 2017/10/14 0:26
 */
public interface UserTokenDao extends BaseDao<UserTokenEntity> {

    UserTokenEntity findByAccessToken(String accessToken);

    UserTokenEntity findByRefreshToken(String refreshToken);

    List<UserTokenEntity> findAllByUserAndAuthPlatform(UserEntity user, AuthPlatform authPlatform);
}
