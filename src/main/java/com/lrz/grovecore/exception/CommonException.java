package com.lrz.grovecore.exception;

import com.lrz.grovecore.envm.IEnum;

/**
 * 通用异常
 *
 * @author lirenzhi
 * @since 2023/6/19 2:58 PM
 */
public class CommonException extends BusinessException {
    public CommonException(String message) {
        super(message);
    }

    public CommonException(IEnum errorCode) {
        super(errorCode);
    }

    public CommonException(String errorCode, String message) {
        super(errorCode, message);
    }
}
