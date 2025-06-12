package com.sparta.mms.infrastructure.security.config;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

    // 추후 프론트엔드 배포 시 배포 주소 추가
    private List<String> allowedOrigin = List.of("http://localhost:3000", "http://127.0.0.1:3000");

    // UrlBasedCorsConfiguration이 설정되어 있을 경우 Spring Security에서 CorsFilter 자동 구성
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(allowedOrigin);
        configuration.setAllowedMethods(
            List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "Cookie"));
        configuration.setAllowCredentials(true);

        configuration.setExposedHeaders(List.of("Set-Cookie", "RefreshToken"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}