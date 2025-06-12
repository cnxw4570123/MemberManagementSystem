package com.sparta.mms.application;

import com.sparta.mms.application.dto.request.UserSignUpCommand;
import com.sparta.mms.application.dto.response.FindUserQuery;
import com.sparta.mms.application.dto.response.UpdateUserQuery;
import com.sparta.mms.common.exception.UserNotFoundException;
import com.sparta.mms.common.utils.UserUtils;
import com.sparta.mms.domain.entity.User;
import com.sparta.mms.domain.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public FindUserQuery save(UserSignUpCommand command) {
        User save = userRepository.save(command.toEntity(passwordEncoder));
        return FindUserQuery.from(save);
    }

    public Optional<User> findById(long userId) {
        return userRepository.finById(userId);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public UpdateUserQuery grantUserRoleAdmin(long userId) {
        User user = userRepository.finById(userId)
            .orElseThrow(UserNotFoundException::new);

        user.grantAdminRole();

        User update = userRepository.update(user);

        // 기존 권한으로 발급한 액세스 토큰 만료 처리 -> 재로그인 요구
        UserUtils.markUserModified(userId);

        return UpdateUserQuery.from(update);

    }
}
