package com.otus.library.library_service.configuration;

import com.otus.library.library_service.security.JwtAuthenticationEntryPoint;
import com.otus.library.library_service.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    public static final String[] PUBLIC_GET = {"/api/books/**", "/api/authors/**", "/api/genres/**"};

    public static final String[] AUTHENTICATED = {"/api/bookings/**", "/api/comments/**", "/api/notifications/**"};

    public static final String[] ADMIN_BUSINESS_LOGIC = {"/api/books/**", "/api/authors/**", "/api/genres/**"};

    public static final String[] ADMIN_SYSTEM = {"/api/users/**", "/api/admin/**", "/api/bookings/admin/**"};

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final JwtAuthenticationEntryPoint unauthorizedHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .exceptionHandling(handling -> handling
                        .authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        //public
                        .requestMatchers(HttpMethod.GET, PUBLIC_GET).permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/users/register").permitAll()
                        //authorized
                        .requestMatchers(AUTHENTICATED).authenticated()
                        //admin
                        .requestMatchers(HttpMethod.POST, ADMIN_BUSINESS_LOGIC).hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, ADMIN_BUSINESS_LOGIC).hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, ADMIN_BUSINESS_LOGIC).hasRole("ADMIN")
                        .requestMatchers(ADMIN_SYSTEM).hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
