package dev.ime.exception;

import java.util.Map;
import java.util.UUID;

public class AttributeUniqueException extends BasicException{

	private static final long serialVersionUID = 2993319017153815545L;

	public AttributeUniqueException(Map<String, String> errors) {
		super(
				UUID.randomUUID(), 
				"AttributeUniqueException",
				"Hello, hello, anybody at home? you got AttributeUniqueException",
				errors
				);
	}

}
