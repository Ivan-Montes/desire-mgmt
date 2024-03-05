package dev.ime.exception;

import java.util.Map;
import java.util.UUID;

public class DateBadFormatException extends BasicException {	
	
	private static final long serialVersionUID = 9151458294592882531L;

	public DateBadFormatException(Map<String, String> errors) {
		super(
				UUID.randomUUID(), 
				"DateBadFormatException", 
				"Knock, knock McFly, you have a EntityAssociatedException",
				errors);
	}

}
