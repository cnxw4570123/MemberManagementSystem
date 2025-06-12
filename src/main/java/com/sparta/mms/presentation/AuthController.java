package com.sparta.mms.presentation;

import com.sparta.mms.application.UserService;
import com.sparta.mms.common.dto.response.Response;
import com.sparta.mms.common.dto.response.Success;
import com.sparta.mms.presentation.dto.request.SignUpUserRequest;
import com.sparta.mms.presentation.dto.response.SignUpUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/sign-up")
    public ResponseEntity<Response> signUp(@RequestBody SignUpUserRequest request) {
        SignUpUserResponse data = SignUpUserResponse.from(userService.save(request.toCommand()));
        Response response = Success.of(HttpStatus.CREATED, data);

        return response.toResponseEntity();
    }
}
