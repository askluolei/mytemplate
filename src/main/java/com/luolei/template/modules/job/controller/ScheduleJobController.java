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
import springfox.documentation.annotations.ApiIgnore;

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
    @ApiImplicitParams({
        @ApiImplicitParam(name = "beanName", value = "bean名字", dataType = "String", paramType = "queryPermission", example = "test"),
        @ApiImplicitParam(name = "methodName", value = "方法名", dataType = "String", paramType = "queryPermission", example = "test"),
        @ApiImplicitParam(name = "status", value = "状态", dataType = "int", paramType = "queryPermission", defaultValue = "0", example = "0", allowableValues = "0, 1")
    })
    @GetMapping
    public R list(@ApiIgnore ScheduleJobView jobView) {
        return R.ok().with("jobs", jobService.query(jobView));
    }

    @ApiOperation(value = "定时任务信息", notes = "根据主键获取定时任务信息")
    @GetMapping("/{jobId}")
    public R info(@PathVariable("jobId") Long id) {
        return R.ok().with("job", jobDao.findOne(id));
    }

    @ApiOperation(value = "添加定时任务", notes = "新增一个定时任务，并立即启动")
    @PostMapping
    public R save(@RequestBody ScheduleJobView job) {
        return R.ok().with("job", jobService.save(convert(job)));
    }

    @ApiOperation(value = "修改定时任务", notes = "根据主键修改定时任务")
    @PutMapping
    public R update(@RequestBody ScheduleJobView job) {
        return R.ok().with("job", jobService.update(convert(job)));
    }

    @ApiOperation(value = "删除定时任务", notes = "根据主键删除定时任务")
    @ApiImplicitParam(name = "jobIds", value = "定时任务主键数组", required = true, dataType = "List<Long>")
    @DeleteMapping
    public R delete(@RequestBody List<Long> jobIds) {
        jobService.deleteBatch(jobIds);
        return R.ok();
    }

    @ApiOperation(value = "执行定时任务", notes = "根据主键执行定时任务")
    @ApiImplicitParam(name = "jobIds", value = "定时任务主键数组", required = true, dataType = "List<Long>")
    @PutMapping("/run")
    public R run(@RequestBody List<Long> jobIds) {
        jobService.run(jobIds);
        return R.ok();
    }

    @ApiOperation(value = "暂停定时任务", notes = "根据主键暂停定时任务")
    @ApiImplicitParam(name = "jobIds", value = "定时任务主键数组", required = true, dataType = "List<Long>")
    @PutMapping("/pause")
    public R pause(@RequestBody List<Long> jobIds) {
        jobService.pause(jobIds);
        return R.ok();
    }

    @ApiOperation(value = "恢复定时任务", notes = "根据主键恢复定时任务")
    @ApiImplicitParam(name = "jobIds", value = "定时任务主键数组", required = true, dataType = "List<Long>")
    @PutMapping("/resume")
    public R resume(@RequestBody List<Long> jobIds) {
        jobService.resume(jobIds);
        return R.ok();
    }

}
