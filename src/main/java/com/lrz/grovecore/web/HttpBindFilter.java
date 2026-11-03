package com.lrz.grovecore.web;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author lirenzhi
 * @since 2023/3/16 2:42 PM
 */
@WebFilter
public class HttpBindFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        HttpServletContext.setContext(request, response);
        filterChain.doFilter(servletRequest, servletResponse);
        HttpServletContext.removeContext();
    }
}
