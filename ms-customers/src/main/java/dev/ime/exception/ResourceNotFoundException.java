package dev.ime.exception;

import java.util.Map;
import java.util.UUID;

import dev.ime.tool.SomeConstants;

public class ResourceNotFoundException extends BasicException{

	private static final long serialVersionUID = -7130844309396524940L;

	public ResourceNotFoundException(Map<String, String> errors) {
		super(
				UUID.randomUUID(),
				SomeConstants.EX_RESOURCE_NOT_FOUND,
				SomeConstants.ANYBODYHOME,
				errors
				);
	}

}
