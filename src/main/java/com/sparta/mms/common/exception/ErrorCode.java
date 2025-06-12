package com.sparta.mms.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    INVALID_CREDENTIALS(
        HttpStatus.BAD_REQUEST,
        "INVALID_CREDENTIALS",
        "아이디 또는 비밀번호가 일치하지 않습니다."
    ),
    USER_ALREADY_EXISTS(
        HttpStatus.BAD_REQUEST,
        "USER_ALREADY_EXISTS",
        "이미 존재하는 아이디입니다."
    ),
    ACCESS_DENIED(
        HttpStatus.FORBIDDEN,
        "ACCESS_DENIED",
        "관리자 권한이 필요한 요청입니다. 접근 권한이 없습니다."
    ),
    INVALID_TOKEN(
        HttpStatus.UNAUTHORIZED,
        "INVALID_TOKEN",
        "잘못된 토큰입니다. 다시 로그인해주세요."
    ),
    INVALID_ROLE(
        HttpStatus.UNAUTHORIZED,
        "ROLE_NOT_FOUND",
        "잘못된 역할입니다."
    ),
    LOGIN_REQUIRED(
        HttpStatus.UNAUTHORIZED,
        "LOGIN_REQUIRED",
        "로그인 필요입니다."
    ),
    TEMPORARY_SERVER_ISSUE(
        HttpStatus.SERVICE_UNAVAILABLE,
        "TEMPORARY_SERVER_ISSUE",
        "요청을 처리하는데 문제가 발생했습니다. 잠시 후 다시 시도해 주세요."
    ),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;


}
