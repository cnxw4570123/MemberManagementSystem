package com.sparta.mms.domain.repository;

import com.sparta.mms.domain.entity.User;
import java.util.Optional;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository {

    User save(User user);

    Optional<User> findByUsername(String username);

    Optional<User> finById(long userId);

    User update(User user);
}
