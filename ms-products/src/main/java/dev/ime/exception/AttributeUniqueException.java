package dev.ime.exception;

import java.util.Map;
import java.util.UUID;

import dev.ime.tool.SomeConstants;

public class AttributeUniqueException extends BasicException{

	private static final long serialVersionUID = 2993319017153815545L;

	public AttributeUniqueException(Map<String, String> errors) {
		super(
				UUID.randomUUID(), 
				SomeConstants.EX_ATTR_UNIQUE,
				SomeConstants.ANYBODYHOME,
				errors
				);
	}

}
