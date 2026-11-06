package com.lrz.grovecore.exception;

import com.lrz.grovecore.envm.IEnum;

import static com.lrz.grovecore.envm.ErrorCodeEnum.UNKNOWN;

/**
 * 未知异常
 *
 * @author lirenzhi
 * @since 2023/3/21 4:36 PM
 */
public class UnKnownException extends BusinessException {

    public UnKnownException(String message) {
        super(message);
    }

    public UnKnownException(IEnum errorCode) {
        super(errorCode);
    }

    public UnKnownException() {
        this(UNKNOWN);
    }

    public UnKnownException(String errorCode, String message) {
        super(errorCode, message);
    }
}
