package com.luolei.template.modules.sys.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.luolei.template.common.jpa.BaseEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * 权限
 * @author luolei
 * @email askluolei@gmail.com
 * @date 2017/10/13 22:58
 */
@Entity
@Table(name = "t_permission")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true, exclude = {"roles"})
public class PermissionEntity extends BaseEntity {

    @Column(name = "tf_permission", length = 32, unique = true, nullable = false)
    private String permission;

    @Column(name = "tf_remark")
    private String remark;

    @ManyToMany(mappedBy = "permissions", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonIgnoreProperties("permissions")
    private List<RoleEntity> roles;
}
