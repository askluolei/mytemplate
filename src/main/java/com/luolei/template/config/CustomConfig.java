package com.luolei.template.config;

import com.luolei.template.common.security.PasswordEncoder;
import com.luolei.template.common.utils.Sequence;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author luolei
 * @email askluolei@gmail.com
 * @date 2017/10/15 21:16
 */
@Configuration
public class CustomConfig {

    @Value("${template.password.strength:-1}")
    private int strength;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new PasswordEncoder(strength);
    }

    @Bean
    public Sequence sequence() {
        return new Sequence(0, 0);
    }
}
