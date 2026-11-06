package com.lrz.grovecore.redis.lock;

import com.lrz.grovecore.function.Executable;
import com.lrz.grovecore.utils.ElapsedTimeUtil;
import com.lrz.grovecore.utils.ApplicationContextHolder;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * 分布式锁上下文。只有成功获得锁才执行回调，执行后释放锁并清理当前任务名。
 * 未获得锁时跳过回调；getWithDistributedLock 返回 null。
 *
 * @author lirenzhi
 * @since 2022/12/13 2:31 下午
 */
@Slf4j
public class DistributedLockContext {

    static ThreadLocal<String> nameThreadLocal = new ThreadLocal<>();

    public static void setName(String taskName) {
        nameThreadLocal.set(taskName);
    }

    public static void doWithDistributedLock(String key, long time, TimeUnit timeUnit, Executable executable) throws InterruptedException {
        DistributedLock distributedLock = ApplicationContextHolder.getBean(DistributedLock.class);
        LockHolder lockHolder = distributedLock.tryLock(key, time, timeUnit);
        doConsumer(key, executable, lockHolder);
    }

    public static void doWithDistributedLock(String key, Executable executable) {
        DistributedLock distributedLock = ApplicationContextHolder.getBean(DistributedLock.class);
        LockHolder lockHolder = distributedLock.tryLock(key);
        doConsumer(key, executable, lockHolder);
    }

    private static void doConsumer(String key, Executable executable, LockHolder lockHolder) {
        if (!lockHolder.isLocked()) {
            logSkipped();
            return;
        }
        long start = System.currentTimeMillis();
        try {
            log();
            executable.execute();
        } finally {
            String elapsed = ElapsedTimeUtil.elapsedTimeFrom(start);
            if (nameThreadLocal.get() != null) {
                log.info("分布式锁环境下执行[{}] 结束，释放分布式锁：{}，占用：{}", nameThreadLocal.get(), key, elapsed);
            } else {
                log.info("释放分布式锁：{}，占用：{}", key, elapsed);
            }
            lockHolder.releaseLock();
        }
    }

    public static <E> E getWithDistributedLock(String key, Supplier<E> supplier) {
        DistributedLock distributedLock = ApplicationContextHolder.getBean(DistributedLock.class);
        LockHolder lockHolder = distributedLock.tryLock(key);
        return doSupplier(key, supplier, lockHolder);
    }

    public static <E> E getWithDistributedLock(String key, long time, TimeUnit timeUnit, Supplier<E> supplier) throws InterruptedException {
        DistributedLock distributedLock = ApplicationContextHolder.getBean(DistributedLock.class);
        LockHolder lockHolder = distributedLock.tryLock(key, time, timeUnit);
        return doSupplier(key, supplier, lockHolder);
    }

    private static <E> E doSupplier(String key, Supplier<E> supplier, LockHolder lockHolder) {
        if (!lockHolder.isLocked()) {
            logSkipped();
            return null;
        }
        try {
            log();
            return supplier.get();
        } finally {
            if (nameThreadLocal.get() != null) {
                log.info("分布式锁环境下执行[{}] 结束，释放分布式锁：{}", nameThreadLocal.get(), key);
            } else {
                log.info("释放分布式锁：{}", key);
            }
            lockHolder.releaseLock();
        }
    }

    private static void logSkipped() {
        if (nameThreadLocal.get() != null) {
            log.info("执行[{}]因为未抢到分布式锁，被跳过", nameThreadLocal.get());
        } else {
            log.info("未抢到分布式锁，被跳过");
        }
    }

    private static void log() {
        if (nameThreadLocal.get() != null) {
            log.info("分布式锁环境下执行[{}] 开始", nameThreadLocal.get());
        }
    }
}
