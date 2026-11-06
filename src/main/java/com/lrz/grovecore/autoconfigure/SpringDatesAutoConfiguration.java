package com.lrz.grovecore.autoconfigure;

import com.lrz.grovecore.convert.GroveDateConverter;
import com.lrz.grovecore.utils.ApplicationContextHolder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 仅注册 SpringDates 所需组件，不扫描库内其他功能。
 */
@Configuration(proxyBeanMethods = false)
public class SpringDatesAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(ApplicationContextHolder.class)
    public static ApplicationContextHolder applicationContextHolder() {
        return new ApplicationContextHolder();
    }

    @Bean
    @ConditionalOnMissingBean
    public GroveDateConverter groveDateConverter() {
        return new GroveDateConverter();
    }
}
