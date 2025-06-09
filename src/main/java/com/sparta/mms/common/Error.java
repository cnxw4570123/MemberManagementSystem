package com.sparta.mms.common;

public record Error(
    String code,
    String message
) implements Response {

    public static Error of(String code, String message) {
        return new Error(code, message);
    }
}
