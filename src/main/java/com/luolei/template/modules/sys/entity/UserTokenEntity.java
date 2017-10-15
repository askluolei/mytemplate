package com.luolei.template.modules.sys.entity;

import com.luolei.template.common.jpa.BaseEntity;
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
     * token
     */
    @Column(name = "tf_token", nullable = false, unique = true)
    private String token;

    /**
     * 过期时间
     */
    @Column(name = "tf_expire_time")
    private LocalDateTime expireTime;

    /**
     * 用户信息
     */
    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
