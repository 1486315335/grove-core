package com.lrz.grovecore.exception;

import com.lrz.grovecore.envm.IEnum;

/**
 * @author lirenzhi
 * @since 2023/3/22 11:05 AM
 */
public class FormatException extends BusinessException {
    public FormatException(String message) {
        super(message);
    }

    public FormatException(IEnum errorCode) {
        super(errorCode);
    }

    public FormatException(String errorCode, String message) {
        super(errorCode, message);
    }
}
