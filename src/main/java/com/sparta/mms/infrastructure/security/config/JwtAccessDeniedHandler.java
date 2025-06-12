package com.sparta.mms.infrastructure.security.config;

import static com.sparta.mms.common.exception.ErrorCode.ACCESS_DENIED;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.mms.common.dto.response.Error;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
        AccessDeniedException accessDeniedException) throws IOException, ServletException {

        response.setStatus(ACCESS_DENIED.getHttpStatus().value());
        setContentTypeAndEncoding(response);
        Error error = Error.of(ACCESS_DENIED.getHttpStatus(),
            ACCESS_DENIED.getCode(), ACCESS_DENIED.getMessage());
        response.setStatus(error.status().value());
        response.getWriter().write(objectMapper.writeValueAsString(error));
    }

    private void setContentTypeAndEncoding(HttpServletResponse response) {
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");
    }
}
