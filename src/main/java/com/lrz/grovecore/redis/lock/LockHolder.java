package com.lrz.grovecore.redis.lock;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.redisson.api.RLock;

/**
 * 分布式锁持有者
 *
 * @author lirenzhi
 * @since 2022/12/13 2:43 下午
 */
@Getter
@Setter
@AllArgsConstructor
public class LockHolder {

    private RLock lock;
    private boolean locked;

    public void releaseLock() {
        if (lock == null || !lock.isHeldByCurrentThread()) {
            return;
        }
        if (locked) {
            lock.unlock();
        }
    }
}
