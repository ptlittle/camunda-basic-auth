package com.camunda.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class SecurityConfig {

    // This is just a very simple Identity Management solution for demo purposes.
    // In real world scenarios, this would be replaced by the actual IAM solution
    @SuppressWarnings("deprecation")
    @Bean
    public UserDetailsService userDetailsService() {
    	log.info("entering userDetailsService");
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withDefaultPasswordEncoder().username("camunda-admin").password("fr1d8y").roles("ACTUATOR", "camunda-admin").build());
        manager.createUser(User.withDefaultPasswordEncoder().username("john").password("john").roles("camunda-user").build());
    	log.info("leaving userDetailsService");
        return manager;
    }

}