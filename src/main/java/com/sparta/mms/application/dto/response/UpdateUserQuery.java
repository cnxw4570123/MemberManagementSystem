package com.sparta.mms.application.dto.response;

import com.sparta.mms.common.Role;
import com.sparta.mms.domain.entity.User;
import lombok.Builder;

@Builder
public record UpdateUserQuery(
    String username,
    String nickname,
    Role role
    // TODO : roles 필드 추가
) {

    public static UpdateUserQuery from(User user) {
        return UpdateUserQuery.builder()
            .username(user.getUsername())
            .nickname(user.getNickname())
            .role(user.getUserRole())
            .build();
    }
}
