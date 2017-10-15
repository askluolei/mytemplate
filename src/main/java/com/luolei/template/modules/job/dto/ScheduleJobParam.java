package com.luolei.template.modules.job.dto;

import com.luolei.template.common.dto.PageQuery;
import lombok.Getter;
import lombok.Setter;

/**
 * @author luolei
 * @email askluolei@gmail.com
 * @date 2017/10/14 14:25
 */
@Getter
@Setter
public class ScheduleJobParam extends PageQuery {
    private String beanName;
    private String methodName;
    private Integer status;
}
