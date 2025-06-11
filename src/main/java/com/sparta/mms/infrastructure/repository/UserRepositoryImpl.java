package com.sparta.mms.infrastructure.repository;

import com.sparta.mms.domain.entity.User;
import com.sparta.mms.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final UserInMemoryRepository userInMemoryRepository;

    @Override
    public User save(User user) {
        return userInMemoryRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userInMemoryRepository.findByUsername(username);
    }
}
