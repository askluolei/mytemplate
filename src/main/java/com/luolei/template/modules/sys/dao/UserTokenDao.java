package com.luolei.template.modules.sys.dao;

import com.luolei.template.modules.sys.entity.UserTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author luolei
 * @email askluolei@gmail.com
 * @date 2017/10/14 0:26
 */
public interface UserTokenDao extends JpaRepository<UserTokenEntity, Long> {

    UserTokenEntity findByToken(String token);
}
