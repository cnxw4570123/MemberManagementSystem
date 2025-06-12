package com.sparta.mms.presentation.dto.request;

import com.sparta.mms.application.dto.request.UserSignUpCommand;
import com.sparta.mms.common.Role;
import lombok.Builder;

@Builder
public record SignUpUserRequest(
    String username,
    String password,
    String nickname
) {

    public UserSignUpCommand toCommand() {
        return UserSignUpCommand.builder()
            .username(username)
            .password(password)
            .nickname(nickname)
            .userRole(Role.USER)
            .build();
    }
}
