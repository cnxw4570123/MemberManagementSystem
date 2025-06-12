package com.sparta.mms.common.dto.response;

import org.springframework.http.HttpStatus;


public record Success<T>(
    HttpStatus status,
    T data
) implements Response {

    public static <T> Success<T> ok(T data) {
        return new Success<>(HttpStatus.OK, data);
    }

    public static <T> Success<T> of(HttpStatus status, T data) {
        return new Success<>(status, data);
    }

    @Override
    public HttpStatus getStatus() {
        return status;
    }
}
