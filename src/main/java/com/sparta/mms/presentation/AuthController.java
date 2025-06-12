package com.sparta.mms.presentation;

import com.sparta.mms.application.UserService;
import com.sparta.mms.common.dto.response.Response;
import com.sparta.mms.common.dto.response.Success;
import com.sparta.mms.presentation.dto.request.SignInUserRequest;
import com.sparta.mms.presentation.dto.request.SignUpUserRequest;
import com.sparta.mms.presentation.dto.response.SignUpUserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Auth", description = "회원 관련 API")
public class AuthController {

    private final UserService userService;

    @PostMapping("/sign-up")
    @Operation(summary = "회원가입")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "회원가입 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "회원가입 실패")
    })

    public ResponseEntity<Response> signUp(@RequestBody SignUpUserRequest request) {
        SignUpUserResponse data = SignUpUserResponse.from(userService.save(request.toCommand()));
        Response response = Success.of(HttpStatus.CREATED, data);

        return response.toResponseEntity();
    }

    @PostMapping("/sign-in")
    @Operation(summary = "로그인")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "로그인 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "로그인 실패")
    })
    public void signIn(@RequestBody SignInUserRequest request) {

    }
}
