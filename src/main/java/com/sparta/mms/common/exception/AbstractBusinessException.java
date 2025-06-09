package com.sparta.mms.common.exception;

import lombok.Getter;

@Getter
public class AbstractBusinessException extends RuntimeException {

    private final String code;

    public AbstractBusinessException(String code, String message) {
        super(message);
        this.code = code;
    }
}
