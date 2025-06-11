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
        HttpStatus.UNAUTHORIZED,
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
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;


}
