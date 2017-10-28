package com.luolei.template.modules.job.service;

import com.luolei.template.common.utils.Constant;
import com.luolei.template.common.utils.JpaUtils;
import com.luolei.template.modules.job.dao.ScheduleJobDao;
import com.luolei.template.modules.job.entity.ScheduleJobEntity;
import com.luolei.template.modules.job.utils.ScheduleUtils;
import com.luolei.template.modules.job.vo.ScheduleJobView;
import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Objects;

/**
 * @author luolei
 * @email askluolei@gmail.com
 * @date 2017/10/14 13:33
 */
@Service
@Transactional(rollbackFor = Throwable.class)
public class ScheduleJobService {

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private ScheduleJobDao jobDao;

    /**
     * 项目启动时，初始化定时器
     */
    @PostConstruct
    public void init() {
        List<ScheduleJobEntity> scheduleJobList = jobDao.findAll();
        for (ScheduleJobEntity scheduleJob : scheduleJobList) {
            CronTrigger cronTrigger = ScheduleUtils.getCronTrigger(scheduler, scheduleJob.getId());
            if (Objects.isNull(cronTrigger)) {
                ScheduleUtils.createScheduleJob(scheduler, scheduleJob);
            } else {
                ScheduleUtils.updateScheduleJob(scheduler, scheduleJob);
            }
        }
    }

    public Page<ScheduleJobEntity> query(ScheduleJobView scheduleJob) {
        int page = 0;
        int pageSize = 10;
        if (Objects.nonNull(scheduleJob.getPage())) {
            page = scheduleJob.getPage();
        }
        if (Objects.nonNull(scheduleJob.getPage())) {
            pageSize = scheduleJob.getPageSize();
        }
        ScheduleJobEntity entityExample = new ScheduleJobEntity();
        ExampleMatcher matcher = ExampleMatcher.matching();
        if (Objects.nonNull(scheduleJob.getBeanName())) {
            entityExample.setBeanName(scheduleJob.getBeanName());
            matcher = matcher.withMatcher("beanName", m -> m.contains());
        }
        if (Objects.nonNull(scheduleJob.getMethodName())) {
            entityExample.setMethodName(scheduleJob.getMethodName());
            matcher = matcher.withMatcher("methodName", m -> m.contains());
        }
        if (Objects.nonNull(scheduleJob.getStatus())) {
            entityExample.setStatus(scheduleJob.getStatus());
            matcher = matcher.withMatcher("status", m -> m.exact());
        }
        Example<ScheduleJobEntity> example = Example.of(entityExample, matcher);
        return jobDao.findAll(example, new PageRequest(page, pageSize, new Sort("id")));
    }

    public ScheduleJobEntity save(ScheduleJobEntity scheduleJob) {
        scheduleJob.setStatus(Constant.ScheduleStatus.NORMAL.getValue());
        scheduleJob = jobDao.save(scheduleJob);
        ScheduleUtils.createScheduleJob(scheduler, scheduleJob);
        return scheduleJob;
    }

    public ScheduleJobEntity update(ScheduleJobEntity scheduleJob) {
        ScheduleUtils.updateScheduleJob(scheduler, scheduleJob);
        scheduleJob = jobDao.save(scheduleJob);
        return scheduleJob;
    }

    public void deleteBatch(List<Long> ids) {
        for (Long id : ids) {
            ScheduleUtils.deleteScheduleJob(scheduler, id);
        }
        List<ScheduleJobEntity> jobs = jobDao.findAll(ids);
//        JpaUtils.dislink(jobs);
//        jobs = jobDao.save(jobs);
        jobDao.delete(jobs);
    }

    public List<ScheduleJobEntity> updateBatch(List<ScheduleJobEntity> scheduleJobs) {
        return jobDao.save(scheduleJobs);
    }

    @Transactional(readOnly = true)
    public void run(List<Long> ids) {
        List<ScheduleJobEntity> scheduleJobs = jobDao.findAll(ids);
        for(ScheduleJobEntity job : scheduleJobs){
            ScheduleUtils.run(scheduler, jobDao.findOne(job.getId()));
        }
    }

    public void pause(List<Long> ids) {
        List<ScheduleJobEntity> scheduleJobs = jobDao.findAll(ids);
        for (ScheduleJobEntity job : scheduleJobs) {
            job.setStatus(Constant.ScheduleStatus.PAUSE.getValue());
            ScheduleUtils.pauseJob(scheduler, job.getId());
        }
        updateBatch(scheduleJobs);
    }

    public void resume(List<Long> ids) {
        List<ScheduleJobEntity> scheduleJobs = jobDao.findAll(ids);
        for (ScheduleJobEntity job : scheduleJobs) {
            job.setStatus(Constant.ScheduleStatus.NORMAL.getValue());
            ScheduleUtils.resumeJob(scheduler, job.getId());
        }
        updateBatch(scheduleJobs);
    }
}
