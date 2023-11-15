package com.lms.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	SecurityFilterChain sfc(HttpSecurity http) throws Exception {

		http.csrf(csrf -> csrf.disable());
		http.cors(cor -> cor.configurationSource(new CorsConfigurationSource() {

			@Override
			public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

				List<String> listoforigin = List.of("http://localhost:3000/");
				List<String> listofmethods = List.of("GET", "POST","PUT","DELETE");
				CorsConfiguration cfg = new CorsConfiguration();

				cfg.setAllowedOrigins(listoforigin);
				cfg.setAllowedMethods(listofmethods);

				return cfg;

			}
		}));

		http.authorizeHttpRequests(
				auth -> auth.requestMatchers("/user/testdb").authenticated().requestMatchers("/save").permitAll());
		http.formLogin(Customizer.withDefaults()).httpBasic(Customizer.withDefaults());

		return http.build();
	}

}
