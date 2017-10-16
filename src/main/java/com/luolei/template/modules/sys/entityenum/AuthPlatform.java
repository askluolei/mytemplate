package com.luolei.template.modules.sys.entityenum;

/**
 * 认证平台
 *
 * @author luolei
 * @email askluolei@gmail.com
 * @date 2017/10/16 21:27
 */
public enum AuthPlatform {

    PC,
    ANDROID,
    IOS,
    WE_CHAT,
    UNKNOWN
    ;

    public static AuthPlatform from(String platform) {
        AuthPlatform result = UNKNOWN;
        for (AuthPlatform p : AuthPlatform.values()) {
            if (p.name().equalsIgnoreCase(platform)) {
                result = p;
                break;
            }
        }
        return result;
    }
}
