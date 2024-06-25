package com.example.uade.tpo.controller.config.security;

import com.example.uade.tpo.controller.config.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

        private final JwtAuthenticationFilter jwtAuthFilter;
        private final AuthenticationProvider authenticationProvider;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .csrf(AbstractHttpConfigurer::disable)
                                .authorizeHttpRequests(req -> req
                                        .requestMatchers(
                                                "/api/auth/**",
                                                "/swagger-ui/**",
                                                "/v3/api-docs/**",
                                                "/swagger-ui.html",
                                                "/swagger-ui/index.html",
                                                "/v3/api-docs/swagger-config",
                                                "/api/order/**",
                                                "/api/payment/**",
                                                "/api/product/**",
                                                "/api/discount/**"
                                        ).permitAll()
                                        .anyRequest().authenticated())
                                .exceptionHandling(exc -> exc
                                        .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                                        .accessDeniedHandler(new CustomAccessDeniedHandler()))
                                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                                .authenticationProvider(authenticationProvider)
                                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }
}

//Logica para dar permisos a los post con los roles