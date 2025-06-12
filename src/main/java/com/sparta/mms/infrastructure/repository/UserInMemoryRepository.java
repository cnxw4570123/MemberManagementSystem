package com.sparta.mms.infrastructure.repository;

import com.sparta.mms.common.Role;
import com.sparta.mms.domain.entity.User;
import jakarta.annotation.PostConstruct;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class UserInMemoryRepository {

    private final AtomicLong userSequence = new AtomicLong();

    private final ConcurrentHashMap<Long, User> users = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        // ADMIN 생성
        User user = User.withoutId()
            .userRole(Role.ADMIN)
            .nickname("admin")
            .username("admin")
            .password("{bcrypt}$2a$12$7UFZAxCrpxqXyZq.KuvFNunDzYD3.9CJcqQLZPI/pe/qCXpJQDieS")
            .build();
        user.assignUserId(userSequence.addAndGet(1L));

        users.put(user.getUserId(), user);
    }

    // TODO : Trie로 아이디 검색 구현

    public User save(User user) {
        User saved = User.withoutId()
            .username(user.getUsername())
            .nickname(user.getNickname())
            .password(user.getPassword())
            .userRole(user.getUserRole())
            .build();

        saved.assignUserId(2L);

        return saved;
    }

    public Optional<User> findByUsername(String username) {
        // TODO : 지금은 values 돌아다니면서 찾기
        User found = User.withoutId()
            .username("JIN HO")
            .nickname("Mentos")
            .password("{bcrypt}$2a$12$7UFZAxCrpxqXyZq.KuvFNunDzYD3.9CJcqQLZPI/pe/qCXpJQDieS")
            .userRole(Role.USER)
            .build();

        found.assignUserId(2L);
        return Optional.of(found);
    }

    public Optional<User> findById(long userId) {
        User found = User.withoutId()
            .username("JIN HO")
            .nickname("Mentos")
            .password("{bcrypt}$2a$12$7UFZAxCrpxqXyZq.KuvFNunDzYD3.9CJcqQLZPI/pe/qCXpJQDieS")
            .userRole(Role.USER).build();

        found.assignUserId(2L);
        return Optional.of(found);
    }
}