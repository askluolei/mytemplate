package com.luolei.template.common.utils;

import com.luolei.template.common.jpa.BaseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Objects;

/**
 * @author luolei
 * @email askluolei@gmail.com
 * @date 2017/10/28 12:19
 */
public class JpaUtils {

    private static Logger logger = LoggerFactory.getLogger(JpaUtils.class);

    public static void dislink(Iterable<? extends BaseEntity> entities) {
        if (Objects.nonNull(entities)) {
            for (BaseEntity entity : entities) {
                dislink(entity);
            }
        }
    }

    /**
     * 解除外键关联
     * @param entity
     */
    public static void dislink(BaseEntity entity) {
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
                            fieldVals.clear();
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
}
