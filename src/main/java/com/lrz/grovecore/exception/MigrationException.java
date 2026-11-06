package com.lrz.grovecore.exception;

import com.lrz.grovecore.envm.IEnum;

/**
 * @author lirenzhi
 * @since 2023/6/16 3:15 PM
 */
public class MigrationException extends BusinessException {
    public MigrationException(String message) {
        super(message);
    }

    public MigrationException(IEnum errorCode) {
        super(errorCode);
    }
}
