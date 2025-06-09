package com.sparta.mms.common.exception;

import com.sparta.mms.common.ErrorCode;

public class AdminRoleRequiredException extends AbstractBusinessException {

    public AdminRoleRequiredException() {
        super(ErrorCode.ACCESS_DENIED.getCode(), ErrorCode.ACCESS_DENIED.getMessage());
    }

}
