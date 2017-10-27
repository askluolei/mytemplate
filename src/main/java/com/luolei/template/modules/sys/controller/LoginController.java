package com.luolei.template.modules.sys.controller;

import com.google.common.base.Preconditions;
import com.luolei.template.common.annotation.AccessToken;
import com.luolei.template.common.annotation.LoginUser;
import com.luolei.template.common.annotation.RequestIP;
import com.luolei.template.common.annotation.RequestPlatform;
import com.luolei.template.common.api.R;
import com.luolei.template.common.validation.group.LoginGroup;
import com.luolei.template.modules.sys.dto.UserParam;
import com.luolei.template.modules.sys.entity.UserEntity;
import com.luolei.template.modules.sys.entityenum.AuthType;
import com.luolei.template.modules.sys.service.UserService;
import com.xiaoleilu.hutool.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@RequestMapping("/sys/login")
public class LoginController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "用户名密码登录", notes = "使用用户名密码登录，这种方式")
    @PostMapping
    public R login(@RequestBody @Validated(LoginGroup.class) UserParam user, @RequestPlatform String platform, @RequestIP String requestIP) {
        return R.ok(userService.login(user.getUsername(), user.getPassword(), AuthType.USER_PASS, platform, requestIP, user.getRememberMe()));
    }

    @PostMapping("/office")
    public R forceOffline(@AccessToken String accessToken, @RequestPlatform String platform, @LoginUser UserEntity user) {
        userService.forceOffline(accessToken, platform, user);
        return R.ok();
    }

    @PostMapping("/token")
    public R loginByToken(@RequestBody UserParam user, @RequestPlatform String platform, @RequestIP String requestIP) {
        Preconditions.checkArgument(StrUtil.isNotBlank(user.getRefreshToken()), "token 不能为空");
        return R.ok(userService.loginByToken(user.getRefreshToken(), AuthType.REFRESH_TOKEN, platform, requestIP));
    }

    @PostMapping("/refresh")
    public R refreshAccessToken(@AccessToken String accessToken, @RequestPlatform String platform, @RequestIP String requestIP) {
        return R.ok(userService.refreshAccessToken(accessToken, platform, requestIP));
    }

    @PostMapping("/logout")
    public R logout(@AccessToken String accessToken, @RequestPlatform String platform, @LoginUser UserEntity user) {
        userService.logout(accessToken, platform, user);
        return R.ok();
    }

}
