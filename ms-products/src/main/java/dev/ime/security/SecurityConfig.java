package dev.ime.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	
		return http
				.authorizeHttpRequests( auth -> {
					auth.requestMatchers("/api/**").permitAll();
                	auth.requestMatchers("/v3/api-docs/**").permitAll();
					auth.requestMatchers("/swagger-ui/**").permitAll();
					auth.requestMatchers("/actuator/**").permitAll();
					auth.anyRequest().authenticated();
				})
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(Customizer.withDefaults())
                .formLogin(Customizer.withDefaults())
        		.logout(LogoutConfigurer::permitAll)
				.build();
		
	}
}
