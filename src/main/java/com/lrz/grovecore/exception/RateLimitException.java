package com.lrz.grovecore.exception;

import com.lrz.grovecore.envm.IEnum;

public class RateLimitException extends BusinessException {

    public RateLimitException(String message) {
        super(message);
    }

    public RateLimitException(IEnum errorCode) {
        super(errorCode);
    }

    public RateLimitException(String errorCode, String message) {
        super(errorCode, message);
    }
}