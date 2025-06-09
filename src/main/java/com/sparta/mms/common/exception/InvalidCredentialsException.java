package com.sparta.mms.common.exception;

import static com.sparta.mms.common.ErrorCode.*;

public class InvalidCredentialsException extends AbstractBusinessException {

    public InvalidCredentialsException() {
        super(INVALID_CREDENTIALS.getCode(), INVALID_CREDENTIALS.getMessage());
    }

}
