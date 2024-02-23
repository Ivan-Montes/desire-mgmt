package dev.ime.exception;

import java.util.Map;
import java.util.UUID;

public class ResourceNotFoundException extends BasicException{

	private static final long serialVersionUID = -7130844309396524940L;

	public ResourceNotFoundException(Map<String, String> errors) {
		super(
				UUID.randomUUID(),
				"ResourceNotFoundException",
				"Knock, knock McFly, you have a ResourceNotFoundException",
				errors
				);
	}

}
