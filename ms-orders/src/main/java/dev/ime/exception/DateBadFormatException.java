package dev.ime.exception;

import java.util.Map;
import java.util.UUID;

import dev.ime.tool.SomeConstants;

public class DateBadFormatException extends BasicException {	
	
	private static final long serialVersionUID = 9151458294592882531L;

	public DateBadFormatException(Map<String, String> errors) {
		super(
				UUID.randomUUID(), 
				SomeConstants.EX_DATE_BAD, 
				SomeConstants.ANYBODYHOME,
				errors);
	}

}
