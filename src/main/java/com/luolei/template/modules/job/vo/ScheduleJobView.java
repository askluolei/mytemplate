package com.luolei.template.modules.job.vo;

import com.luolei.template.common.vo.BaseViewObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author luolei
 * @email askluolei@gmail.com
 * @date 2017/10/28 0:17
 */
@Getter
@Setter
@ToString
@ApiModel
public class ScheduleJobView extends BaseViewObject {

    @ApiModelProperty(value = "spring Bean名字", required = true, example = "testTask")
    private String beanName;

    @ApiModelProperty(value = "方法名", required = true, example = "test")
    private String methodName;

    @ApiModelProperty(value = "参数", example = "Hello World!")
    private String params;

    @ApiModelProperty(value = "cron 表达式", required = true, example = "0 */1 * * * ?")
    private String cronExpression;

    @ApiModelProperty(value = "任务状态", example = "0", allowableValues = "0, 1")
    private Integer status = 0;

    @ApiModelProperty(value = "备注", example = "测试定时任务")
    private String remark;
}
