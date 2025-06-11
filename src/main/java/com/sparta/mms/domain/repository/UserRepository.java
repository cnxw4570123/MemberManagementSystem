package com.sparta.mms.domain.repository;

import com.sparta.mms.domain.entity.User;
import java.util.Optional;

public interface UserRepository {

    User save(User user);

    Optional<User> findByUsername(String username);
}
