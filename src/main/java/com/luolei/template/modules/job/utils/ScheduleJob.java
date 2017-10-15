package com.luolei.template.modules.job.utils;

import com.alibaba.fastjson.JSON;
import com.luolei.template.common.utils.SpringContextUtils;
import com.luolei.template.modules.job.dao.ScheduleJobLogDao;
import com.luolei.template.modules.job.entity.ScheduleJobEntity;
import com.luolei.template.modules.job.entity.ScheduleJobLogEntity;
import com.xiaoleilu.hutool.util.StrUtil;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 定时任务
 *
 * @author luolei
 * @email askluolei@gmail.com
 * @date 2017/10/14 13:06
 */
public class ScheduleJob extends QuartzJobBean {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private ExecutorService service = Executors.newSingleThreadExecutor();

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        ScheduleJobEntity scheduleJob = JSON.parseObject(context.getMergedJobDataMap().getString(ScheduleJobEntity.JOB_PARAM_KEY), ScheduleJobEntity.class);
        ScheduleJobLogEntity log = new ScheduleJobLogEntity();
        log.setJob(scheduleJob);

        ScheduleJobLogDao logDao = SpringContextUtils.getBean(ScheduleJobLogDao.class);
        long startTime = System.currentTimeMillis();
        try {
            logger.info("任务准备执行，任务ID：{}", scheduleJob.getId());
            ScheduleRunnable task = new ScheduleRunnable(scheduleJob.getBeanName(), scheduleJob.getMethodName(), scheduleJob.getParams());
            Future<?> future = service.submit(task);
            future.get();
            long times = System.currentTimeMillis() - startTime;
            log.setTimes(times);
            log.setStatus(0);
            logger.info("任务执行完毕，任务ID：{} 总共耗时：{} 毫秒", scheduleJob.getId(), times);
        } catch (Exception e) {
            logger.error("任务执行失败，任务ID：" + scheduleJob.getId(), e);
            //任务执行总时长
            long times = System.currentTimeMillis() - startTime;
            log.setTimes(times);

            //任务状态    0：成功    1：失败
            log.setStatus(1);
            log.setError(StrUtil.sub(e.toString(), 0, 2000));
        } finally {
            logDao.save(log);
        }
    }
}
