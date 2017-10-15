package com.luolei.template.modules.job.utils;

import com.luolei.template.common.exception.TException;
import com.luolei.template.common.utils.SpringContextUtils;
import com.xiaoleilu.hutool.util.StrUtil;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

/**
 * 执行定时任务
 *
 * @author luolei
 * @email askluolei@gmail.com
 * @date 2017/10/14 13:28
 */
public class ScheduleRunnable implements Runnable {

    private Object target;
    private Method method;
    private String params;

    public ScheduleRunnable(String beanName, String methodName, String params) throws NoSuchMethodException, SecurityException {
        this.target = SpringContextUtils.getBean(beanName);
        this.params = params;

        if (StrUtil.isNotBlank(params)) {
            this.method = target.getClass().getDeclaredMethod(methodName, String.class);
        } else {
            this.method = target.getClass().getDeclaredMethod(methodName);
        }
    }

    @Override
    public void run() {
        try {
            ReflectionUtils.makeAccessible(method);
            if (StrUtil.isNotBlank(params)) {
                method.invoke(target, params);
            } else {
                method.invoke(target);
            }
        } catch (Exception e) {
            //这个地方抛异常有用？
            throw new TException("执行定时任务失败", e);
        }
    }
}
