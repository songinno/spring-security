package com.example.springsecurity.configuration;

import com.example.springsecurity.security.CustomAccessDeniedHandler;
import com.example.springsecurity.security.CustomAuthenticationEntryPoint;
import com.example.springsecurity.security.CustomUserDetailService;
import com.example.springsecurity.security.SecurityAuthenticationFilter;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity // # 스프링 시큐리티 활성화
// @EnableGlobalMethodSecurity(securedEnabled = true) // deprecated -> @EnableMethodSecurity
@EnableMethodSecurity // # Controller 메서드에 직접적으로 Role 부여 가능 
public class SecurityConfig {

    @Autowired
    private CustomAuthenticationEntryPoint authenticationEntryPoint;


    @Autowired
    private CustomAccessDeniedHandler accessDeniedHandler;

    // * @EnableMethodSecurity + @Secured(Controller)
    @Autowired
    private CustomUserDetailService customUserDetailService;

    // * Filter
    @Bean
    public SecurityAuthenticationFilter securityAuthenticationFilter() {
        return new SecurityAuthenticationFilter();
    }

// ! cors.configurationSource() 사용 시
//    @Autowired
//    private CorsConfig config;

    // * SecurityFilterChain - Spring Security 5.5 이상
    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // ! 외부 POST 요청을 받기 위해 CSRF 꺼주기
        http.csrf(csrf -> csrf.disable());
        // ! CORS 커스텀
        http.cors(Customizer.withDefaults()); // 기존의 cors()와 같다함
//        http.cors(cors -> cors.configurationSource(config.corsConfigurationSource()));
        // ! 세션 관리X
        http.sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // ! exceptionHandling 추가
        // # UnAutorized(401)
        http.exceptionHandling(exceptionHandling -> 
            exceptionHandling.authenticationEntryPoint(authenticationEntryPoint));
        // # Forbidden(403)
        http.exceptionHandling(exceptionHandling 
            -> exceptionHandling.accessDeniedHandler(accessDeniedHandler));

        // ! 인증 및 인가 관리
        http.authorizeHttpRequests(authorizeHttpRequests ->
                authorizeHttpRequests
                    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/api/v1/test/permit-all").permitAll()
//                        .requestMatchers("api/v1/test/auth").hasRole("ADMIN") // # 접근 권한 제한
                        .requestMatchers("/**").authenticated()
                        .anyRequest().permitAll()
        );

        // ! Filter 적용
        http.addFilterBefore(securityAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}



