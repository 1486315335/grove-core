package com.lrz.grovecore.exception;

import com.lrz.grovecore.envm.IEnum;

/**
 * 未知异常
 *
 * @author lirenzhi
 * @since 2023/3/21 4:36 PM
 */
public class NoRoleException extends BusinessException {

    public NoRoleException(String message) {
        super(message);
    }

    public NoRoleException(IEnum errorCode) {
        super(errorCode);
    }

    public NoRoleException(String errorCode, String message) {
        super(errorCode, message);
    }
}
