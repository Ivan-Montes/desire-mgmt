package dev.ime.tool;

import java.time.LocalDate;
import java.util.logging.Logger;

import org.springframework.stereotype.Component;

@Component
public class Transformer {

	private final Checker checker;
	private final Logger logger;
	
	public Transformer(Checker checker, Logger logger) {
		super();
		this.checker = checker;
		this.logger = logger;
	}


	public LocalDate fromStringToDateTimeOnErrorNull(String dateString) {
		
		LocalDate dateTimeDecepticon = null;
		
		if ( checker.localDateFormat(dateString) && checker.localDateValid(dateString)) {
			
			try {
				
				dateTimeDecepticon = LocalDate.parse(dateString);
				
			}catch(Exception ex){
				
				logger.warning("Error parsing LocalDate " + dateString);
				return dateTimeDecepticon;				
			}
		}
		
		return dateTimeDecepticon;
	}
	
	
	public LocalDate fromStringToDateTimeOnErrorZeroes(String dateString) {
		
		LocalDate dateTimeDecepticon = LocalDate.of(0, 1, 1);
		
		if ( checker.localDateFormat(dateString) && checker.localDateValid(dateString)) {
			
			try {
				
				dateTimeDecepticon = LocalDate.parse(dateString);
				
			}catch(Exception ex){
				
				logger.warning("Error parsing LocalDate " + dateString);				
			}
		}
		
		return dateTimeDecepticon;
	}
	
}
