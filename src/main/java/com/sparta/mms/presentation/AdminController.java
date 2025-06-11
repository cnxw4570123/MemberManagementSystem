package com.sparta.mms.presentation;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    @PatchMapping("/users/{userId}/roles")
    public ResponseEntity<Map<String, Object>> grantAdminRole(
        @PathVariable("userId") String userId
    ) {
        Map<String, Object> data = Map.of(
            "username", "JIN HO",
            "nickname", "Mentos",
            "roles", Map.of("role", "Admin")
        );

        return ResponseEntity.ok(data);
    }
}
