package com.luolei.template.common.jpa;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import javax.persistence.*;

import com.luolei.template.common.utils.JpaUtils;
import com.luolei.template.common.utils.SpringContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * jpa 回调
 *
 * @author 罗雷
 * @date 2017/10/18 0018 17:38
 */
public class JpaCallbackListener {

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 新增记录后触发
     * @param entity
     */
    @PostPersist
    public void postPersist(BaseEntity entity) {
        logger.debug("**************** --postPermist-- ****************");
    }

    /**
     * 删除记录后触发
     * @param entity
     */
    @PostRemove
    public void postRemove(BaseEntity entity) {
        logger.debug("**************** --postRemove-- ****************");
    }

    /**
     * 更新记录后触发
     * @param entity
     */
    @PostUpdate
    public void postUpdate(BaseEntity entity) {
        logger.debug("**************** --postUpdate-- ****************");
    }

    /**
     * 加载记录有触发
     * @param entity
     */
    @PostLoad
    public void postLoad(BaseEntity entity) {
        logger.debug("**************** --postLoad-- ****************");
    }

    /**
     * 新增记录前触发
     * @param entity
     */
    @PrePersist
    public void prePersist(BaseEntity entity) {
        logger.debug("**************** --prePersist-- ****************");
    }

    /**
     * 删除记录前触发
     * @param entity
     */
    @PreRemove
    public void preRemove(BaseEntity entity) {
        logger.debug("**************** --preRemove-- ****************");
        JpaUtils.dislink(entity);
    }

    /**
     * 更新记录前触发
     * 用来拦截逻辑删除的情况
     * @param entity
     */
    @PreUpdate
    public void preUpdate(BaseEntity entity) {
        logger.debug("**************** --preUpdate-- ****************");
        if (entity.getDeleted()) {
            //逻辑删除，断开外键
            JpaUtils.dislink(entity);
        }
    }
}
