package com.example.demo.config;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                /*
                // WEB SECURITY - authentication from endpoints
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/posts/public").permitAll()
                        .requestMatchers(HttpMethod.GET, "/posts/**").hasAnyRole("USER" , "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/posts/**").hasRole("USER")
                        .requestMatchers(HttpMethod.DELETE, "/posts/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                 */
                .httpBasic(withDefaults())
                .csrf(csrf -> csrf.disable());

        return http.build();
    }

    @Bean
    UserDetailsService users(PasswordEncoder passwordEncoder) {
        UserDetails user = User.builder()
                .username("user")
                .password(passwordEncoder.encode("password"))
                .roles("USER")
                .build();

        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("password"))
                .roles("ADMIN")
                .build();

        UserDetails vip = User.builder()
                .username("vip")
                .password(passwordEncoder.encode("password"))
                .roles("VIP")
                .build();

        return new InMemoryUserDetailsManager(user, admin, vip);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
