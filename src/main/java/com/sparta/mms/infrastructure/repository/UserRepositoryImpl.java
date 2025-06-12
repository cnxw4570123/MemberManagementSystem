package com.sparta.mms.infrastructure.repository;

import com.sparta.mms.common.exception.UserDuplicatedException;
import com.sparta.mms.domain.entity.User;
import com.sparta.mms.domain.repository.UserRepository;
import java.util.Optional;
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
    public Optional<User> findByUsername(String username) {
        return userInMemoryRepository.findByUsername(username);
    }

    @Override
    public Optional<User> finById(long userId) {
        return userInMemoryRepository.findById(userId);
    }
}
