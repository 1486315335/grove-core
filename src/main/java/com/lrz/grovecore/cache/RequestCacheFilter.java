package com.lrz.grovecore.cache;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 请求缓存过滤器
 *
 * @author lirenzhi
 * @since 2022/12/16 3:21 下午
 */
public class RequestCacheFilter extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        RequestCacheContext.begin();
        try {
            filterChain.doFilter(request, response);
        } finally {
            RequestCacheContext.remove();
        }
    }

    @Override
    protected boolean shouldNotFilterAsyncDispatch() {
        return false;
    }

    @Override
    protected boolean shouldNotFilterErrorDispatch() {
        return false;
    }
}
