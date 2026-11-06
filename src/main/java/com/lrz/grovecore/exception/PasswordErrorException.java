package com.lrz.grovecore.exception;

import com.lrz.grovecore.envm.IEnum;

/**
 * @author lirenzhi
 * @since 2023/3/22 11:05 AM
 */
public class PasswordErrorException extends BusinessException {
    public PasswordErrorException(String message) {
        super(message);
    }

    public PasswordErrorException(IEnum errorCode) {
        super(errorCode);
    }

    public PasswordErrorException(String errorCode, String message) {
        super(errorCode, message);
    }
}
