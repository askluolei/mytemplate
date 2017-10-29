package com.luolei.template.modules.sys.dao;

import com.luolei.template.common.jpa.BaseDao;
import com.luolei.template.modules.sys.entity.PermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author luolei
 * @email askluolei@gmail.com
 * @date 2017/10/13 23:13
 */
public interface PermissionDao extends BaseDao<PermissionEntity> {

    PermissionEntity findByPermission(String permission);

    List<PermissionEntity> findByPermissionContaining(String permission);
}
