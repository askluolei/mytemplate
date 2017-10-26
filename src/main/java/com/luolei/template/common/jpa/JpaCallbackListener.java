package com.luolei.template.common.jpa;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Objects;

import javax.persistence.*;

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
     * 解除外键关联
     * @param entity
     */
    private void dislink(BaseEntity entity) {
        for (Field field : entity.getClass().getDeclaredFields()) {
            try {
                if (field.isAnnotationPresent(OneToOne.class)) {
                    OneToOne oneToOne = field.getAnnotation(OneToOne.class);
                    String mapperBy = oneToOne.mappedBy();
                    if (!mapperBy.isEmpty()) {
                        //存在　mapperBy，则代表外键在field这个实体类中
                        //肯定不为null　否则　hibernate　启动不了
                        Field mapperField = field.getType().getDeclaredField(mapperBy);
                        //权限
                        mapperField.setAccessible(true);
                        field.setAccessible(true);
                        Object fieldVal = field.get(entity);
                        if (Objects.nonNull(fieldVal)) {
                            //断开外键
                            mapperField.set(fieldVal, null);
                        }
                    }
                } else if (field.isAnnotationPresent(OneToMany.class)) {
                    OneToMany oneToMany = field.getAnnotation(OneToMany.class);
                    String mapperBy = oneToMany.mappedBy();
                    if (!mapperBy.isEmpty()) {
                        field.setAccessible(true);
                        Collection<?> fieldVals = (Collection<?>) field.get(entity);
                        if (Objects.nonNull(fieldVals)) {
                            for (Object fieldVal : fieldVals) {
                                Field mapperField = fieldVal.getClass().getDeclaredField(mapperBy);
                                mapperField.setAccessible(true);
                                mapperField.set(fieldVal, null);
                            }
                        }
                    }
                } else if (field.isAnnotationPresent(ManyToMany.class)) {
                    ManyToMany manyToMany = field.getAnnotation(ManyToMany.class);
                    String mapperBy = manyToMany.mappedBy();
                    if (!mapperBy.isEmpty()) {
                        field.setAccessible(true);
                        Collection<?> fieldVals = (Collection<?>) field.get(entity);
                        if (Objects.nonNull(fieldVals)) {
                            for (Object fieldVal: fieldVals) {
                                Field mapperField = fieldVal.getClass().getDeclaredField(mapperBy);
                                mapperField.setAccessible(true);
                                Collection<?> toUpdates = (Collection<?>) mapperField.get(fieldVal);
                                if (Objects.nonNull(toUpdates)) {
                                    toUpdates.remove(entity);
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                logger.error("删除前的处理异常", e);
            }
        }
    }

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
        dislink(entity);
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
            dislink(entity);
        }
    }
}
