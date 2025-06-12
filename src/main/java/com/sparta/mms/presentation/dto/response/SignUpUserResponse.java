package com.sparta.mms.presentation.dto.response;

import com.sparta.mms.application.dto.response.FindUserQuery;
import com.sparta.mms.common.Role;
import lombok.Builder;

@Builder
public record SignUpUserResponse(
    String username,
    String nickname,
    Role role
) {

    public static SignUpUserResponse from(FindUserQuery findUserQuery) {
        return SignUpUserResponse.builder()
            .username(findUserQuery.username())
            .nickname(findUserQuery.nickname())
            .role(findUserQuery.role())
            .build();
    }
}
