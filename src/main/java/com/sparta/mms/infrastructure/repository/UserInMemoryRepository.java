package com.sparta.mms.infrastructure.repository;

import com.sparta.mms.common.Role;
import com.sparta.mms.domain.entity.User;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class UserInMemoryRepository {


    public User save(User user) {
        User saved = User.withoutId()
            .username(user.getUsername())
            .nickname(user.getNickname())
            .password(user.getPassword())
            .userRole(user.getUserRole())
            .build();

        saved.assignUserId(1L);

        return saved;
    }

    public Optional<User> findByUsername(String username) {
        User found = User.withoutId()
            .username("JIN HO")
            .nickname("Mentos")
            .password("1234")
            .userRole(Role.USER)
            .build();

        found.assignUserId(1L);
        return Optional.of(found);
    }
}
