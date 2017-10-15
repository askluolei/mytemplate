package com.luolei.template.modules.sys.controller;

import com.luolei.template.common.annotation.AccessToken;
import com.luolei.template.common.annotation.LoginUser;
import com.luolei.template.common.api.R;
import com.luolei.template.common.validation.group.LoginGroup;
import com.luolei.template.modules.sys.dto.UserParam;
import com.luolei.template.modules.sys.entity.UserEntity;
import com.luolei.template.modules.sys.service.UserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用户登录
 *
 * @author luolei
 * @email askluolei@gmail.com
 * @date 2017/10/14 23:35
 */
@Api
@RestController
@RequestMapping("/sys")
public class LoginController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public R login(@RequestBody @Validated(LoginGroup.class) UserParam user) {
        return R.ok(userService.login(user.getUsername(), user.getPassword()));
    }

    @PostMapping("/logout")
    public R logout(@AccessToken String accessToken) {
        userService.logout(accessToken);
        return R.ok();
    }

}
