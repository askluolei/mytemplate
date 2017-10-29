package com.luolei.template.modules.sys.controller;

import com.luolei.template.common.annotation.AuthzExplain;
import com.luolei.template.common.api.R;
import com.luolei.template.modules.sys.entity.PermissionEntity;
import com.luolei.template.modules.sys.entity.RoleEntity;
import com.luolei.template.modules.sys.service.AuthzService;
import com.luolei.template.modules.sys.vo.PermissionView;
import com.luolei.template.modules.sys.vo.RoleView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author luolei
 * @email askluolei@gmail.com
 * @date 2017/10/28 18:08
 */
@Api(value = "/authz", description = "授权相关")
@RestController
@RequestMapping(path = "/sys/authz", produces = {"application/json; charset=UTF-8"})
public class AuthorizeController {

    @Autowired
    private AuthzService authzService;

    private List<PermissionEntity> convert(List<PermissionView> views) {
        return views.stream().map(v -> {
            PermissionEntity entity = new PermissionEntity();
            BeanUtils.copyProperties(v, entity);
            return entity;
        }).collect(Collectors.toList());
    }

//    @ApiOperation(value = "添加权限", notes = "添加权限")
//    @PostMapping("permission")
//    public R addPermission(@RequestBody List<PermissionView> permissions) {
//        return R.ok().with("permissions", authzService.addPermission(convert(permissions)));
//    }
//
//    @ApiOperation(value = "删除权限", notes = "根据主键删除")
//    @DeleteMapping("permission")
//    public R deletePermission(@RequestBody List<Long> ids) {
//        authzService.deletePermission(ids);
//        return R.ok();
//    }

    /**
     * 权限都是通过注解 @RequiresPermissions 写死的，因此在第一次初始化的时候权限就写到表中，不允许修改（如果修改了，那有的代码就可能没人有权限访问了）
     */
    @ApiOperation(value = "更新权限", notes = "更新权限, 只能修改权限备注信息，返回的是修改成功的权限信息")
    @PutMapping("permission")
    @RequiresPermissions({"authorization:modify"})
    @AuthzExplain("更新权限备注")
    public R updatePermission(@RequestBody List<PermissionView> permissions) {
        return R.ok().with("permissions", authzService.updatePermission(convert(permissions)));
    }

    @ApiOperation(value = "权限查询", notes = "根据权限模糊查询，如果没带条件就返回全部权限")
    @ApiImplicitParam(name = "permission", value = "权限简码", paramType = "query", dataType = "String")
    @GetMapping("permission")
    public R queryPermission(@RequestParam(name = "permission", required = false) String permission) {
        return R.ok().with("permissions", authzService.queryPermission(permission));
    }

    private List<RoleEntity> convertRole(List<RoleView> views) {
        List<RoleEntity> roles = new ArrayList<>(views.size());
        views.forEach(view -> {
            RoleEntity role = new RoleEntity();
            BeanUtils.copyProperties(view, role, "permissions");
            if (Objects.nonNull(view.getPermissions()) && !view.getPermissions().isEmpty()) {
                role.setPermissions(convert(view.getPermissions()));
            }
            roles.add(role);
        });
        return roles;
    }

    @ApiOperation(value = "添加角色", notes = "添加角色，可以附带上权限")
//    @ApiImplicitParam(name = "roles", value = "角色列表", paramType = "body", dataType = "List<RoleView>")
    @PostMapping("role")
    public R addRole(@RequestBody List<RoleView> roles) {
        return R.ok().with("roles", authzService.addRoles(convertRole(roles)));
    }

    @ApiOperation(value = "删除角色", notes = "删除角色")
//    @ApiImplicitParam(name = "ids", value = "角色id列表", paramType = "body", dataType = "List<Long>")
    @DeleteMapping("role")
    public R deleteRole(@RequestBody List<Long> ids) {
        authzService.deleteRole(ids);
        return R.ok();
    }

    @ApiOperation(value = "修改角色", notes = "可以修改角色的权限列表")
//    @ApiImplicitParam(name = "roles", value = "角色列表", paramType = "body", dataType = "List<RoleView>")
    @PutMapping("role")
    public R updateRole(@RequestBody List<RoleView> roles) {
        return R.ok().with("roles", authzService.updateRole(convertRole(roles)));
    }

    @ApiOperation(value = "查询角色", notes = "查询权限列表，或者根据权限名查询")
//    @ApiImplicitParam(name = "roleName", value = "角色简码", paramType = "query", dataType = "String")
    @GetMapping("role")
    public R queryRole(@RequestParam(name = "roleName", required = false) String roleName) {
        return R.ok().with("roles", authzService.queryRole(roleName));
    }

}
