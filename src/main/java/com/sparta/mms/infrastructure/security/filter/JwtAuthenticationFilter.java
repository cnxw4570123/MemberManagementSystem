package com.sparta.mms.infrastructure.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.mms.common.dto.response.Error;
import com.sparta.mms.common.exception.ErrorCode;
import com.sparta.mms.common.dto.response.Response;
import com.sparta.mms.common.dto.response.Success;
import com.sparta.mms.infrastructure.security.TokenProvider;
import com.sparta.mms.infrastructure.security.UserDetailsImpl;
import com.sparta.mms.infrastructure.security.dto.request.LoginRequest;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j(topic = "로그인 및 JWT 발급")
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final TokenProvider tokenProvider;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void init() {
        setFilterProcessesUrl("/auth/sign-in");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
        HttpServletResponse response) throws
        AuthenticationException {
        try {
            LoginRequest loginDto = new ObjectMapper().readValue(request.getInputStream(),
                LoginRequest.class);
            return getAuthenticationManager().authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.username(),
                    loginDto.password(), null));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
        HttpServletResponse response, FilterChain chain,
        Authentication authResult) throws IOException, ServletException {
        // 아이디와 권한으로 JWT 생성
        Long userId = ((UserDetailsImpl) authResult.getPrincipal()).getUserId();
        String authorities = ((UserDetailsImpl) authResult.getPrincipal()).getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","));

        String accessToken = tokenProvider.generateAccessToken(userId, authorities);

        Success<Map<String, String>> success = Success.ok(
            Collections.singletonMap("accessToken", accessToken));

        setContentTypeAndEncoding(response);
        response.setStatus(success.status().value());

        response.getWriter().write(objectMapper.writeValueAsString(success));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
        HttpServletResponse response,
        AuthenticationException failed) throws IOException, ServletException {

        Response fail = Error.of(
            ErrorCode.INVALID_CREDENTIALS.getHttpStatus(),
            ErrorCode.INVALID_CREDENTIALS.getCode(),
            ErrorCode.INVALID_CREDENTIALS.getMessage()
        );

        setContentTypeAndEncoding(response);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        response.getWriter().write(objectMapper.writeValueAsString(fail));
    }

    private void setContentTypeAndEncoding(HttpServletResponse response) {
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");
    }
}