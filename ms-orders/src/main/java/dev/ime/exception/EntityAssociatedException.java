package dev.ime.exception;

import java.util.Map;
import java.util.UUID;

public class EntityAssociatedException  extends BasicException{

	private static final long serialVersionUID = -7622584198785659603L;

	public EntityAssociatedException( Map<String, String> errors ) {
		super(
				UUID.randomUUID(), 
				"EntityAssociatedException", 
				"Knock, knock McFly, you have a EntityAssociatedException",
				errors
				);
	}

	
}
