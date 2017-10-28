package com.luolei.template.datasources;

import com.luolei.template.common.jpa.BaseDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewInterceptor;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.Map;

/**
 * JPA 相关的类 分别配置
 * @author luolei
 * @email askluolei@gmail.com
 * @date 2017/10/12 23:47
 */
@Profile("dev")
@Configuration
@EnableJpaAuditing
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "entityManagerFactoryPrimary",
        transactionManagerRef = "transactionManagerPrimary",
        basePackages = {"com.luolei.template.modules.sys.dao"}, repositoryBaseClass = BaseDaoImpl.class)//设置dao（repo）所在位置
public class JpaDynamicDataSourceFirst extends WebMvcConfigurerAdapter{

    @Autowired
    private JpaProperties jpaProperties;

    @Autowired
    private EntityManagerFactoryBuilder builder;

    @Autowired
    @Qualifier(DataSourceNames.FIRST)
    private DataSource primaryDS;


    @Bean(name = "entityManagerPrimary")
    @Primary
    public EntityManager entityManager() {
        return entityManagerFactoryPrimary().getObject().createEntityManager();
    }

    @Bean(name = "entityManagerFactoryPrimary")
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryPrimary() {
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
    public PlatformTransactionManager transactionManagerPrimary(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(entityManagerFactoryPrimary().getObject());
    }

    @Bean
    public OpenEntityManagerInViewInterceptor openEntityManagerInViewInterceptorPrimary() {
        OpenEntityManagerInViewInterceptor openEntityManagerInViewInterceptor = new OpenEntityManagerInViewInterceptor();
        openEntityManagerInViewInterceptor.setEntityManagerFactory(entityManagerFactoryPrimary().getObject());
        return openEntityManagerInViewInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addWebRequestInterceptor(openEntityManagerInViewInterceptorPrimary());
    }

}
