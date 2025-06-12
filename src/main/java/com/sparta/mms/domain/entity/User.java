package com.sparta.mms.domain.entity;


import com.sparta.mms.common.Role;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    private Long userId;
    private String username;
    private String nickname;
    private String password;
    private Role userRole;

    public void assignUserId(Long userId) {
        this.userId = userId;
    }

    @Builder(builderMethodName = "withoutId")
    public User(String username, String nickname, String password, Role userRole) {
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.userRole = userRole;
    }

    public void grantAdminRole() {
        this.userRole = Role.ADMIN;
    }
}
