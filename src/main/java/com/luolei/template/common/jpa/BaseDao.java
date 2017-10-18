package com.luolei.template.common.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * 扩展 JpaResository
 * 添加逻辑删除方法
 * 使用逻辑删除，关联关系，都要配置成双向的
 * 逻辑删除只是将deleted字段设置为true了，还是会查询出来
 * 如果不行查询出来，那么在每个实体类上添加注解 @Where(clause = "deleted = false")
 *
 * 配置双向关联的时候要注意，在不维持外键的那一方要配置 mapperBy 内容为维持外键实体类，本类型引用字段名
 *
 * @author 罗雷
 * @date 2017/10/18 0018 17:35
 */
@NoRepositoryBean
public interface BaseDao<T> extends JpaRepository<T, Long>, JpaSpecificationExecutor<T> {

    void softDeleteById(Long id);

    void softDeleteById(Iterable<Long> ids);

    void softDelete(T entity);

    void softDelete(Iterable<T> entities);

    void softDeleteAll();

}
