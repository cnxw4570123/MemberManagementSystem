package com.sparta.mms.infrastructure.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.mms.common.Role;
import com.sparta.mms.common.dto.response.Error;
import com.sparta.mms.common.exception.AbstractBusinessException;
import com.sparta.mms.common.exception.InvalidTokenException;
import com.sparta.mms.common.utils.UserUtils;
import com.sparta.mms.domain.entity.User;
import com.sparta.mms.infrastructure.security.TokenProvider;
import com.sparta.mms.infrastructure.security.TokenStatus;
import com.sparta.mms.infrastructure.security.UserDetailsImpl;
import com.sparta.mms.infrastructure.security.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j(topic = "JWT 검증 및 인가")
@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final String AUTHORIZATION_HEADER = "Authorization";
    private final String BEARER_PREFIX = "Bearer ";
    private final TokenProvider tokenProvider;
    private final UserDetailsServiceImpl userDetailsService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getRequestURI().matches("^/api/v1/auth/");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
        // 인가 필터는 JWT 검증하고, SecurityContext에 Authentication을 저장

        // 1. JWT 검증
        String token = request.getHeader(AUTHORIZATION_HEADER);

        if (!StringUtils.hasText(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        token = token.substring(BEARER_PREFIX.length());
        TokenStatus tokenStatus = tokenProvider.validateAccessToken(token);

        try {
            // 토큰이 정상적이지 않을 경우
            if (tokenStatus == TokenStatus.IS_NOT_VALID) {
                throw new InvalidTokenException();
            }

            // 토큰이 만료 -> 재발급 필요
            if (tokenStatus == TokenStatus.IS_EXPIRED) {
                // TODO : 재발급용 예외코드 추가
                throw new InvalidTokenException();
            }

            // 2. JWT에서 아이디 추출 & 권한 추출
            long userId = tokenProvider.getUserId(token);
            Role userRole = tokenProvider.getUserRole(token);
            LocalDateTime issuedAt = tokenProvider.getIssuedAt(token);
            UserUtils.checkAccessTokenBlocked(userId, issuedAt);

            setAuthentication(userId, userRole);
            filterChain.doFilter(request, response);
        } catch (AbstractBusinessException e) {
            setContentTypeAndEncoding(response);
            Error error = Error.of(e.getHttpStatus(), e.getCode(), e.getMessage());
            response.setStatus(error.status().value());
            response.getWriter().write(objectMapper.writeValueAsString(error));
        }
    }

    // 4. SecurityContext에 Authentication 객체 저장
    private void setAuthentication(long userId, Role userRole) {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = createJwtAuthentication(userId, userRole);

        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    /**
     * Jwt 토큰을 기반으로 인증 주체를 만드는 메서드
     * @param userId
     * @param userRole
     * @return
     */
    private UsernamePasswordAuthenticationToken createJwtAuthentication(long userId,
        Role userRole) {
        User jwtUser = User.withoutId()
            .username(UUID.randomUUID().toString())
            .nickname(UUID.randomUUID().toString())
            .userRole(userRole)
            .build();

        jwtUser.assignUserId(userId);

        UserDetails userDetails = new UserDetailsImpl(jwtUser);

        return new UsernamePasswordAuthenticationToken(
            userDetails,
            null,
            userDetails.getAuthorities()
        );
    }

    private void setContentTypeAndEncoding(HttpServletResponse response) {
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");
    }

}
