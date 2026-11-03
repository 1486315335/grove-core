package com.lrz.grovecore.redis.lock;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 分布式锁
 *
 * @author lirenzhi
 * @since 2022/12/13 2:41 下午
 */
@Component
@ConditionalOnExpression("${xx.features.redis-enable:true}")
public class DistributedLock {

    @Autowired(required = false)
    private RedissonClient redissonClient;

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
