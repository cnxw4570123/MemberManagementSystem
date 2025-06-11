package com.sparta.mms.common.exception;

import static com.sparta.mms.common.exception.ErrorCode.INVALID_TOKEN;

public class InvalidTokenException extends AbstractBusinessException {

    public InvalidTokenException() {
        super(
            INVALID_TOKEN.getHttpStatus(),
            INVALID_TOKEN.getCode(),
            INVALID_TOKEN.getMessage()
        );
    }
}
