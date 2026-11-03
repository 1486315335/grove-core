package com.lrz.grovecore.cache;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * 请求缓存过滤器
 *
 * @author lirenzhi
 * @since 2022/12/16 3:21 下午
 */
@Slf4j
@WebFilter
public class RequestCacheFilter implements Filter {


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        filterChain.doFilter(servletRequest, servletResponse);
        log.info("清除请求缓存");
        RequestCacheContext.remove();
    }
}
