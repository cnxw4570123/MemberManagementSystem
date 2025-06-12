package com.sparta.mms.infrastructure.repository;

import com.sparta.mms.common.Role;
import com.sparta.mms.common.exception.UserDuplicatedException;
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

    public synchronized User save(User user) {
        findByUsername(user.getUsername())
            .ifPresent(u -> {
                throw new UserDuplicatedException();
            });

        long userId = userSequence.addAndGet(1L);

        user.assignUserId(userId);
        users.put(userId, user);

        return users.get(userId);
    }

    public synchronized Optional<User> findByUsername(String username) {
        // TODO : Trie로 아이디 검색 구현

        for (User user : users.values()) {
            if (user.getUsername().equals(username)) {
                return Optional.of(user);
            }
        }

        return Optional.empty();

    }

    public synchronized Optional<User> findById(long userId) {
        return Optional.ofNullable(users.getOrDefault(userId, null));
    }
}