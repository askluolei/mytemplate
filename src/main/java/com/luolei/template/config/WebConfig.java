package com.luolei.template.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.luolei.template.common.security.MyCorsFilter;
import com.luolei.template.common.support.AccessTokenHandlerMethodArgumentResolver;
import com.luolei.template.common.support.LoginUserHandlerMethodArgumentResolver;
import com.luolei.template.common.support.RequestIpHandlerMethodArgumentResolver;
import com.luolei.template.common.support.RequestPlatformHandlerMethodArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.DispatcherType;
import java.util.List;

/**
 * @author luolei
 * @email askluolei@gmail.com
 * @date 2017/10/14 9:20
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter{

    @Bean
    public FilterRegistrationBean corsFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setDispatcherTypes(DispatcherType.REQUEST);
        registration.setFilter(new MyCorsFilter());
        return registration;
    }

    /**
     * java8 日期api序列化问题
     * rest返回的时候 long 序列化为 String 因为js的number长度不够~
     * @param converters
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        super.extendMessageConverters(converters);
        MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = jackson2HttpMessageConverter.getObjectMapper();
        //不显示为null的字段
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        objectMapper.registerModule(simpleModule);

        jackson2HttpMessageConverter.setObjectMapper(objectMapper);
        //放到第一个
        converters.add(0, jackson2HttpMessageConverter);

        //java8 的时间日期api
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.registerModule(new JavaTimeModule());

        //支持hibernate的延时加载，如果到返回的时候，还没有触发延时加载，那么就返回为null
        objectMapper.registerModule(new Hibernate5Module());
    }


    @Autowired
    private AccessTokenHandlerMethodArgumentResolver accessTokenHandlerMethodArgumentResolver;

    @Autowired
    private LoginUserHandlerMethodArgumentResolver loginUserHandlerMethodArgumentResolver;

    @Autowired
    private RequestPlatformHandlerMethodArgumentResolver requestPlatformHandlerMethodArgumentResolver;

    @Autowired
    private RequestIpHandlerMethodArgumentResolver requestIpHandlerMethodArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        super.addArgumentResolvers(argumentResolvers);
        argumentResolvers.add(accessTokenHandlerMethodArgumentResolver);
        argumentResolvers.add(loginUserHandlerMethodArgumentResolver);
        argumentResolvers.add(requestPlatformHandlerMethodArgumentResolver);
        argumentResolvers.add(requestIpHandlerMethodArgumentResolver);
    }
}
