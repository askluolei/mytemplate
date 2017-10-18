package com.luolei.template.common.jpa;

import java.lang.reflect.Field;
import java.util.Objects;

import javax.persistence.EntityManager;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

import com.luolei.template.common.utils.SpringContextUtils;

/**
 * jpa 回调
 *
 * @author 罗雷
 * @date 2017/10/18 0018 17:38
 */
public class JpaCallbackListener {

    /**
     * 新增记录后触发
     * @param entity
     */
    @PostPersist
    public void postPersist(BaseEntity entity) {
    }

    /**
     * 删除记录后触发
     * @param entity
     */
    @PostRemove
    public void postRemove(BaseEntity entity) {
    }

    /**
     * 更新记录后触发
     * @param entity
     */
    @PostUpdate
    public void postUpdate(BaseEntity entity) {
    }

    /**
     * 加载记录有触发
     * @param entity
     */
    @PostLoad
    public void postLoad(BaseEntity entity) {
    }

    /**
     * 新增记录前触发
     * @param entity
     */
    @PrePersist
    public void prePersist(BaseEntity entity) {
    }

    /**
     * 删除记录前触发
     * @param entity
     */
    @PreRemove
    public void preRemove(BaseEntity entity) {
    }

    /**
     * 更新记录前触发
     * 用来拦截逻辑删除的情况
     * @param entity
     */
    @PreUpdate
    public void preUpdate(BaseEntity entity) {
        System.out.println("--- preUpdate ---" + entity.getClass().getSimpleName());
        EntityManager entityManager = SpringContextUtils.getBean(EntityManager.class);
        if (entity.getDeleted()) {
            //软删除
            for (Field field : entity.getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(OneToMany.class)) {
                    //存在OneToMany在上面
                    field.setAccessible(true);
                    try {
                        Iterable<BaseEntity> entities = (Iterable<BaseEntity>) field.get(entity);
                        if (Objects.nonNull(entities)) {
                            for (BaseEntity en : entities) {
                                en.setDeleted(true);
                                entityManager.merge(en);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (field.isAnnotationPresent(OneToOne.class)) {

                }
            }
        }
    }
}
