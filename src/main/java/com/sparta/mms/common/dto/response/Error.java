package com.sparta.mms.common.dto.response;

import org.springframework.http.HttpStatus;

public record Error(
    HttpStatus status,
    String code,
    String message
) implements Response {

    public static Error of(HttpStatus status, String code, String message) {
        return new Error(status, code, message);
    }

    @Override
    public HttpStatus getStatus() {
        return status;
    }
}
