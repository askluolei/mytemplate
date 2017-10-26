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
import java.time.LocalDateTime;

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
     * 上传操作时间
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
}
