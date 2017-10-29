package com.luolei.template.dao;

import com.luolei.template.modules.sys.dao.PermissionDao;
import com.luolei.template.modules.sys.dao.RoleDao;
import com.luolei.template.modules.sys.entity.PermissionEntity;
import com.luolei.template.modules.sys.entity.RoleEntity;
import com.luolei.template.modules.sys.service.AuthzService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 * @author luolei
 * @email askluolei@gmail.com
 * @date 2017/10/29 20:48
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class DaoTest {

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private PermissionDao permissionDao;

    @Autowired
    private AuthzService authzService;

    @Test
    public void test() {
        PermissionEntity p1 = permissionDao.findAll().get(0);
        RoleEntity r1 = new RoleEntity();
        r1.setRoleName("ADMIN");
        r1.setRemark("管理员");
        r1.setPermissions(Arrays.asList(p1));
        r1 = roleDao.save(r1);
        long roleID = r1.getId();
        assertThat(roleDao.count()).isEqualTo(1L);
        assertThat(roleDao.findAll().get(0).getPermissions().size()).isEqualTo(1);

        r1 = new RoleEntity();
        r1.setPermissions(Collections.emptyList());
        r1.setRemark("管理员1");
        r1.setRoleName("ADMIN");
        r1.setId(roleID);
        List<RoleEntity> roles = Arrays.asList(r1);

        authzService.updateRole(roles);

        assertThat(roleDao.count()).isEqualTo(1L);
        assertThat(roleDao.findAll().get(0).getPermissions().size()).isEqualTo(0);
    }
}
