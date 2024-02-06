package com.lbg.markets.poker.security;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class SecurityConfig {

    private final CustomAuthenticationProvider customAuthenticationProvider;

    public SecurityConfig(CustomAuthenticationProvider customAuthenticationProvider) {
        this.customAuthenticationProvider = customAuthenticationProvider;
    }

    @Bean
    @Order(SecurityProperties.DEFAULT_FILTER_ORDER)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .formLogin(form -> form.loginPage("/login").permitAll())
                .authenticationProvider(customAuthenticationProvider)
                .logout(LogoutConfigurer::permitAll)
                .authorizeHttpRequests(auth -> auth
                        .antMatchers("/css/**", "/js/**", "/webjars/**").permitAll()
                        .anyRequest().authenticated());
        return http.build();
    }
}
