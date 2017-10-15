package com.luolei.template.modules.job.dao;

import com.luolei.template.modules.job.entity.ScheduleJobLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author luolei
 * @email askluolei@gmail.com
 * @date 2017/10/13 23:30
 */
public interface ScheduleJobLogDao extends JpaRepository<ScheduleJobLogEntity, Long> {
}
