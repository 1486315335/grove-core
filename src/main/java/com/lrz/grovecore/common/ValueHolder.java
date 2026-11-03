package com.lrz.grovecore.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValueHolder<T> {
    private T value;

    public ValueHolder(T value) {
        this.value = value;
    }

    public static <T> ValueHolder<T> empty() {
        return new ValueHolder<>(null);
    }

    public boolean ifPresent() {
        return value != null;
    }

    public T get() {
        return value;
    }
}