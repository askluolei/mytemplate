package com.luolei.template.modules.sys.dto;

import com.luolei.template.common.validation.group.AddGroup;
import com.luolei.template.common.validation.group.LoginGroup;
import com.luolei.template.common.validation.group.UpdateGroup;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author luolei
 * @email askluolei@gmail.com
 * @date 2017/10/15 21:03
 */
@Getter
@Setter
public class UserParam {

    @NotNull(groups = {UpdateGroup.class})
    private Long id;

    @NotBlank(groups = {LoginGroup.class, UpdateGroup.class, AddGroup.class})
    private String username;

    @NotBlank(groups = {LoginGroup.class, UpdateGroup.class, AddGroup.class})
    private String password;

    private Boolean rememberMe = false;

    private String refreshToken;

    private String email;

    private String mobile;
    private Integer status;
    private LocalDateTime expireTime;
}
