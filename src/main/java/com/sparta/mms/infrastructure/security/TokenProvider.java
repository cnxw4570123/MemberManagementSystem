package com.sparta.mms.infrastructure.security;

import com.sparta.mms.common.Role;
import com.sparta.mms.common.exception.AbstractBusinessException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TokenProvider {

    private int accessTokenExp;
    private String ISSUER = "Baro-Intern";
    private final String AUTHORIZATION_KEY = "auth";
    private final Long TO_MILLIS = 1_000L;

    private SecretKey secretKey;

    public TokenProvider(
        @Value("${jwt.secret}") String salt,
        @Value("${jwt.access.exp}") int accessTokenExp
    ) {
        this.accessTokenExp = accessTokenExp;
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(salt));
    }

    // generation - 생성
    public String generateAccessToken(long userId, String authorities) {
        Date now = new Date();

        return Jwts.builder()
            .subject(String.valueOf(userId))
            .claim(AUTHORIZATION_KEY, authorities)
            .issuer(ISSUER)
            .issuedAt(now)
            .expiration(Date.from(now.toInstant().plusMillis(accessTokenExp * TO_MILLIS)))
            .signWith(secretKey, Jwts.SIG.HS512)
            .compact();
    }

    /**
     * 토큰 상태 검증 메서드
     * @param token : 전달받은 JWT 토큰
     * @return
     * - IS_VALID : 액세스 토큰 정상, 사용 가능 <br>
     * - IS_EXPIRED : 액세스 토큰 만료됨, 재발급 필요 <br>
     * - IS_NOT_VALID : 액세스 토큰 사용 불가능, 재인증 필요
     */
    public TokenStatus validateAccessToken(String token) {
        try {
            Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token);

            return TokenStatus.IS_VALID;
        } catch (SignatureException | MalformedJwtException e) {
            log.error("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.error("만료된 JWT 서명입니다. 토큰 재발급이 필요합니다.");
            return TokenStatus.IS_EXPIRED;
        } catch (UnsupportedJwtException e) {
            log.error("지원되지 않는 JWT 서명입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT 토큰이 잘못 되었습니다.");
        }
        return TokenStatus.IS_NOT_VALID;
    }


    // 데이터 추출
    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(accessToken)
                .getPayload();
        } catch (JwtException e) {
            // TODO : 예외 코드 추가 INVALID_TOKEN
            throw new AbstractBusinessException(
                HttpStatus.UNAUTHORIZED,
                "INVALID_TOKEN",
                "토큰이 만료되었습니다. 다시 로그인해주세요."
            );
        }
    }

    // userId 추출
    public long getUserId(String accessToken) {
        return Long.parseLong(parseClaims(accessToken).getSubject());
    }

    /**
     * Jwt 토큰 내의 auth 클레임을 기준으로 사용자 역할 부여
     * @param accessToken
     * @return 부여받은 역할군
     */
    public Role getUserRole(String accessToken) {
        return Role.from(parseClaims(accessToken).get(AUTHORIZATION_KEY, String.class));
    }

    public LocalDateTime getIssuedAt(String accessToken) {
        Date issuedAt = parseClaims(accessToken).getIssuedAt();

        return issuedAt.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime();
    }
}
