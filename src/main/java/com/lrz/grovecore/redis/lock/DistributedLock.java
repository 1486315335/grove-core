package com.lrz.grovecore.redis.lock;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 分布式锁
 *
 * @author lirenzhi
 * @since 2022/12/13 2:41 下午
 */
public class DistributedLock {

    private final RedissonClient redissonClient;

    public DistributedLock(RedissonClient redissonClient) {
        this.redissonClient = Objects.requireNonNull(redissonClient, "redissonClient");
    }

    public LockHolder tryLock(String key) {
        RLock lock = redissonClient.getLock(key);
        boolean locked = lock.tryLock();
        return new LockHolder(lock, locked);
    }

    public LockHolder tryLock(String key, long time, TimeUnit timeUnit) throws InterruptedException {
        RLock lock = redissonClient.getLock(key);
        boolean locked = lock.tryLock(time, timeUnit);
        return new LockHolder(lock, locked);
    }
}
