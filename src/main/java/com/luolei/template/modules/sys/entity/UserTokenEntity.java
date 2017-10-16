package com.luolei.template.modules.sys.entity;

import com.luolei.template.common.jpa.BaseEntity;
import com.luolei.template.modules.sys.entityenum.AuthPlatform;
import com.luolei.template.modules.sys.entityenum.AuthType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 用户登录后token凭证
 *
 * @author luolei
 * @email askluolei@gmail.com
 * @date 2017/10/14 0:23
 */
@Entity
@Table(name = "t_user_token")
@Getter
@Setter
public class UserTokenEntity extends BaseEntity {

    /**
     * accessToken
     */
    @Column(name = "tf_token", nullable = false, unique = true)
    private String accessToken;

    /**
     * refreshToken
     */
    @Column(name = "tf_refresh_token", unique = true)
    private String refreshToken;

    /**
     * 过期时间
     */
    @Column(name = "tf_expire_time")
    private LocalDateTime expireTime;

    /**
     * 关联sessionID
     * 预留
     */
    @Column(name = "tf_session_id")
    private String sessionID;

    /**
     * 认证方式
     */
    private AuthType authType;

    /**
     * 认证平台
     * PC，android等
     */
    private AuthPlatform authPlatform;

    /**
     * 登录的 IP
     */
    private String loginIp;

    /**
     * 用户信息
     */
    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
