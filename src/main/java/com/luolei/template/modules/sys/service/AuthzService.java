package com.luolei.template.modules.sys.service;

import com.luolei.template.common.exception.TException;
import com.luolei.template.modules.sys.dao.PermissionDao;
import com.luolei.template.modules.sys.dao.RoleDao;
import com.luolei.template.modules.sys.dao.UserDao;
import com.luolei.template.modules.sys.entity.PermissionEntity;
import com.luolei.template.modules.sys.entity.RoleEntity;
import com.xiaoleilu.hutool.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author luolei
 * @email askluolei@gmail.com
 * @date 2017/10/28 18:12
 */
@Service
@Transactional
public class AuthzService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private PermissionDao permissionDao;

    private List<PermissionEntity> saveOrUpdate(List<PermissionEntity> permissions) {
        List<PermissionEntity> entities = new ArrayList<>(permissions.size());
        Set<Long> ids = new HashSet<>(permissions.size());
        for (PermissionEntity entity : permissions) {
            if (Objects.nonNull(entity.getId())) {
                PermissionEntity temp = permissionDao.findOne(entity.getId());
                if (Objects.isNull(temp)) {
                    entity.setId(null);
                    entities.add(entity);
                } else {
                    if (ids.contains(entity.getId())) {
                        throw new TException("id:" + entity.getId() + " 在请求对象中存在重复");
                    }
                    temp.setPermission(entity.getPermission());
                    temp.setRemark(entity.getRemark());
                    entities.add(temp);
                    ids.add(entity.getId());
                }
            } else {
                if (Objects.nonNull(permissionDao.findByPermission(entity.getPermission()))) {
                    throw new TException("permission:" + entity.getPermission() + " 已经存在");
                }
                entities.add(entity);
            }
        }
        return permissionDao.save(entities);
    }

    private List<PermissionEntity> updatePermissionRemark(List<PermissionEntity> permissions) {
        List<PermissionEntity> entities = new ArrayList<>(permissions.size());
        for (PermissionEntity entity : permissions) {
            if (Objects.nonNull(entity.getId())) {
                PermissionEntity temp = permissionDao.findOne(entity.getId());
                if (Objects.nonNull(temp)) {
                    //permission 不能修改，只能修改remark
                    entity.setPermission(temp.getPermission());
                    entities.add(entity);
                }
            }
        }
        return permissionDao.save(entities);
    }

    public List<PermissionEntity> addPermission(List<PermissionEntity> permissions) {
        return saveOrUpdate(permissions);
    }

    public void deletePermission(List<Long> ids) {
        permissionDao.delete(permissionDao.findAll(ids));
    }

    public List<PermissionEntity> updatePermission(List<PermissionEntity> permissions) {
        return updatePermissionRemark(permissions);
    }

    public List<PermissionEntity> queryPermission(String permission) {
        if (StrUtil.isBlank(permission)) {
            return permissionDao.findAll();
        } else {
            return permissionDao.findByPermissionContaining(permission);
        }
    }

    private List<RoleEntity> saveOrUpdateRole(List<RoleEntity> roles) {
        List<RoleEntity> toSaveOrUpdate = new ArrayList<>(roles.size());
        for (RoleEntity role: roles) {
            if (Objects.nonNull(role.getId()) && roleDao.exists(role.getId())) {
                //修改
                //修改之后的权限
                List<PermissionEntity> permissions = new ArrayList<>();
                if (Objects.nonNull(role.getPermissions()) && !role.getPermissions().isEmpty()) {
                    role.getPermissions().forEach(p -> {
                        PermissionEntity tempP =  permissionDao.findOne(p.getId());
                        if (Objects.nonNull(tempP)) {
                            permissions.add(tempP);
                        }
                    });
                }
                RoleEntity temp = roleDao.findOne(role.getId());
                temp.getPermissions().clear();
                temp = roleDao.save(temp);
                temp.setPermissions(permissions);
                temp.setRemark(role.getRemark());
                if (StrUtil.isNotBlank(role.getRoleName())) {
                    temp.setRoleName(role.getRoleName());
                }
                toSaveOrUpdate.add(temp);
            } else {
                //新增
                toSaveOrUpdate.add(role);
            }
        }
        return roleDao.save(toSaveOrUpdate);
    }

    public List<RoleEntity> addRoles(List<RoleEntity> roles) {
        return saveOrUpdateRole(roles);
    }

    public void deleteRole(List<Long> ids) {
        ids.forEach(id -> {
            roleDao.delete(id);
        });
    }

    public List<RoleEntity> updateRole(List<RoleEntity> roles) {
        return saveOrUpdateRole(roles);
    }

    public List<RoleEntity> queryRole(String roleName) {
        List<RoleEntity> roles;
        if (StrUtil.isBlank(roleName)) {
            roles = roleDao.findAll();
        } else {
            roles = roleDao.findByRoleNameContaining(roleName);
        }
        roles.forEach(role -> {
            role.getPermissions().size();
        });
        return roles;
    }

}
