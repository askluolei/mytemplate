package com.luolei.template.config;

import com.luolei.template.TemplateApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Configuration;

/**
 * 打包成 tomcat 需要的 war
 *
 * @author luolei
 * @email askluolei@gmail.com
 * @date 2017/10/15 20:58
 */
@Configuration
public class TomcatWarConfig extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(TemplateApplication.class);
    }
}
