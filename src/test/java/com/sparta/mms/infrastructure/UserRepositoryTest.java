package com.sparta.mms.infrastructure;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import com.sparta.mms.common.ErrorCode;
import com.sparta.mms.common.Role;
import com.sparta.mms.common.exception.InvalidCredentialsException;
import com.sparta.mms.common.exception.UserDuplicatedException;
import com.sparta.mms.domain.entity.User;
import com.sparta.mms.domain.repository.UserRepository;
import com.sparta.mms.infrastructure.repository.UserInMemoryRepository;
import com.sparta.mms.infrastructure.repository.UserRepositoryImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {UserRepositoryImpl.class, UserInMemoryRepository.class})
@Import(UserRepositoryImpl.class)
public class UserRepositoryTest {

    // TODO:

    @Autowired
    UserRepository userRepository;

    @Test
    void 저장_성공_테스트() {
        // given
        User user = User.withoutId()
            .username("JIN HO")
            .nickname("Mentos")
            .password("1234")
            .userRole(Role.USER)
            .build();

        // when
        User saved = userRepository.save(user);

        // then
        assertEquals(user.getUsername(), saved.getUsername());
        assertEquals(1L, saved.getUserId());
        assertEquals(user.getNickname(), saved.getNickname());
        assertEquals(Role.USER, saved.getUserRole());
    }


    @Test
    @Disabled
    void 저장_실패_테스트() {
        // given
        User user = User.withoutId()
            .username("JIN HO")
            .nickname("Mentos")
            .password("1234")
            .userRole(Role.USER)
            .build();

        // when
        userRepository.save(user);

        // then
        assertThatThrownBy(() -> {
            userRepository.save(user);
        })
            .isInstanceOf(UserDuplicatedException.class)
            .hasMessageContaining(ErrorCode.USER_ALREADY_EXISTS.getMessage());
    }

    @Test
    void 찾기_성공_테스트() {
        // given
        String username = "JIN HO";
        User user = User.withoutId()
            .username("JIN HO")
            .nickname("Mentos")
            .password("1234")
            .userRole(Role.USER)
            .build();

        userRepository.save(user);

        // when
        assertDoesNotThrow(() -> {
            userRepository.findByUsername(username)
                .orElseThrow(InvalidCredentialsException::new);
        });

    }

    @Test
    @Disabled
    void 찾기_실패_테스트() {
        // given
        String username = "JIN HO";
        User user = User.withoutId()
            .username("JIN HO")
            .nickname("Mentos")
            .password("1234")
            .userRole(Role.USER)
            .build();

        userRepository.save(user);

        // when
        assertThatThrownBy(() -> {
            userRepository.findByUsername(username)
                .orElseThrow(RuntimeException::new);
        })
            .isInstanceOf(InvalidCredentialsException.class)
            .hasMessageContaining(ErrorCode.INVALID_CREDENTIALS.getMessage());

    }
}
