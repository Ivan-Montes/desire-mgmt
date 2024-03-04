package dev.ime.configuration;

import java.util.logging.Logger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

	@Bean
	Logger loggerBean() {
		return Logger.getLogger(getClass().getName());
	}
	
}
