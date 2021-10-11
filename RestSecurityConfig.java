package com.camunda.demo.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import com.camunda.demo.filter.rest.StatelessUserAuthenticationFilter;

import lombok.extern.slf4j.Slf4j;

@Configuration
// java.lang.IllegalStateException: @Order on WebSecurityConfigurers must be unique. Order of 100 was already used on com.camunda.demo.config.RestSecurityConfig$$EnhancerBySpringCGLIB$$b6d4079f@3aa1c45, so it cannot be used on com.camunda.demo.config.WebAppSecurityConfig$$EnhancerBySpringCGLIB$$17298698@6d45dd4 too.
// @Order(SecurityProperties.BASIC_AUTH_ORDER - 20)
@Order(100 - 1)
@Slf4j
public class RestSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	log.info("entering configure");
    	
        http
                .antMatcher("/engine-rest/*")
                .authorizeRequests().anyRequest().authenticated()
                .and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .httpBasic(); // this is just an example, use any auth mechanism you like
    	log.info("leaving configure");

    }

    @Bean
    public FilterRegistrationBean<StatelessUserAuthenticationFilter> statelessUserAuthenticationFilter(){
    	log.info("entering statelessUserAuthenticationFilter");
        FilterRegistrationBean<StatelessUserAuthenticationFilter> filterRegistration = new FilterRegistrationBean<>();
        filterRegistration.setFilter(new StatelessUserAuthenticationFilter());
        //filterRegistration.setOrder(102); // make sure the filter is registered after the Spring Security Filter Chain
        filterRegistration.setOrder(Ordered.LOWEST_PRECEDENCE);
        filterRegistration.addUrlPatterns("/engine-rest/*");
    	log.info("leaving statelessUserAuthenticationFilter");
        return filterRegistration;
    }

}
