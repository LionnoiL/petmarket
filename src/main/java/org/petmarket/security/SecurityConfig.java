package org.petmarket.security;

import lombok.RequiredArgsConstructor;
import org.petmarket.security.jwt.AccessDeniedHandlerJwt;
import org.petmarket.security.jwt.AuthenticationEntryPointJwt;
import org.petmarket.security.jwt.JwtConfigure;
import org.petmarket.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig {

    private static final String ADMIN_ENDPOINT = "/v1/admin/**";
    private static final String LOGIN_ENDPOINT = "/v1/auth/login";
    private static final String REGISTER_ENDPOINT = "/v1/auth/register";
    private static final String[] ALL_PERMITTED_ENDPOINTS = {
            LOGIN_ENDPOINT,
            REGISTER_ENDPOINT,
            // -- Swagger UI v2
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            // -- Swagger UI v3 (OpenAPI)
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/v1/blog/categories/**"
    };
    private final JwtTokenProvider jwtTokenProvider;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private AuthenticationEntryPointJwt authenticationEntryPointJwt;
    @Autowired
    private AccessDeniedHandlerJwt accessDeniedHandlerJwt;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.cors().disable().csrf()
                .disable()
                .httpBasic().disable()
                .formLogin().disable()
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(ALL_PERMITTED_ENDPOINTS).permitAll()
                        .requestMatchers(ADMIN_ENDPOINT).hasRole("ADMIN")
                        .anyRequest().permitAll()
                )
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPointJwt)
                .and().exceptionHandling().accessDeniedHandler(accessDeniedHandlerJwt)
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().apply(new JwtConfigure(jwtTokenProvider))
        ;

        return http.build();
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setPasswordEncoder(encoder());
        authProvider.setUserDetailsService(userDetailsService);
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(authProvider())
                .build();
    }
}
