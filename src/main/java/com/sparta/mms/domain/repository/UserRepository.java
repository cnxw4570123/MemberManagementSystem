package com.sparta.mms.domain.repository;

import com.sparta.mms.domain.entity.User;

public interface UserRepository {

    User save(User user);

    User findByUsername(String username);
}
