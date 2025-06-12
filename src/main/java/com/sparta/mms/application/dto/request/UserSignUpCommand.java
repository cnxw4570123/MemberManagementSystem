package com.sparta.mms.application.dto.request;


import com.sparta.mms.common.Role;
import com.sparta.mms.domain.entity.User;
import lombok.Builder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Builder
public record UserSignUpCommand(
    String username,
    String password,
    String nickname,
    Role userRole
) {

    public User toEntity(PasswordEncoder passwordEncoder) {
        return User.withoutId()
            .username(username)
            .password(passwordEncoder.encode(password))
            .nickname(nickname)
            .userRole(userRole)
            .build();
    }
}
