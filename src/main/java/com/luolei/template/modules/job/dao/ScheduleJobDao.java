package com.luolei.template.modules.job.dao;

import com.luolei.template.modules.job.entity.ScheduleJobEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author luolei
 * @email askluolei@gmail.com
 * @date 2017/10/13 23:29
 */
public interface ScheduleJobDao extends JpaRepository<ScheduleJobEntity, Long> {
}
