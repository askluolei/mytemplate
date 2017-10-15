package com.luolei.template.common.jpa;

import com.luolei.template.common.utils.Sequence;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.AbstractUUIDGenerator;

import java.io.Serializable;

/**
 * @author luolei
 * @email askluolei@gmail.com
 * @date 2017/10/13 0:20
 */
public class IDGenerator extends AbstractUUIDGenerator {

    private Sequence sequence = new Sequence(1, 1);

    @Override
    public Serializable generate(SessionImplementor sessionImplementor, Object o) throws HibernateException {
        return sequence.nextId();
    }
}
