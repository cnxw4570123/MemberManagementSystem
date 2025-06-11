package com.sparta.mms.common;

import com.sparta.mms.common.exception.AbstractBusinessException;
import com.sparta.mms.common.exception.InvalidRoleException;
import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum Role {
    USER(Authority.USER),
    ADMIN(Authority.ADMIN),
    ;


    private final String authority;

    public static Role from(String auth) {
        return Arrays.stream(Role.values())
            .filter(role -> role.getAuthority().equals(auth))
            .findAny()
            .orElseThrow(InvalidRoleException::new);
    }

    public static class Authority {

        public static final String USER = "ROLE_USER";
        public static final String ADMIN = "ROLE_ADMIN";
    }

}
