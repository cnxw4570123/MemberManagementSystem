package com.sparta.mms.common.exception;

import static com.sparta.mms.common.exception.ErrorCode.*;

public class UserNotFoundException extends AbstractBusinessException {

    public UserNotFoundException() {
        super(
            USER_NOT_FOUND.getHttpStatus(),
            USER_NOT_FOUND.getCode(),
            USER_NOT_FOUND.getMessage()
        );
    }
}
