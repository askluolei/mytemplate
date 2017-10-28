package com.luolei.template.datasources;

import com.luolei.template.common.jpa.BaseDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Map;
import java.util.Properties;

/**
 * @author luolei
 * @email askluolei@gmail.com
 * @date 2017/10/12 23:47
 */
@Profile("dev")
@Configuration
@EnableJpaAuditing
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "entityManagerFactorySecond",
        transactionManagerRef = "transactionManagerSecond",
        basePackages = {"com.luolei.template.modules.job.dao"}, repositoryBaseClass = BaseDaoImpl.class)//设置dao（repo）所在位置
public class JpaDynamicDataSourceSecond extends WebMvcConfigurerAdapter {

    @Autowired
    private JpaProperties jpaProperties;

    @Autowired
    EntityManagerFactoryBuilder builder;

    @Autowired
    @Qualifier(DataSourceNames.SECOND)
    private DataSource secondDS;

    @Bean(name = "entityManagerSecond")
    public EntityManager entityManager() {
        return entityManagerFactorySecond().getObject().createEntityManager();
    }

    @Bean(name = "entityManagerFactorySecond")
    public LocalContainerEntityManagerFactoryBean entityManagerFactorySecond() {
        return builder
                .dataSource(secondDS)
                .properties(getVendorProperties(secondDS))
                .packages("com.luolei.template.modules.job.entity") //设置实体类所在位置
                .persistenceUnit("primaryPersistenceUnit")
                .build();
    }

    private Map<String, String> getVendorProperties(DataSource dataSource) {
        return jpaProperties.getHibernateProperties(dataSource);
    }

    @Bean(name = "transactionManagerSecond")
    PlatformTransactionManager transactionManagerSecond() {
        return new JpaTransactionManager(entityManagerFactorySecond().getObject());
    }

    @Bean
    public OpenEntityManagerInViewInterceptor openEntityManagerInViewInterceptorSecond() {
        OpenEntityManagerInViewInterceptor openEntityManagerInViewInterceptor = new OpenEntityManagerInViewInterceptor();
        openEntityManagerInViewInterceptor.setEntityManagerFactory(entityManagerFactorySecond().getObject());
        return openEntityManagerInViewInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addWebRequestInterceptor(openEntityManagerInViewInterceptorSecond());
    }
}
