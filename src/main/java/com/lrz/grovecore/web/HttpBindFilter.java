package com.lrz.grovecore.web;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

/**
 * @author lirenzhi
 * @since 2023/3/16 2:42 PM
 */
public class HttpBindFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        try {
            HttpServletContext.setContext(request, response);
            filterChain.doFilter(request, response);
        } finally {
            HttpServletContext.removeContext();
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
