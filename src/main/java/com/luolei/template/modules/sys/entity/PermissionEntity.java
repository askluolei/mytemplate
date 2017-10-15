package com.luolei.template.modules.sys.entity;

import com.luolei.template.common.jpa.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

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
public class PermissionEntity extends BaseEntity {

    @Column(name = "tf_permission", length = 32, unique = true, nullable = false)
    private String permission;

    @Column(name = "tf_remark")
    private String remark;
}
