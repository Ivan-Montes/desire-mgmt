package dev.ime.tool;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

@Component
public class Checker {

	public Checker() {
		super();
	}

	public boolean localDateFormat(String dateString) {
		
		return dateString.matches(RegexPattern.LOCALDATE);
		
	}
	
	public boolean localDateValid(String dateString) {
		
		try {
			
			LocalDate.parse(dateString);
			
		}catch(Exception ex){
			
			return false;
			
		}
		
		return true;
		
	}
	
	public boolean checkCustomerId(Long customerId) {
		return true;
	}
	
	public boolean checkProductId(Long productId) {
		return true;
	}
	
	
}
