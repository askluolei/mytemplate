package com.luolei.template.modules.sys.dao;

import com.luolei.template.common.jpa.BaseDao;
import com.luolei.template.modules.sys.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author luolei
 * @email askluolei@gmail.com
 * @date 2017/10/13 23:12
 */
public interface RoleDao extends BaseDao<RoleEntity> {

    List<RoleEntity> findByRoleNameContaining(String roleName);
}
