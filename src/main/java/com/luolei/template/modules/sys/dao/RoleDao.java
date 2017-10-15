package com.luolei.template.modules.sys.dao;

import com.luolei.template.modules.sys.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author luolei
 * @email askluolei@gmail.com
 * @date 2017/10/13 23:12
 */
public interface RoleDao extends JpaRepository<RoleEntity, Long> {
}
