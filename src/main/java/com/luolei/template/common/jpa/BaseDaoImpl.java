package com.luolei.template.common.jpa;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * 实现逻辑删除方法
 *
 * @author 罗雷
 * @date 2017/10/18 0018 17:35
 */
@Transactional
public class BaseDaoImpl<T extends BaseEntity> extends SimpleJpaRepository<T, Long> implements BaseDao<T> {

    private final EntityManager entityManager;
    private final JpaEntityInformation<T, Long> entityInformation;

    public BaseDaoImpl(JpaEntityInformation<T, Long> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        // Keep the EntityManager around to used from the newly introduced methods.
        this.entityManager = entityManager;
        this.entityInformation = entityInformation;
    }

    @Override
    public void softDeleteById(Long id) {
        T entity = findOne(id);
        if (entity == null) {
            throw new EmptyResultDataAccessException(
                    String.format("No %s entity with id %s exists!", entityInformation.getJavaType(), id), 1);
        }
        softDelete(entity);
    }

    @Override
    public void softDeleteById(Iterable<Long> ids) {
        List<T> entities = findAll(ids);
        entities.forEach(entity -> entity.setDeleted(true));
        softDelete(entities);
    }

    @Override
    public void softDelete(T entity) {
        Assert.notNull(entity, "The entity must not be null!");
        entity.setDeleted(true);
        save(entity);
        flush();
    }

    @Override
    public void softDelete(Iterable<T> entities) {
        Assert.notNull(entities, "The entity must not be null!");
        for (T entity : entities) {
            softDelete(entity);
        }
    }

    @Override
    public void softDeleteAll() {
        for (T entity : findAll()) {
            softDelete(entity);
        }
    }

}
