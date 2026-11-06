package com.lrz.grovecore.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.Locale;

/**
 * HTTP 请求线程的上下文，由 HttpBindFilter 绑定并在当前分派结束后清理。
 * 不会自动传递到 @Async、自建线程或线程池任务中。
 *
 * @author lirenzhi
 * @since 2023/3/16 1:50 PM
 */
@Slf4j
public class HttpServletContext {

    private static final ThreadLocal<Holder> holderThreadLocal = new ThreadLocal<>();

    private static final ThreadLocal<Locale> localeThreadLocal = new ThreadLocal<>();

    private static class Holder {
        private final HttpServletRequest request;
        private final HttpServletResponse response;
        private final String requestId;

        private Holder(HttpServletRequest request, HttpServletResponse response) {
            this.request = request;
            this.response = response;
            this.requestId = "R-" + System.currentTimeMillis();

            if (request == null) {
                setLocale(Locale.SIMPLIFIED_CHINESE);
                return;
            }

            setLocale(request.getLocales().nextElement());
        }
    }

    public static void setContext(HttpServletRequest request, HttpServletResponse response) {
        holderThreadLocal.set(new Holder(request, response));
    }

    public static HttpServletRequest getRequest() {
        Holder holder = holderThreadLocal.get();
        if (holder == null) {
            throw new UnsupportedOperationException("在非web的场景中调用了获取请求的方法");
        }
        return holder.request;
    }

    public static HttpServletResponse getResponse() {
        Holder holder = holderThreadLocal.get();
        if (holder == null) {
            log.warn("在非web的场景中调用了获取请求的方法");
            return null;
        }
        return holder.response;
    }

    /**
     * 设置当前请求的语言
     *
     * @param locale 语言环境
     */
    public static void setLocale(Locale locale) {
        localeThreadLocal.set(locale);
    }

    public static Locale getLocal() {
        Locale locale = localeThreadLocal.get();
        return locale == null ? Locale.SIMPLIFIED_CHINESE : locale;
    }

    public static String getRequestId() {
        Holder holder = holderThreadLocal.get();
        if (holder == null) {
            return null;
        }
        return holder.requestId;
    }

    public static void removeContext() {
        holderThreadLocal.remove();
        localeThreadLocal.remove();
    }
}
