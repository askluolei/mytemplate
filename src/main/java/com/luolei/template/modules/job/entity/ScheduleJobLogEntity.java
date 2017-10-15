package com.luolei.template.modules.job.entity;

import com.luolei.template.common.jpa.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

/**
 * 定时执行日志
 *
 * @author luolei
 * @email askluolei@gmail.com
 * @date 2017/10/13 23:25
 */
@Entity
@Table(name = "t_schedule_job_log")
@Getter
@Setter
public class ScheduleJobLogEntity extends BaseEntity {

    /**
     * 任务状态    0：成功    1：失败
     */
    @Column(name = "tf_status")
    @ColumnDefault("0")
    private Integer status;

    /**
     * 失败信息
     */
    @Column(name = "tf_error", length = 2000)
    private String error;

    /**
     * 耗时(单位：毫秒)
     */
    @Column(name = "tf_times")
    private Long times;

    /**
     * 对应的定时任务
     */
    @ManyToOne
    @JoinColumn(name = "job_id")
    private ScheduleJobEntity job;
}
