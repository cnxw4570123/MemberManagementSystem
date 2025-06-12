package com.sparta.mms.application;

import com.sparta.mms.application.dto.request.UserSignUpCommand;
import com.sparta.mms.application.dto.response.FindUserQuery;
import com.sparta.mms.common.exception.UserDuplicatedException;
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
        userRepository.findByUsername(command.username())
            .ifPresent(user -> {
                throw new UserDuplicatedException();
            });

        User save = userRepository.save(command.toEntity(passwordEncoder));
        return FindUserQuery.from(save);
    }

    public Optional<User> findById(long userId) {
        return userRepository.finById(userId);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
