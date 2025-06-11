package com.sparta.mms.infrastructure.security.dto.request;


import lombok.Builder;

@Builder
public record LoginRequest(
    String username,
    String password,
    String nickname
) {

}
