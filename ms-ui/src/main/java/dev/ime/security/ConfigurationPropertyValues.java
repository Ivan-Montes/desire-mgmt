package dev.ime.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("dev.ime.value")
public class ConfigurationPropertyValues {

	private String passValue;
	
	public String getPassValue() {
		return passValue;
	}

	public void setPassValue(String passValue) { 
		this.passValue = passValue; 
	}
	
}
