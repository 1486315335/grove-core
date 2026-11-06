package com.lrz.grovecore.autoconfigure;

import com.lrz.grovecore.web.HttpBindFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.DispatcherType;
import javax.servlet.http.HttpServletRequest;

/**
 * 为 Servlet 请求自动绑定 HttpServletContext，无需使用方扫描库内组件。
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnClass({HttpServletRequest.class, OncePerRequestFilter.class})
@ConditionalOnProperty(prefix = "grove.web", name = "http-context-enabled", havingValue = "true", matchIfMissing = true)
public class HttpServletContextAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean(value = HttpBindFilter.class, parameterizedContainer = FilterRegistrationBean.class)
    public FilterRegistrationBean<HttpBindFilter> groveHttpBindFilterRegistration() {
        FilterRegistrationBean<HttpBindFilter> registration = new FilterRegistrationBean<>(new HttpBindFilter());
        registration.setName("groveHttpBindFilter");
        registration.addUrlPatterns("/*");
        // 字符编码过滤器之后、通常的业务过滤器之前绑定上下文。
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE + 10);
        registration.setAsyncSupported(true);
        registration.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.ASYNC, DispatcherType.ERROR);
        return registration;
    }
}
