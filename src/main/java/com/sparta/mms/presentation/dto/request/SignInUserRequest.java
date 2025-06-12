package com.sparta.mms.presentation.dto.request;

import lombok.Builder;

@Builder
public record SignInUserRequest(
    String username,
    String password
) {

}
