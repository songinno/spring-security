package com.example.springsecurity.controller;

import com.example.springsecurity.security.SecurityUser;
import com.example.springsecurity.service.TestService;
import lombok.RequiredArgsConstructor;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Collection;

@RequiredArgsConstructor
@RequestMapping("/api/v1/test")
@RestController
public class TestController {
    private final TestService testService;

    @GetMapping("/permit-all")
    public Object getTest() throws Exception {
        return testService.getTest();
    }

/*    @GetMapping("/auth")
    public Object getTest2() {
        return testService.getTest2();
    }*/

    @Secured("ROLE_ADMIN")
    @GetMapping("/auth")
    public Object getTest2(@AuthenticationPrincipal SecurityUser securityUser) throws Exception {
        return testService.getTest2(securityUser);
    }

    // * 인증 완료된 로그인 객체 가져오기
    // ! SecurityContextHolder에서 직접 가져오기
    @Secured("ROLE_ADMIN")
    @GetMapping("/auth-info")
    public Object getTest3() throws  Exception {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails) principal;
        String username = userDetails.getUsername();
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

        System.out.println("username = " + username);
        authorities.stream()
                .forEach(e -> System.out.println("authority = " + e));

        return null;
    }

    // ! Principal 객체 가져오기
    @Secured("ROLE_ADMIN")
    @GetMapping("/auth-info2")
    public Object getTest4(Principal principal) throws  Exception {
        // # 스프링이 아닌 Java에서 정의된 객체로, 사용할만한 메서드가 getName() 밖에 없음
        String username = principal.getName();
        System.out.println("username = " + username);
        return null;
    }
}
