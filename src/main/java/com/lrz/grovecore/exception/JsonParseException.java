package com.lrz.grovecore.exception;

import com.lrz.grovecore.envm.IEnum;

import static com.lrz.grovecore.envm.ErrorCodeEnum.JSON_CONVERT_ERROR;

/**
 * @author lirenzhi
 * @since 2023/3/22 10:55 AM
 */
public class JsonParseException extends BusinessException {

    public JsonParseException() {
        this(JSON_CONVERT_ERROR);
    }

    public JsonParseException(String message) {
        super(message);
    }

    public JsonParseException(IEnum errorCode) {
        super(errorCode);
    }

    public JsonParseException(String errorCode, String message) {
        super(errorCode, message);
    }
}
