package com.sparta.mms.infrastructure;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sparta.mms.common.Role;
import com.sparta.mms.common.exception.ErrorCode;
import com.sparta.mms.common.exception.InvalidCredentialsException;
import com.sparta.mms.common.exception.UserDuplicatedException;
import com.sparta.mms.domain.entity.User;
import com.sparta.mms.domain.repository.UserRepository;
import com.sparta.mms.infrastructure.repository.UserInMemoryRepository;
import com.sparta.mms.infrastructure.repository.UserRepositoryImpl;
import com.sparta.mms.infrastructure.security.config.PasswordConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
    classes = {UserRepositoryImpl.class, UserInMemoryRepository.class, PasswordConfig.class}
)
@Import(UserRepositoryImpl.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserRepositoryTest {

    // TODO: PasswordEncoder 추가 후 matches 확인

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;


    @BeforeAll
    void setUp() {
        User origin = User.withoutId()
            .username("JIN HO")
            .nickname("Mentos")
            .password(passwordEncoder.encode("1234"))
            .userRole(Role.USER)
            .build();

        userRepository.save(origin);
    }

    @Test
    void 저장_성공_테스트() {
        // given
        String orgPassword = "1234";
        User user = User.withoutId()
            .username("Test")
            .nickname("Test123")
            .password(passwordEncoder.encode(orgPassword))
            .userRole(Role.USER)
            .build();

        // when
        User saved = userRepository.save(user);

        // then
        assertEquals(user.getUsername(), saved.getUsername());
        assertTrue(passwordEncoder.matches(orgPassword, saved.getPassword()));
        assertEquals(user.getNickname(), saved.getNickname());
        assertEquals(Role.USER, saved.getUserRole());
    }


    @Test
    void 저장_실패_테스트() {
        // given
        User user = User.withoutId()
            .username("JIN HO")
            .nickname("Mentees")
            .password(passwordEncoder.encode("123411"))
            .userRole(Role.USER)
            .build();

        // when + then
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

        // when + then
        assertDoesNotThrow(() -> {
            userRepository.findByUsername(username)
                .orElseThrow(InvalidCredentialsException::new);
        });

    }

    @Test
    void 찾기_실패_테스트() {
        //given
        String username = "NONE";

        // when + then
        assertThatThrownBy(() -> {
            userRepository.findByUsername(username)
                .orElseThrow(InvalidCredentialsException::new);
        })
            .isInstanceOf(InvalidCredentialsException.class)
            .hasMessageContaining(ErrorCode.INVALID_CREDENTIALS.getMessage());
    }
}
