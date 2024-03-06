package com.example.springsecurity.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


public class SecurityAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private CustomUserDetailService customUserDetailService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // ! 인증 완료된 UserDetails 구현 객체
        UserDetails authentication = customUserDetailService.loadUserByUsername("test");

        // ! UsernamePasswordAuthenticationToken의 두번째 생성자
        // # super.setAuthenticated()
//        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(authentication.getUsername(), null, authentication.getAuthorities());
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(authentication, "test", authentication.getAuthorities());

        // ! SecurityContextHolder에 있는 Context 객체의 authentication 여부에 따라, 인증 여부 결정
        SecurityContextHolder.getContext().setAuthentication(auth);
        filterChain.doFilter(request, response);

    }
}
