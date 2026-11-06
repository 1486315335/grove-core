package com.lrz.grovecore.exception;

import com.lrz.grovecore.envm.IEnum;

/**
 * @author lirenzhi
 * @since 2023/3/22 11:01 AM
 */
public class NotCertifiedException extends BusinessException {
    public NotCertifiedException(String message) {
        super(message);
    }

    public NotCertifiedException(IEnum errorCode) {
        super(errorCode);
    }

    public NotCertifiedException(String errorCode, String message) {
        super(errorCode, message);
    }
}
