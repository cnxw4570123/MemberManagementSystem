package com.sparta.mms.application.dto.response;


import com.sparta.mms.common.Role;
import com.sparta.mms.domain.entity.User;
import lombok.Builder;

@Builder
public record FindUserQuery(
    String username,
    String nickname,
    Role role
    // TODO : roles 필드 추가
) {

    public static FindUserQuery from(User user) {
        return FindUserQuery.builder()
            .nickname(user.getNickname())
            .role(user.getUserRole())
            .username(user.getUsername())
            .build();
    }
}
