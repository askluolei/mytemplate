package com.luolei.template.modules.sys.controller;

import com.google.common.base.Preconditions;
import com.luolei.template.common.annotation.AccessToken;
import com.luolei.template.common.annotation.LoginUser;
import com.luolei.template.common.annotation.RequestIP;
import com.luolei.template.common.annotation.RequestPlatform;
import com.luolei.template.common.api.R;
import com.luolei.template.common.validation.group.LoginGroup;
import com.luolei.template.modules.sys.entity.UserEntity;
import com.luolei.template.modules.sys.entityenum.AuthType;
import com.luolei.template.modules.sys.service.UserService;
import com.luolei.template.modules.sys.vo.UserView;
import com.xiaoleilu.hutool.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 用户登录
 *
 * @author luolei
 * @email askluolei@gmail.com
 * @date 2017/10/14 23:35
 */
@Api(value = "/user", description = "认证相关")
@RestController
@RequestMapping(path = "/sys/login", produces = {"application/json; charset=UTF-8"})
public class LoginController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "登录", notes = "用户名密码登录")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "user", value = "用户认证信息", required = true, paramType = "body", dataType = "UserView"),
        @ApiImplicitParam(name = "platform", value = "客户端平台标识", required = true, paramType = "header", dataType = "String", example = "PC")
    })
    @PostMapping
    public R login(@RequestBody @Validated(LoginGroup.class) UserView user, @RequestPlatform String platform,@ApiIgnore @RequestIP String requestIP) {
        return R.ok(userService.login(user.getUsername(), user.getPassword(), AuthType.USER_PASS, platform, requestIP, user.getRememberMe()));
    }

    @ApiOperation(value = "踢人下线", notes = "用户名密码认证成功后可以选择踢出当前帐号的其他登录token")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "accessToken", value = "凭证", required = true, paramType = "header", dataType = "String"),
        @ApiImplicitParam(name = "platform", value = "客户端平台标识", required = true, paramType = "header", dataType = "String", example = "PC")
    })
    @PostMapping("/office")
    public R forceOffline(@AccessToken String accessToken, @RequestPlatform String platform, @ApiIgnore @LoginUser UserEntity user) {
        userService.forceOffline(accessToken, platform, user);
        return R.ok();
    }

    @ApiOperation(value = "refreshToken登录", notes = "登录用户，选择记住密码后会获取一个refreshToken，以后就可以使用这个凭证登录，注意的是这个凭证可以被踢出")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "user", value = "用户认证信息", required = true, paramType = "body", dataType = "UserView"),
        @ApiImplicitParam(name = "platform", value = "客户端平台标识", required = true, paramType = "header", dataType = "String", example = "PC")
    })
    @PostMapping("/token")
    public R loginByToken(@RequestBody UserView user, @RequestPlatform String platform, @RequestIP String requestIP) {
        Preconditions.checkArgument(StrUtil.isNotBlank(user.getRefreshToken()), "token 不能为空");
        return R.ok(userService.loginByToken(user.getRefreshToken(), AuthType.REFRESH_TOKEN, platform, requestIP));
    }

    @ApiOperation(value = "更新凭证", notes = "当accessToken快过期的时候，使用这个接口来更新凭证")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "accessToken", value = "凭证", required = true, paramType = "header", dataType = "String"),
        @ApiImplicitParam(name = "platform", value = "客户端平台标识", required = true, paramType = "header", dataType = "String", example = "PC")
    })
    @PostMapping("/refresh")
    public R refreshAccessToken(@AccessToken String accessToken, @RequestPlatform String platform, @RequestIP String requestIP) {
        return R.ok().with("token", userService.refreshAccessToken(accessToken, platform, requestIP));
    }

    @ApiOperation(value = "退出登录", notes = "退出登录")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "accessToken", value = "凭证", required = true, paramType = "header", dataType = "String"),
        @ApiImplicitParam(name = "platform", value = "客户端平台标识", required = true, paramType = "header", dataType = "String", example = "PC")
    })
    @PostMapping("/logout")
    public R logout(@AccessToken String accessToken, @RequestPlatform String platform, @LoginUser UserEntity user) {
        userService.logout(accessToken, platform, user);
        return R.ok();
    }

}
