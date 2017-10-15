package com.luolei.template.modules.sys.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.luolei.template.common.jpa.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * 用户
 * @author luolei
 * @email askluolei@gmail.com
 * @date 2017/10/13 23:05
 */
@Entity
@Table(name = "t_user")
@Getter
@Setter
public class UserEntity extends BaseEntity {

    @Column(name = "tf_username", length = 32, nullable = false, unique = true)
    private String username;

    @Column(name = "tf_password", length = 128, nullable = false)
    @JsonIgnore
    private String password;

    @Column(name = "tf_email", length = 64)
    private String email;

    @Column(name = "tf_mobile", length = 32)
    private String mobile;

    /**
     * 状态  0：禁用   1：正常
     */
    @Column(name = "tf_status")
    @ColumnDefault("0")
    private Integer status;

    /**
     * 帐号过期时间
     */
    @Column(name = "tf_expireTime")
    private LocalDateTime expireTime;

    /**
     * 角色
     * 一个用户可以有多个角色
     */
    @ManyToMany
    @JoinTable(name = "t_user_rle",
    joinColumns = {@JoinColumn(name = "user_id")},
    inverseJoinColumns = {@JoinColumn(name = "role_id")},
    indexes = {@Index(columnList = "user_id")})
    private Set<RoleEntity> roles;
}
