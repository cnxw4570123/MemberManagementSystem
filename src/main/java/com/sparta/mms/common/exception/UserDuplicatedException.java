package com.sparta.mms.common.exception;

import com.sparta.mms.common.ErrorCode;

public class UserDuplicatedException extends AbstractBusinessException {

    public UserDuplicatedException() {
        super(ErrorCode.USER_ALREADY_EXISTS.getCode(), ErrorCode.USER_ALREADY_EXISTS.getMessage());
    }
}
