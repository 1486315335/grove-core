package com.lrz.grovecore.exception;

import com.lrz.grovecore.envm.IEnum;
import com.lrz.grovecore.utils.ObjectUtil;
import lombok.Getter;
import lombok.Setter;

/**
 * @author lirenzhi
 * @since 2023/3/21 4:09 PM
 */
@Getter
@Setter
public abstract class BusinessException extends RuntimeException {

    private String errorCode;
    private static final String SERVICE_NAME = ObjectUtil.nvl(System.getenv("SERVICE_NAME"), System.getProperty("SERVICE_NAME"));
    private String service = SERVICE_NAME;

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(IEnum errorCode) {
        this(errorCode.getMessage());
        this.errorCode = errorCode.getCode();
    }

    public BusinessException(String errorCode, String message) {
        this(message);
        this.errorCode = errorCode;
    }
}
