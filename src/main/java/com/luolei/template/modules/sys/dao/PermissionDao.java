package com.luolei.template.modules.sys.dao;

import com.luolei.template.modules.sys.entity.PermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author luolei
 * @email askluolei@gmail.com
 * @date 2017/10/13 23:13
 */
public interface PermissionDao extends JpaRepository<PermissionEntity, Long> {
}
