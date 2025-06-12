package com.sparta.mms.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AbstractBusinessException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final String code;

    public AbstractBusinessException(HttpStatus status, String code, String message) {
        super(message);
        this.httpStatus = status;
        this.code = code;
    }
}
