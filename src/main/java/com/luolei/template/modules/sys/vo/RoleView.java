package com.luolei.template.modules.sys.vo;

import com.luolei.template.common.vo.BaseViewObject;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author luolei
 * @email askluolei@gmail.com
 * @date 2017/10/28 17:52
 */
@Getter
@Setter
public class RoleView extends BaseViewObject {

    private String roleName;
    private String remark;

    private List<PermissionView> permissions;
}
