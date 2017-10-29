package com.luolei.template;

import com.luolei.template.common.annotation.AuthzExplain;
import com.luolei.template.modules.sys.controller.AuthorizeController;
import com.luolei.template.modules.sys.dao.PermissionDao;
import com.luolei.template.modules.sys.entity.PermissionEntity;
import com.xiaoleilu.hutool.lang.ClassScaner;
import com.xiaoleilu.hutool.util.ClassUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Method;
import java.util.*;

@SpringBootApplication
public class TemplateApplication implements CommandLineRunner{

    public static void main(String[] args) {
        SpringApplication.run(TemplateApplication.class, args);
    }

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private PermissionDao permissionDao;

    @Value("${template.permission.init:false}")
    private boolean init;

    @Override
    public void run(String... args) throws Exception {
        if (init) {
            Set<String> permissionKeys = new HashSet<>();
            List<PermissionEntity> entities = new ArrayList<>();
            Set<Class<?>> classes = ClassScaner.scanPackage(getClass().getPackage().getName());
            for (Class<?> clazz : classes) {
                if (clazz.isAnnotationPresent(RequiresPermissions.class)) {
                    RequiresPermissions requiresPermissions = clazz.getAnnotation(RequiresPermissions.class);
                    String[] permissions = requiresPermissions.value();
                    String remark = "";
                    if (clazz.isAnnotationPresent(AuthzExplain.class)) {
                        remark = clazz.getAnnotation(AuthzExplain.class).value();
                    }
                    dealPermission(permissions, permissionKeys, entities, remark);
                }

                Method[] methods = clazz.getMethods();
                for (Method method : methods) {
                    if (method.isAnnotationPresent(RequiresPermissions.class)) {
                        RequiresPermissions requiresPermissions = method.getAnnotation(RequiresPermissions.class);
                        String[] permissions = requiresPermissions.value();
                        String remark = "";
                        if (method.isAnnotationPresent(AuthzExplain.class)) {
                            remark = method.getAnnotation(AuthzExplain.class).value();
                        }
                        dealPermission(permissions, permissionKeys, entities, remark);
                    }
                }
            }
            List<PermissionEntity> permissions = new ArrayList<>(entities.size());
            for (PermissionEntity entity : entities) {
                PermissionEntity permission = permissionDao.findByPermission(entity.getPermission());
                    if (Objects.isNull(permission)) {
                        permissions.add(entity);
                    }
                }
            permissionDao.save(permissions);
        }
    }

    private void dealPermission(String[] permissions,  Set<String> permissionKeys, List<PermissionEntity> entities, String remark) {
        for (String permission : permissions) {
            if (!permissionKeys.contains(permission)) {
                permissionKeys.add(permission);
                PermissionEntity entity = new PermissionEntity();
                entity.setPermission(permission);
                entity.setRemark(remark);
                entities.add(entity);
            }
        }
    }
}
