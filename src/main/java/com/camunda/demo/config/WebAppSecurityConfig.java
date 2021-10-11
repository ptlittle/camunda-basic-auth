package com.camunda.demo.config;

import java.util.Collections;

import org.camunda.bpm.engine.rest.security.auth.ProcessEngineAuthenticationFilter;
import org.camunda.bpm.webapp.impl.security.auth.ContainerBasedAuthenticationFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import lombok.extern.slf4j.Slf4j;

@Configuration
// @Order(SecurityProperties.BASIC_AUTH_ORDER - 15)
@Slf4j
public class WebAppSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		log.info("entering configure");
		// @formatter:off
// o.s.w.s.handler.SimpleUrlHandlerMapping  : Patterns [/webjars/**, /**, /camunda/lib/**, /camunda/api/**, /camunda/app/**] in 'resourceHandlerMapping'    	
		http.antMatcher("/camunda/**")
		.authorizeRequests()
		.anyRequest()
		.authenticated()
		.and()
		.httpBasic();// this is just an example, use any auth mechanism you like
		// @formatter:on
		log.info("leaving configure");

	}

	/**
	 * http://localhost:8080/app/welcome/default/#!/welcome
	 * 
	 * @return
	 */

	@Bean
	public FilterRegistrationBean<ContainerBasedAuthenticationFilter> containerBasedAuthenticationFilter() {
		log.info("entering containerBasedAuthenticationFilter");

//		https://dzone.com/articles/springboot-embedded-camunda-single-sign-on-with-saml-idp-provider
		FilterRegistrationBean<ContainerBasedAuthenticationFilter> filterRegistration = new FilterRegistrationBean<>();

//		https://docs.camunda.org/manual/7.15/webapps/shared-options/container-based-authentication/#enabling-container-based-authentication		
		filterRegistration.setName("Container Based Authentication Filter");
		
//		https://dzone.com/articles/springboot-embedded-camunda-single-sign-on-with-saml-idp-provider
		filterRegistration.setFilter(new ContainerBasedAuthenticationFilter());
		
//		https://dzone.com/articles/springboot-embedded-camunda-single-sign-on-with-saml-idp-provider
//		filterRegistration.setInitParameters(Collections.singletonMap("authentication-provider","com.camunda.demo.filter.webapp.SpringSecurityAuthenticationProvider"));
	
		
//		https://docs.camunda.org/manual/7.15/webapps/shared-options/container-based-authentication/#enabling-container-based-authentication		
		filterRegistration.setInitParameters(Collections.singletonMap("authentication-provider",
				"org.camunda.bpm.engine.rest.security.auth.impl.ContainerBasedAuthenticationProvider"));

// 		https://dzone.com/articles/springboot-embedded-camunda-single-sign-on-with-saml-idp-provider 		
		filterRegistration.setOrder(101); // make sure the filter is registered after the Spring Security Filter Chain
		
//		https://stackoverflow.com/questions/25957879/filter-order-in-spring-boot		
//		filterRegistration.setOrder(Ordered.LOWEST_PRECEDENCE);
		
// 		https://dzone.com/articles/springboot-embedded-camunda-single-sign-on-with-saml-idp-provider 		
//		filterRegistration.addUrlPatterns("/camunda/*");
		
//		https://docs.camunda.org/manual/7.15/webapps/shared-options/container-based-authentication/#enabling-container-based-authentication		
		filterRegistration.addUrlPatterns("/camunda/*");
		
		log.info("leaving containerBasedAuthenticationFilter");
		return filterRegistration;
	}

	@Bean
	public FilterRegistrationBean<ProcessEngineAuthenticationFilter> processEngineAuthenticationFilter() {
		log.info("entering processEngineAuthenticationFilter");

		FilterRegistrationBean<ProcessEngineAuthenticationFilter> registration = new FilterRegistrationBean<>();
		registration.setName("camunda-auth");
		
// https://docs.camunda.org/manual/7.15/reference/rest/overview/authentication/		
		registration.setAsyncSupported(true);
		
		registration.setFilter(new ProcessEngineAuthenticationFilter());
		
// 		https://dzone.com/articles/springboot-embedded-camunda-single-sign-on-with-saml-idp-provider 		
		
//		registration.setInitParameters(Collections.singletonMap("authentication-provider",
//				"com.camunda.demo.filter.webapp.SpringSecurityAuthenticationProvider"));
		
//https://docs.camunda.org/manual/7.15/reference/rest/overview/authentication/
		registration.setInitParameters(Collections.singletonMap("authentication-provider",
				"org.camunda.bpm.engine.rest.security.auth.impl.HttpBasicAuthenticationProvider"));

		registration.setOrder(Ordered.LOWEST_PRECEDENCE);
		
		
// 		https://dzone.com/articles/springboot-embedded-camunda-single-sign-on-with-saml-idp-provider 		
//		registration.addUrlPatterns("/engine-rest/*");
		
//		https://docs.camunda.org/manual/7.15/reference/rest/overview/authentication/		
		registration.addUrlPatterns("/rest/*");
		
		log.info("leaving processEngineAuthenticationFilter");
		return registration;
	}

} // end of class
