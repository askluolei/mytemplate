package com.luolei.template.common.jpa;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;

/**
 * @author luolei
 * @email askluolei@gmail.com
 * @date 2017/10/13 0:14
 */
@Getter
@Setter
@EqualsAndHashCode
@MappedSuperclass
@EntityListeners({AuditingEntityListener.class,JpaCallbackListener.class})
public class BaseEntity implements Serializable{

    /**
     * ID 主键
     */
    @Id
    @Column(name = "id", length = 40)
    @GeneratedValue(generator = "gid")
    @GenericGenerator(name = "gid", strategy = "com.luolei.template.common.jpa.IDGenerator")
    private Long id;

    /**
     * 如果要用 @Version 注解 ，插入的时候必须要有个值，这个可以设置为 not null 并加上一个默认值，这样以后更新就会顺带更新这个值
     * 如果刚插入的时候为null，后面会出问题的 save方法 如果是更新的话，会出现异常
     */
    @Version
    @Column(name = "version", nullable=false)
    private Integer version = 1;

    /**
     * 使用 @CreatedDate @LastModifiedDate 注解 记得要在配置类上使用 @EnableJpaAuditing 开启这个功能
     *
     */
    @Column(name = "create_time", nullable = false)
    @CreatedDate
    private LocalDateTime createTime;

    /**
     * 上次操作时间
     */
    @Column(name = "last_operate_time", nullable = false)
    @LastModifiedDate
    private LocalDateTime lastOperateTime;

    /**
     * 删除标记，支持逻辑删除
     * 使用逻辑删除，关联关系，都要配置成双向的
     */
    @Column(name = "deleted")
    private Boolean deleted = false;

//    @PreRemove
//    public void preRemove(BaseEntity entity) {
//        dislink(entity);
//    }
//
//    @PreUpdate
//    public void preUpdate(BaseEntity entity) {
//        if (entity.getDeleted()) {
//            //逻辑删除，断开外键
//            dislink(entity);
//        }
//    }
//
//    /**
//     * 解除外键关联
//     * @param entity
//     */
//    private void dislink(BaseEntity entity) {
//        for (Field field : entity.getClass().getDeclaredFields()) {
//            try {
//                if (field.isAnnotationPresent(OneToOne.class)) {
//                    OneToOne oneToOne = field.getAnnotation(OneToOne.class);
//                    String mapperBy = oneToOne.mappedBy();
//                    if (!mapperBy.isEmpty()) {
//                        //存在　mapperBy，则代表外键在field这个实体类中
//                        //肯定不为null　否则　hibernate　启动不了
//                        Field mapperField = field.getType().getDeclaredField(mapperBy);
//                        //权限
//                        mapperField.setAccessible(true);
//                        field.setAccessible(true);
//                        Object fieldVal = field.get(entity);
//                        if (Objects.nonNull(fieldVal)) {
//                            //断开外键
//                            mapperField.set(fieldVal, null);
//                        }
//                    }
//                } else if (field.isAnnotationPresent(OneToMany.class)) {
//                    OneToMany oneToMany = field.getAnnotation(OneToMany.class);
//                    String mapperBy = oneToMany.mappedBy();
//                    if (!mapperBy.isEmpty()) {
//                        field.setAccessible(true);
//                        Collection<?> fieldVals = (Collection<?>) field.get(entity);
//                        if (Objects.nonNull(fieldVals)) {
//                            for (Object fieldVal : fieldVals) {
//                                Field mapperField = fieldVal.getClass().getDeclaredField(mapperBy);
//                                mapperField.setAccessible(true);
//                                mapperField.set(fieldVal, null);
//                            }
//                        }
//                    }
//                } else if (field.isAnnotationPresent(ManyToMany.class)) {
//                    ManyToMany manyToMany = field.getAnnotation(ManyToMany.class);
//                    String mapperBy = manyToMany.mappedBy();
//                    if (!mapperBy.isEmpty()) {
//                        field.setAccessible(true);
//                        Collection<?> fieldVals = (Collection<?>) field.get(entity);
//                        if (Objects.nonNull(fieldVals)) {
//                            for (Object fieldVal: fieldVals) {
//                                Field mapperField = fieldVal.getClass().getDeclaredField(mapperBy);
//                                mapperField.setAccessible(true);
//                                Collection<?> toUpdates = (Collection<?>) mapperField.get(fieldVal);
//                                if (Objects.nonNull(toUpdates)) {
//                                    toUpdates.remove(entity);
//                                }
//                            }
//                        }
//                    }
//                }
//            } catch (Exception e) {
//            }
//        }
//    }
}
