package com.lrz.grovecore.listener;

import jakarta.annotation.PostConstruct;

/**
 * @author lirenzhi
 * @since 2023/1/29 4:27 下午
 */
public interface Listener {

    @PostConstruct
    default void init() {
        ListenerRegister.register(this);
    }
}
