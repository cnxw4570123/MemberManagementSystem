package com.sparta.mms.presentation;

import com.sparta.mms.application.UserService;
import com.sparta.mms.application.dto.response.UpdateUserQuery;
import com.sparta.mms.common.dto.response.Response;
import com.sparta.mms.common.dto.response.Success;
import com.sparta.mms.presentation.dto.response.GrantUserAdminResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @PatchMapping("/users/{userId}/roles")
    public ResponseEntity<Response> grantAdminRole(
        @PathVariable("userId") Long userId
    ) {
        UpdateUserQuery updateUserQuery = userService.grantUserRoleAdmin(userId);
        Response response = Success.of(HttpStatus.ACCEPTED,
            GrantUserAdminResponse.from(updateUserQuery));

        return response.toResponseEntity();
    }
}
