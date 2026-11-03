package com.lrz.grovecore.function;

import java.util.function.Consumer;

public class EmptyConsumer<T> implements Consumer<T> {
    @Override
    public void accept(T t) {
    }
}