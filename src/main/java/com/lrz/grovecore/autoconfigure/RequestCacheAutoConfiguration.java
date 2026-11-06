package com.lrz.grovecore.autoconfigure;

import com.lrz.grovecore.aspect.ThreadCacheAspect;
import com.lrz.grovecore.cache.RequestCacheFilter;
import org.aspectj.lang.annotation.Aspect;
import org.mvel2.MVEL;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import javax.servlet.DispatcherType;
import javax.servlet.http.HttpServletRequest;

@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter(AopAutoConfiguration.class)
@ConditionalOnClass({HttpServletRequest.class, Aspect.class, MVEL.class})
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnProperty(name = {"grove.web.request-cache-enabled", "spring.aop.auto"}, havingValue = "true", matchIfMissing = true)
public class RequestCacheAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ThreadCacheAspect threadCacheAspect() {
        return new ThreadCacheAspect();
    }

    @Bean
    @ConditionalOnMissingBean(value = RequestCacheFilter.class, parameterizedContainer = FilterRegistrationBean.class)
    public FilterRegistrationBean<RequestCacheFilter> groveRequestCacheFilterRegistration() {
        FilterRegistrationBean<RequestCacheFilter> registration = new FilterRegistrationBean<>(new RequestCacheFilter());
        registration.setName("groveRequestCacheFilter");
        registration.addUrlPatterns("/*");
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE + 20);
        registration.setAsyncSupported(true);
        registration.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.ASYNC, DispatcherType.ERROR);
        return registration;
    }
}
