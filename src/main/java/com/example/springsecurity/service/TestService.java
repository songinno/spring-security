package com.example.springsecurity.service;

import com.example.springsecurity.security.SecurityUser;

public interface TestService {
    Object getTest();

//    Object getTest2();

    Object getTest2(SecurityUser securityUser);
}
