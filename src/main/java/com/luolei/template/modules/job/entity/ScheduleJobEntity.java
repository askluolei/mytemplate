package com.luolei.template.modules.job.entity;

import com.luolei.template.common.jpa.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 定时器
 *
 * @author luolei
 * @email askluolei@gmail.com
 * @date 2017/10/13 23:20
 */
@Entity
@Table(name = "t_schedule_job")
@Getter
@Setter
public class ScheduleJobEntity extends BaseEntity {

    private static final long serialVersionUID = 2956280592023358352L;

    /**
     * 任务调度参数key
     */
    public static final String JOB_PARAM_KEY = "JOB_PARAM_KEY";

    /**
     * spring bean名称
     */
    @Column(name = "tf_bean_name", length = 64, nullable = false)
    private String beanName;

    /**
     * 方法名
     */
    @Column(name = "tf_method_name", length = 64, nullable = false)
    private String methodName;

    /**
     * 参数
     */
    @Column(name = "tf_params")
    private String params;

    /**
     * cron表达式
     */
    @Column(name = "tf_cron_expression", length = 16)
    private String cronExpression;

    /**
     * 任务状态  0：正常  1：暂停
     */
    @Column(name = "tf_status")
    @ColumnDefault("0")
    private Integer status;

    /**
     * 备注
     */
    @Column(name = "remark")
    private String remark;
}