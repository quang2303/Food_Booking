package com.quang.app.JavaWeb_cdquang.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.quang.app.JavaWeb_cdquang.security.CustomAuthenticationFailureHandler;
import com.quang.app.JavaWeb_cdquang.security.Md5PasswordEncoder;
import com.quang.app.JavaWeb_cdquang.service.CustomUserDetailsService;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {
	@Autowired
	CustomAuthenticationFailureHandler authenticationFailureHandler;
	
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
		.csrf(csrf -> csrf.disable())
		.authorizeHttpRequests(auth -> auth
				.requestMatchers("/user/**").permitAll()
				.requestMatchers("/admin/**").hasRole("ADMIN")
				.anyRequest().permitAll()
				)
		.formLogin(form -> form
				.loginPage("/admin/login.html")
				.loginProcessingUrl("/api/auth/login")
				.failureHandler(authenticationFailureHandler)
				.defaultSuccessUrl("/admin/home.html", true)
				.permitAll()
				)
		.logout(logout -> logout
				.logoutUrl("/logout")
				.invalidateHttpSession(true)
                .deleteCookies("JSESSIONID").permitAll())
		.httpBasic(Customizer.withDefaults());
		
		return http.build();
	}
	
	@Bean
    PasswordEncoder passwordEncoder() {
        return new Md5PasswordEncoder();
    }
	
	@Bean
	UserDetailsService userDetailsService() {
	    return new CustomUserDetailsService();
	}

}
