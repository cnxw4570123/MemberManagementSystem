package com.sparta.mms.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    INVALID_CREDENTIALS("INVALID_CREDENTIALS", "아이디 또는 비밀번호가 일치하지 않습니다."),
    USER_ALREADY_EXISTS("USER_ALREADY_EXISTS", "이미 존재하는 아이디입니다."),
    ACCESS_DENIED("ACCESS_DENIED", "관리자 권한이 필요한 요청입니다. 접근 권한이 없습니다."),
    ;

    private final String code;
    private final String message;


}
