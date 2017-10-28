package com.luolei.template.common.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 基本视图对象
 *
 * @author luolei
 * @email askluolei@gmail.com
 * @date 2017/10/28 0:17
 */
@Getter
@Setter
public class BaseViewObject {

    @ApiModelProperty(value = "主键", notes = "修改时候需要", example = "123")
    private Long id;

    @ApiModelProperty(hidden = true)
    private LocalDateTime createTime;

    @ApiModelProperty(hidden = true)
    private LocalDateTime lastOperateTime;

    @ApiModelProperty(hidden = true)
    private Integer page;

    @ApiModelProperty(hidden = true)
    private Integer pageSize;

    @ApiModelProperty(hidden = true)
    private Integer totalElement;

    @ApiModelProperty(hidden = true)
    private Integer totalPage;
}
