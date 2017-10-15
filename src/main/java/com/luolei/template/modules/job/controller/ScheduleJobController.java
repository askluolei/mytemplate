package com.luolei.template.modules.job.controller;

import com.luolei.template.common.api.R;
import com.luolei.template.modules.job.dao.ScheduleJobDao;
import com.luolei.template.modules.job.dto.ScheduleJobParam;
import com.luolei.template.modules.job.entity.ScheduleJobEntity;
import com.luolei.template.modules.job.service.ScheduleJobService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 定时任务
 *
 * @author luolei
 * @email askluolei@gmail.com
 * @date 2017/10/14 14:19
 */
@Api(tags = {"定时任务"})
@RestControllerAdvice
@RequestMapping(path = "/sys/schedule")
public class ScheduleJobController {

    @Autowired
    private ScheduleJobDao jobDao;

    @Autowired
    private ScheduleJobService jobService;

    @ApiOperation(value = "定时任务列表", notes = "获取定时任务列表")
    @GetMapping("/list")
    public R list(ScheduleJobParam scheduleJob) {
        return R.ok(jobService.query(scheduleJob));
    }

    @ApiOperation(value = "定时任务信息", notes = "根据主键获取定时任务信息")
    @GetMapping("/{jobId}")
    public R info(@PathVariable("jobId") Long id) {
        return R.ok(jobDao.findOne(id));
    }

    @ApiOperation(value = "添加定时任务", notes = "新增一个定时任务，并立即启动")
    @ApiImplicitParam(name = "scheduleJob", value = "定时任务实体", required = true)
    @PostMapping
    public R save(@RequestBody  ScheduleJobEntity scheduleJob) {
        return R.ok(jobService.save(scheduleJob));
    }

    @ApiOperation(value = "修改定时任务", notes = "根据主键修改定时任务")
    @ApiImplicitParam(name = "scheduleJob", value = "定时任务实体", required = true)
    @PutMapping
    public R update(@RequestBody  ScheduleJobEntity scheduleJob) {
        return R.ok(jobService.update(scheduleJob));
    }

    @ApiOperation(value = "删除定时任务", notes = "根据主键删除定时任务")
    @ApiImplicitParam(name = "jobs", value = "定时任务主键数组", required = true)
    @DeleteMapping
    public R delete(@RequestBody List<Long> jobs) {
        jobService.deleteBatch(jobs);
        return R.ok();
    }

    @ApiOperation(value = "执行定时任务", notes = "根据主键执行定时任务")
    @ApiImplicitParam(name = "jobs", value = "定时任务主键数组", required = true)
    @PutMapping("/run")
    public R run(@RequestBody List<Long> jobs) {
        jobService.run(jobs);
        return R.ok();
    }

    @ApiOperation(value = "暂停定时任务", notes = "根据主键暂停定时任务")
    @ApiImplicitParam(name = "jobs", value = "定时任务主键数组", required = true)
    @PutMapping("/pause")
    public R pause(@RequestBody List<Long> jobs) {
        jobService.pause(jobs);
        return R.ok();
    }

    @ApiOperation(value = "恢复定时任务", notes = "根据主键恢复定时任务")
    @ApiImplicitParam(name = "jobs", value = "定时任务主键数组", required = true)
    @PutMapping("/resume")
    public R resume(@RequestBody List<Long> jobs) {
        jobService.resume(jobs);
        return R.ok();
    }

}
