package com.example.springsecurity.security;

import java.util.Arrays;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CustomUserDetailService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        if (!username.equals("test")) throw new UsernameNotFoundException("유저 이름은 test이어야 합니다.");

        SecurityUser securityUser = new SecurityUser(username, Arrays.asList("ROLE_ADMIN"));

        return securityUser;

    }
}
