package com.sparta.mms.presentation.dto.response;

import com.sparta.mms.application.dto.response.UpdateUserQuery;
import com.sparta.mms.common.Role;
import lombok.Builder;

@Builder
public record GrantUserAdminResponse(
    String username,
    String nickname,
    Role role
) {

    public static GrantUserAdminResponse from(UpdateUserQuery userQuery) {
        return GrantUserAdminResponse.builder()
            .username(userQuery.username())
            .nickname(userQuery.nickname())
            .role(userQuery.role())
            .build();
    }
}
