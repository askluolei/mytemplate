package com.luolei.template.common;

import com.luolei.template.modules.job.entity.ScheduleJobEntity;
import com.luolei.template.modules.job.vo.ScheduleJobView;
import com.luolei.template.modules.sys.entity.PermissionEntity;
import com.luolei.template.modules.sys.entity.RoleEntity;
import com.luolei.template.modules.sys.vo.PermissionView;
import com.luolei.template.modules.sys.vo.RoleView;
import org.junit.Test;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.BeanUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author luolei
 * @email askluolei@gmail.com
 * @date 2017/10/28 0:39
 */
public class BeanUtilTest {

    @Test
    public void test() {
        ScheduleJobEntity jobEntity = new ScheduleJobEntity();
        jobEntity.setStatus(1);
        jobEntity.setMethodName("method");
        jobEntity.setBeanName("bean");
        jobEntity.setCronExpression("0 1 2");
        jobEntity.setParams("params");
        jobEntity.setRemark("remark");
        jobEntity.setId(1L);

        ScheduleJobView view = new ScheduleJobView();

        BeanUtils.copyProperties(jobEntity, view);
        System.out.println(view);
        System.out.println(view.getId());
    }

    @Test
    public void test2() {
        PermissionView v1 = new PermissionView();
        v1.setPermission("v1");
        v1.setRemark("vr1");

        PermissionView v2 = new PermissionView();
        v2.setPermission("v2");
        v2.setRemark("vr2");

        List<PermissionView> views = Arrays.asList(v1, v2);

        List<PermissionEntity> entities = views.stream().map(v -> {
            PermissionEntity entity = new PermissionEntity();
            BeanUtils.copyProperties(v, entity);
            return entity;
        }).collect(Collectors.toList());

        entities.forEach(p -> {
            System.out.println(p.getPermission() + " " + p.getRemark());
        });
    }

    @Test
    public void test3() {
        PermissionView v1 = new PermissionView();
        v1.setPermission("v1");
        v1.setRemark("vr1");

        PermissionView v2 = new PermissionView();
        v2.setPermission("v2");
        v2.setRemark("vr2");

        RoleView r1 = new RoleView();
        r1.setRoleName("r1");
        r1.setRemark("rr1");
        r1.setPermissions(Arrays.asList(v1, v2));

        RoleEntity roleEntity = new RoleEntity();
        BeanUtils.copyProperties(r1, roleEntity, "permissions");

        System.out.println(roleEntity.getRoleName());
        System.out.println(roleEntity.getRemark());

        if (Objects.nonNull(roleEntity.getPermissions())) {
            roleEntity.getPermissions().forEach(p -> {
                System.out.println(p.getPermission());
            });
        }
    }

}
