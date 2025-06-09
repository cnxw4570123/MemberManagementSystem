package com.sparta.mms.auth.presentation;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    @PostMapping("/sign-up")
    public ResponseEntity<Map<String, Object>> signUp() {
        Map<String, Object> data = Map.of(
            "username", "JIN HO",
            "nickname", "Mentos",
            "roles", Map.of("role", "USER")
        );

        return ResponseEntity.ok(data);
    }


    @PostMapping("/sign-in")
    public ResponseEntity<Map<String, String>> signIn() {

        Map<String, String> data = Map.of("token", "temp-token");

        return ResponseEntity.ok(data);
    }
}
