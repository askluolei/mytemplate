package com.luolei.template.modules.job.controller;

import com.luolei.template.common.api.R;
import com.luolei.template.modules.job.dao.ScheduleJobDao;
import com.luolei.template.modules.job.entity.ScheduleJobEntity;
import com.luolei.template.modules.job.service.ScheduleJobService;
import com.luolei.template.modules.job.vo.ScheduleJobView;
import io.swagger.annotations.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * 定时任务
 *
 * @author luolei
 * @email askluolei@gmail.com
 * @date 2017/10/14 14:19
 */
@Api(value = "/schedulejob", description = "定时任务")
@RestController
@RequestMapping(path = "/sys/schedule", produces = {"application/json; charset=UTF-8"})
public class ScheduleJobController {

    @Autowired
    private ScheduleJobDao jobDao;

    @Autowired
    private ScheduleJobService jobService;

    private ScheduleJobEntity convert(ScheduleJobView jobView) {
        ScheduleJobEntity jobEntity = new ScheduleJobEntity();
        BeanUtils.copyProperties(jobView, jobEntity);
        return jobEntity;
    }

    @ApiOperation(value = "定时任务列表", notes = "获取定时任务列表", nickname = "定时任务列表")
    @GetMapping
    public R list(ScheduleJobView jobView, @PathVariable(name = "jobId", required = false) Long id) {
        return R.ok().with("jobs", jobService.query(jobView));
    }

    @ApiOperation(value = "定时任务信息", notes = "根据主键获取定时任务信息")
    @GetMapping("/{jobId}")
    public R info(@PathVariable("jobId") Long id) {
        return R.ok().with("job", jobDao.findOne(id));
    }

    @ApiOperation(value = "添加定时任务", notes = "新增一个定时任务，并立即启动")
    @PostMapping
    public R save(@RequestBody ScheduleJobView jobView) {
        return R.ok().with("job", jobService.save(convert(jobView)));
    }

    @ApiOperation(value = "修改定时任务", notes = "根据主键修改定时任务")
    @PutMapping
    public R update(@RequestBody ScheduleJobView jobView) {
        return R.ok().with("job", jobService.update(convert(jobView)));
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
