package com.luolei.template.datasource;

import com.luolei.template.modules.job.dao.ScheduleJobDao;
import com.luolei.template.modules.job.entity.ScheduleJobEntity;
import com.luolei.template.modules.sys.dao.PermissionDao;
import com.luolei.template.modules.sys.entity.PermissionEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author luolei
 * @email askluolei@gmail.com
 * @date 2017/10/13 23:35
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class MultiDataSourceTest {

    @Autowired
    private ScheduleJobDao jobDao;

    @Autowired
    private PermissionDao permissionDao;

    @Test
    public void test() {
        PermissionEntity p1 = new PermissionEntity();
        p1.setPermission("p1");
        p1.setRemark("test");
        permissionDao.save(p1);

        ScheduleJobEntity j1 = new ScheduleJobEntity();
        j1.setBeanName("bean");
        j1.setMethodName("method");
        jobDao.save(j1);
    }
}
