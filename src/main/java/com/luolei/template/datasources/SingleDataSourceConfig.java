package com.luolei.template.datasources;

import com.luolei.template.common.jpa.BaseDaoImpl;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author luolei
 * @email askluolei@gmail.com
 * @date 2017/10/28 9:34
 */
@Profile("single")
@Configuration
@EnableJpaAuditing
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.luolei.template", repositoryBaseClass = BaseDaoImpl.class)//设置dao（repo）所在位置
public class SingleDataSourceConfig {
}
