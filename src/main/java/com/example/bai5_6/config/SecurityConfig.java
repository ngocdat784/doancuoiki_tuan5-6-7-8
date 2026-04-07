package com.example.bai5_6.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import com.example.bai5_6.Service.AccountService;
import org.thymeleaf.extras.springsecurity6.dialect.SpringSecurityDialect;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;


@Configuration
public class SecurityConfig {

    @Autowired
    private AccountService accountService;
    
    

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

   @Bean
public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {

    AuthenticationManagerBuilder builder =
            http.getSharedObject(AuthenticationManagerBuilder.class);

    builder.userDetailsService(accountService)
           .passwordEncoder(passwordEncoder());

    return builder.build();
}

    @Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    http
        .csrf(csrf -> csrf.disable())

      .authorizeHttpRequests(auth -> auth

    // login + register public
    .requestMatchers("/login", "/register").permitAll()

    // ================= PRODUCT =================
    .requestMatchers("/products/add", "/products/delete/**", "/products/edit/**")
        .hasRole("ADMIN")

    // ================= ORDER =================
    .requestMatchers("/cart/admin/**")
        .hasRole("ADMIN")

    .requestMatchers("/cart/my-orders")
        .authenticated()

    // ================= DEFAULT =================
    .anyRequest().authenticated()
)

        .formLogin(form -> form
            .loginPage("/login")
            .defaultSuccessUrl("/products", true)
            .permitAll()
        )

        .logout(logout -> logout
            .logoutUrl("/logout")
            .logoutSuccessUrl("/login?logout")
            .permitAll()
        );

    return http.build();
}
@Bean
public SpringSecurityDialect securityDialect() {
    return new SpringSecurityDialect();
}
}