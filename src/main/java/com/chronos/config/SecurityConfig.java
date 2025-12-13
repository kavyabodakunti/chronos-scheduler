 package com.chronos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Bean
     SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
        .csrf(csrf -> csrf.disable())   // NEW WAY (no deprecation warning)
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/monitor/**").permitAll()  // public monitoring
            .anyRequest().authenticated()                // everything else secured
        )
        .httpBasic(httpBasic -> {}); 

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {

        UserDetails user = User
                .withUsername("admin")
                .password("{noop}admin123") // no encoding for now
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user);
    }

}
