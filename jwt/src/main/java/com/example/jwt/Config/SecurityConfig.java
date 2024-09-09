package com.example.jwt.Config;

import com.example.jwt.filter.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.HttpSecurityDsl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Service;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@Order(1)
public class SecurityConfig {
    @Autowired
    private JwtAuthFilter jwtAuthFilter;
    @Bean
    public UserDetailsService userDetailsService(){
//        UserDetails admin = User.withUsername("Hari")
//                .password(passwordEncoder().encode("admin"))
//                .roles("ADMIN")
//                .build();
//        UserDetails user = User.withUsername("Hari1")
//                .password(passwordEncoder().encode("admin2"))
//                .roles("USER")
//                .build();
//        return new InMemoryUserDetailsManager(admin,user);
        return new UserInfoUserDetailsService();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable()) // Disable CSRF if you're using JWT
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/", "/new", "/authenticate").permitAll() // Allow unauthenticated access to these endpoints
                                .anyRequest().authenticated() // Require authentication for all other endpoints
                )
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Stateless sessions for JWT
                )
                .authenticationProvider(authenticationProvider()) // Use the custom AuthenticationProvider
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class) // Add your JWT filter before username-password filter
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
