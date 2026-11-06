package com.lrz.grovecore.exception;

import com.lrz.grovecore.envm.IEnum;

/**
 * @author lirenzhi
 * @since 2023/3/22 10:58 AM
 */
public class SplitParametersException extends BusinessException {
    public SplitParametersException(String message) {
        super(message);
    }

    public SplitParametersException(IEnum errorCode) {
        super(errorCode);
    }

    public SplitParametersException(String errorCode, String message) {
        super(errorCode, message);
    }
}
