package com.luolei.template.modules.sys.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.luolei.template.common.jpa.BaseEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

/**
 * 角色
 *
 * @author luolei
 * @email askluolei@gmail.com
 * @date 2017/10/13 23:00
 */
@Entity
@Table(name = "t_role")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true, exclude = {"users", "permissions"})
public class RoleEntity extends BaseEntity {

    @Column(name = "tf_role_name", length = 32, unique = true, nullable = false)
    private String roleName;

    @Column(name = "tf_remark")
    private String remark;

    @ManyToMany(mappedBy = "roles", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonIgnoreProperties("roles")
    private List<UserEntity> users;

    /**
     * 角色 权限多对多
     */
    @ManyToMany
    @JoinTable(name = "t_role_permission",
            joinColumns = {@JoinColumn(name = "role_id")},
            inverseJoinColumns = {@JoinColumn(name = "permission_id")},
            indexes = {@Index(columnList = "role_id")})
    @JsonIgnoreProperties("roles")
    private List<PermissionEntity> permissions;
}
