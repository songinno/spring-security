package com.example.springsecurity.service.implement;

import com.example.springsecurity.security.SecurityUser;
import com.example.springsecurity.service.TestService;
import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl implements TestService {
    @Override
    public Object getTest() {
        return "test";
    }

/*    @Override
    public Object getTest2() {
        return "test2";
    }*/

    @Override
    public Object getTest2(SecurityUser securityUser) {
        return securityUser;
    }

    
}
