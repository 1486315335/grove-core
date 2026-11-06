package com.lrz.grovecore.autoconfigure;

import com.lrz.grovecore.redis.lock.DistributedLock;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** 复用业务项目的 RedissonClient，不创建 Redis 连接。 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter(name = {
        "org.redisson.spring.starter.RedissonAutoConfiguration",
        "org.redisson.spring.starter.RedissonAutoConfigurationV2"
})
@ConditionalOnClass(RedissonClient.class)
@ConditionalOnBean(RedissonClient.class)
@ConditionalOnProperty(prefix = "distributed.lock", name = "enable", havingValue = "true", matchIfMissing = false)
public class DistributedLockAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public DistributedLock distributedLock(RedissonClient redissonClient) {
        return new DistributedLock(redissonClient);
    }
}
