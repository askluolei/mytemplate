package com.luolei.template.common.metrics;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer.Context;

/**
 * @author luolei
 * @email askluolei@gmail.com
 * @date 2017/10/15 20:10
 */
@Component
@Aspect
public class MetricsAspect {

    /**
     * 任意public 方法
     */
    @Pointcut("execution(public * *(..))")
    private void anyPublic() {
    }

    /**
     * 目标对象上有 @RestController 注解的类
     */
    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    private void withinAnnotationRestController() {
    }

    /**
     * 目标对象上有 @Controller 注解的类
     */
    @Pointcut("@within(org.springframework.stereotype.Controller)")
    private void withinAnnotationController() {
    }

    /**
     * 目标对象上有 @Service 注解的类
     */
    @Pointcut("@within(org.springframework.stereotype.Service)")
    private void withinAnnotationService() {
    }

    /**
     * 目标对象上有 @Repository 注解的类
     */
    @Pointcut("@within(org.springframework.stereotype.Repository)")
    private void withinAnnotationRepository() {
    }

    /**
     * 目标对象上有 @Component 注解的类
     */
    @Pointcut("@within(org.springframework.stereotype.Component)")
    private void withinAnnotationComponent() {
    }

    /**
     * 方法上有 @GetMapping 注解
     */
    @Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping)")
    private void annotationGetMappingMethod() {
    }

    /**
     * 方法上有 @PostMapping 注解
     */
    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)")
    private void annotationPostMappingMethod() {
    }

    /**
     * 方法上有 @PutMapping 注解
     */
    @Pointcut("@annotation(org.springframework.web.bind.annotation.PutMapping)")
    private void annotationPutMappingMethod() {
    }

    /**
     * 方法上有 @DeleteMapping 注解
     */
    @Pointcut("@annotation(org.springframework.web.bind.annotation.DeleteMapping)")
    private void annotationDeleteMappingMethod() {
    }

    /**
     * 方法上有 @PatchMapping 注解
     */
    @Pointcut("@annotation(org.springframework.web.bind.annotation.PatchMapping)")
    private void annotationPatchMappingMethod() {
    }

    @Autowired
    private MetricRegistry registry;

    /**
     * 环绕通知点
     * @RestController 的public 方法
     */
    @Around("withinAnnotationRestController() && anyPublic()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        String key = pjp.getSignature().toShortString();//使用短名，如果有同名类同名方法 统计信息就会有点问题
        Context time = registry.timer(key).time();
        Object retVal;
        try {
            retVal = pjp.proceed();
        } finally {
            time.close();
        }
        return retVal;
    }
}
