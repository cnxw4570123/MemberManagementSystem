package com.sparta.mms.common.exception;

import static com.sparta.mms.common.exception.ErrorCode.USER_ALREADY_EXISTS;

public class UserDuplicatedException extends AbstractBusinessException {

    public UserDuplicatedException() {
        super(
            USER_ALREADY_EXISTS.getHttpStatus(),
            USER_ALREADY_EXISTS.getCode(),
            USER_ALREADY_EXISTS.getMessage()
        );
    }
}
