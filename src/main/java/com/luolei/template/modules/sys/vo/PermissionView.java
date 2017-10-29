package com.luolei.template.modules.sys.vo;

import com.luolei.template.common.vo.BaseViewObject;
import lombok.Getter;
import lombok.Setter;

/**
 * @author luolei
 * @email askluolei@gmail.com
 * @date 2017/10/28 17:52
 */
@Getter
@Setter
public class PermissionView extends BaseViewObject {

    private String permission;
    private String remark;
}
