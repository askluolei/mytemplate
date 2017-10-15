package com.luolei.template.modules.job.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author luolei
 * @email askluolei@gmail.com
 * @date 2017/10/14 14:16
 */
@Component
public class TestTask {

    private Logger logger = LoggerFactory.getLogger(getClass());

    public void test(String params) {
        logger.info("我是带参数的test方法，正在被执行，参数为:{}", params);
    }

    public void test2() {
        logger.info("我是不带参数的test2方法，正在被执行");
    }
}
