package com.luolei.template.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 自定义配置
 * @author luolei
 * @email askluolei@gmail.com
 * @date 2017/10/15 21:26
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "template")
public class TemplateProperties {

    private Long expireTime;
}
