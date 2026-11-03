package com.lrz.grovecore.aspect;

import com.google.common.collect.Maps;
import com.lrz.grovecore.annotation.RequestCached;
import com.lrz.grovecore.cache.RequestCacheContext;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.mvel2.MVEL;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.ConcurrentMap;

/**
 * 方法缓存切面
 *
 * @author lirenzhi
 * @since 2022/12/15 4:07 下午
 */
@Slf4j
@Aspect
@Component
public class ThreadCacheAspect {

    private static final ConcurrentMap<String, Serializable> expressionCache = Maps.newConcurrentMap();

    @Pointcut("@annotation(com.lrz.grovecore.annotation.RequestCached))")
    public void pointCut() {
    }

    @Around("pointCut()&&@annotation(requestCached)")
    public Object around(ProceedingJoinPoint joinPoint, RequestCached requestCached) throws Throwable {

        String cacheKey = createCacheKey(joinPoint, requestCached);

        Object obj = RequestCacheContext.get(cacheKey);

        if (obj != null) {
            return obj;
        }
        Object proceed = joinPoint.proceed();
        if (proceed != null) {
            RequestCacheContext.set(cacheKey, proceed);
        }
        return proceed;
    }

    private String createCacheKey(ProceedingJoinPoint joinPoint, RequestCached requestCached) {

        Object[] args = joinPoint.getArgs();

        String key = requestCached.key();
        if (Objects.nonNull(key)) {
            return generateCacheKey(key, joinPoint.getTarget(), args, null);
        }

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getMethod().getName();
        String className = joinPoint.getTarget().getClass().getName();
        return String.join("#", className, methodName);
    }

    public String generateCacheKey(String key, Object target, Object[] args, Object result) {
        Serializable expression = loadExpressionFromCache(key);

        HashMap<String, Object> map = Maps.newHashMap();
        map.put("self", target);
        map.put("args", args);
        map.put("result", result);
        return "REQUEST_CACHE::" + MVEL.executeExpression(expression, map);
    }

    private Serializable loadExpressionFromCache(String key) {
        if (expressionCache.containsKey(key)) {
            return expressionCache.get(key);
        }
        Serializable serializable = MVEL.compileExpression(key);
        expressionCache.put(key, serializable);
        return serializable;
    }
}
