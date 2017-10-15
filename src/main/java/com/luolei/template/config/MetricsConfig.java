package com.luolei.template.config;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;

/**
 * Metrics 配置
 *
 * @author luolei
 * @email askluolei@gmail.com
 * @date 2017/10/15 20:12
 */
@Configuration
@EnableAspectJAutoProxy
public class MetricsConfig implements CommandLineRunner {

    @Autowired
    private MetricRegistry registry;

    @Override
    public void run(String... args) throws Exception {
        ConsoleReporter reporter = ConsoleReporter.forRegistry(registry).convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS).build();
        reporter.start(600, TimeUnit.SECONDS);//这里给出的示例是每10分钟控制台输出一次统计信息
    }
}
