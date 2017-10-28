package com.luolei.template.common;

import com.luolei.template.modules.job.entity.ScheduleJobEntity;
import com.luolei.template.modules.job.vo.ScheduleJobView;
import org.junit.Test;
import org.springframework.beans.BeanUtils;

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

}
