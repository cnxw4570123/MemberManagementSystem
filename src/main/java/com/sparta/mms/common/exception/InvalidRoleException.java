package com.sparta.mms.common.exception;

import static com.sparta.mms.common.exception.ErrorCode.INVALID_ROLE;

public class InvalidRoleException extends AbstractBusinessException {

    public InvalidRoleException() {
        super(
            INVALID_ROLE.getHttpStatus(),
            INVALID_ROLE.getCode(),
            INVALID_ROLE.getMessage()
        );
    }
}
