package dev.ime.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final ConfigurationPropertyValues cpv;
	
	public SecurityConfig(ConfigurationPropertyValues cpv) {
		super();
		this.cpv = cpv;
	}
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	
		return http
				.authorizeHttpRequests( auth -> {
					auth.requestMatchers("/","/index", "/login" ).permitAll();
					auth.requestMatchers("/products/**", "/categories/**").permitAll();
					auth.requestMatchers("/orders/**", "/orderdetails/**").permitAll();
					auth.requestMatchers("/addresses/**", "/customers/**").permitAll();
					auth.requestMatchers("/actuator/**").permitAll();
					auth.requestMatchers("/css/**", "/images/**").permitAll();
					auth.anyRequest().authenticated();
				})
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable) 
                .httpBasic(Customizer.withDefaults())
                .formLogin( f -> f.loginPage("/login").defaultSuccessUrl("/success").permitAll() )
        		.logout(LogoutConfigurer::permitAll)
				.build();
		
	}	


	@Bean
	UserDetailsService users() {

		String passwd = cpv.getPassValue();
		
		UserDetails user = User.builder()
			.username("user")
			.password(passwordEncoder().encode(passwd))
			.roles("USER")
			.build();
		
		UserDetails admin = User.builder()
			.username("admin")
			.password(passwordEncoder().encode(passwd))
			.roles("USER", "ADMIN")
			.build();
		
		return new InMemoryUserDetailsManager(user, admin);
		
	}
	
	
	@Bean
	PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}
}
