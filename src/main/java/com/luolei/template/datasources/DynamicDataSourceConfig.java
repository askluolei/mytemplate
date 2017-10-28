package com.luolei.template.datasources;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

/**
 * 配置多数据源
 *
 * @author luolei
 * @email askluolei@gmail.com
 * @date 2017/10/12 23:27
 */
@Profile("dev")
@Configuration
public class DynamicDataSourceConfig {

    @Bean
    @ConfigurationProperties("spring.datasource.druid.first")
    @Primary
    @Qualifier(DataSourceNames.FIRST)
    public DataSource firstDataSource(){
        return DruidDataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties("spring.datasource.druid.second")
    @Qualifier(DataSourceNames.SECOND)
    public DataSource secondDataSource(){
        return DruidDataSourceBuilder.create().build();
    }

}
