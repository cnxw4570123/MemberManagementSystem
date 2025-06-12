package com.sparta.mms.infrastructure.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.mms.common.dto.response.Error;
import com.sparta.mms.common.dto.response.Response;
import com.sparta.mms.common.exception.ErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

@Slf4j(topic = "미인증")
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException authException) throws IOException, ServletException {

        log.error("인증되지 않은 요청 : {}", authException.getMessage());
        Response error = Error.of(
            ErrorCode.LOGIN_REQUIRED.getHttpStatus(),
            ErrorCode.LOGIN_REQUIRED.getCode(),
            ErrorCode.LOGIN_REQUIRED.getMessage()
        );
        setContentTypeAndEncoding(response);
        response.setStatus(error.getStatus().value());
        response.getWriter().write(objectMapper.writeValueAsString(error));
    }

    private void setContentTypeAndEncoding(HttpServletResponse response) {
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");
    }
}
