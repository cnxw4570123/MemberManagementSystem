package com.sparta.mms.infrastructure.security;

import com.sparta.mms.application.UserService;
import com.sparta.mms.common.exception.InvalidCredentialsException;
import com.sparta.mms.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username)
            .orElseThrow(InvalidCredentialsException::new);

        return new UserDetailsImpl(user);
    }

    public UserDetails loadUserByUserId(long userId) {
        User user = userService.findById(userId)
            .orElseThrow(InvalidCredentialsException::new);
        return new UserDetailsImpl(user);
    }
}
