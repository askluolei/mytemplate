package com.luolei.template.common.utils;

/**
 * @author luolei
 * @email askluolei@gmail.com
 * @date 2017/10/14 13:24
 */
public class Constant {

    /**
     * token 的默认有效时间  单位秒
     */
    public static final long TOKEN_EXPIRE_TIME = 30 * 60;

    public static final String TOKEN_HEADER_KEY = "token";

    public enum ScheduleStatus {
        /**
         * 正常
         */
        NORMAL(0),
        /**
         * 暂停
         */
        PAUSE(1);

        private int value;

        ScheduleStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
