package com.luolei.template.datasources;

import com.luolei.template.common.jpa.BaseDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.Map;

/**
 * JPA 相关的类 分别配置
 * @author luolei
 * @email askluolei@gmail.com
 * @date 2017/10/12 23:47
 */
@Configuration
@EnableJpaAuditing
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "entityManagerFactoryPrimary",
        transactionManagerRef = "transactionManagerPrimary",
        basePackages = {"com.luolei.template.modules.sys.dao"}, repositoryBaseClass = BaseDaoImpl.class)//设置dao（repo）所在位置
public class JpaDynamicDataSourceFirst {

    @Autowired
    private JpaProperties jpaProperties;

    @Autowired
    @Qualifier(DataSourceNames.FIRST)
    private DataSource primaryDS;

    @Bean(name = "entityManagerPrimary")
    @Primary
    public EntityManager entityManager(EntityManagerFactoryBuilder builder) {
        return entityManagerFactoryPrimary(builder).getObject().createEntityManager();
    }

    @Bean(name = "entityManagerFactoryPrimary")
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryPrimary(EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(primaryDS)
                .properties(getVendorProperties(primaryDS))
                .packages("com.luolei.template.modules.sys.entity") //设置实体类所在位置
                .persistenceUnit("primaryPersistenceUnit")
                .build();
    }

    private Map<String, String> getVendorProperties(DataSource dataSource) {
        return jpaProperties.getHibernateProperties(dataSource);
    }

    @Bean(name = "transactionManagerPrimary")
    @Primary
    PlatformTransactionManager transactionManagerPrimary(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(entityManagerFactoryPrimary(builder).getObject());
    }
}
