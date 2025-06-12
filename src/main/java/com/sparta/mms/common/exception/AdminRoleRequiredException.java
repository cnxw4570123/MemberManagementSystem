package com.sparta.mms.common.exception;

import static com.sparta.mms.common.exception.ErrorCode.ACCESS_DENIED;

public class AdminRoleRequiredException extends AbstractBusinessException {

    public AdminRoleRequiredException() {
        super(
            ACCESS_DENIED.getHttpStatus(),
            ACCESS_DENIED.getCode(),
            ACCESS_DENIED.getMessage()
        );
    }

}
