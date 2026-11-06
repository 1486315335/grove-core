package com.lrz.grovecore.utils;

import com.lrz.grovecore.function.Executable;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Supplier;

/**
 * 异常工具类
 *
 * @author lirenzhi
 * @since 2023/3/16 11:21 AM
 */
@Slf4j
public final class ExceptionUtil {

    public static <R> R eatException(Supplier<R> supplier, R r) {
        return eatException(supplier, r, () -> null, true, true);
    }

    public static <R> R eatExceptionAndLog(Supplier<R> supplier, R r) {
        return eatException(supplier, r, () -> null, false, false);
    }

    public static <R> R eatException(Supplier<R> supplier,
                                     R afterThrowExceptionReturnValue,
                                     Supplier<String> bizSceneDescription,
                                     boolean printExceptionLog,
                                     boolean printExceptionStack) {
        try {
            return supplier.get();
        } catch (Exception ex) {
            String message = ex.getMessage();
            String bizSceneDesc = bizSceneDescription.get();
            if (StringUtil.isNotEmpty(bizSceneDesc)) {
                message = message + "，原因：" + bizSceneDesc;
            }

            if (printExceptionLog) {
                if (printExceptionStack) {
                    log.error(message, ex);
                } else {
                    log.error(message);
                }
            }
        }
        return afterThrowExceptionReturnValue;
    }

    public static void eatExceptionWithLog(Executable executable, String message) {
        try {
            executable.execute();
        } catch (Exception ex) {
            log.error(message);
        }
    }

    public static String errorTrackSpace(Exception e) {
        StringBuffer sb = new StringBuffer();
        if (e != null) {
            for (StackTraceElement element : e.getStackTrace()) {
                sb.append("\r\n\t").append(element);
            }
        }
        return sb.length() == 0 ? "" : sb.toString();
    }

    /**
     * 把错误信息打印成一整行，会很长建议使用numberOfPositioningRows(Throwable ex)
     *
     * @param e e
     * @return {@link String}
     * @see ExceptionUtil#numberOfPositioningRows(Throwable)
     */
    public static String toOneLineString(Throwable e) {
        return cn.hutool.core.exceptions.ExceptionUtil.stacktraceToOneLineString(e);
    }

    /**
     * 定位行数
     *
     * @param ex 异常
     * @return {@link String}
     */
    public static String numberOfPositioningRows(Throwable ex) {
        return ex.getStackTrace()[0].toString();
    }

    public static String toString(Throwable e) {
        return cn.hutool.core.exceptions.ExceptionUtil.stacktraceToString(e);
    }
}
