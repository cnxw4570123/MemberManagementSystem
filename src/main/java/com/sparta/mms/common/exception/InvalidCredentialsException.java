package com.sparta.mms.common.exception;

import static com.sparta.mms.common.exception.ErrorCode.*;

public class InvalidCredentialsException extends AbstractBusinessException {

    public InvalidCredentialsException() {
        super(
            INVALID_CREDENTIALS.getHttpStatus(),
            INVALID_CREDENTIALS.getCode(),
            INVALID_CREDENTIALS.getMessage()
        );
    }

}
