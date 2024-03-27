package dev.ime.exceptionhandler;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import dev.ime.exception.*;
import dev.ime.exceptionresponse.ExceptionResponse;
import dev.ime.tool.SomeConstants;
import io.swagger.v3.oas.annotations.Operation;

@ControllerAdvice
public class GlobalExceptionHandler {

	private final Logger logger;	
	
	public GlobalExceptionHandler(Logger logger) {
		super();
		this.logger = logger;
	}

	@Operation( summary = "Basic Exception", description = "Several implementations of it")
	@ExceptionHandler({
			dev.ime.exception.ResourceNotFoundException.class,
			dev.ime.exception.EntityAssociatedException.class,
			dev.ime.exception.BasicException.class
			})
	public ResponseEntity<ExceptionResponse> basicException(BasicException ex){

		logger.severe("Threw " + ex.getName() + " :=: An implementation of Basic Exception Class");
		return new ResponseEntity<>( new ExceptionResponse( ex.getIdentifier(),ex.getName(),ex.getDescription(),ex.getErrors() ),
									HttpStatus.NOT_FOUND );
	}	
	
	@Operation( summary = SomeConstants.EX_METHOD_ARGUMENT_INVALID, description = "Some http request attribute has not fulfilled Jakarta validation")
	@ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
	public ResponseEntity<ExceptionResponse> methodArgumentNotValidException(MethodArgumentNotValidException ex){

		logger.severe("Throw " + SomeConstants.EX_METHOD_ARGUMENT_INVALID);
		Map<String, String> errors = new HashMap<>();
		    ex.getBindingResult().getAllErrors().forEach( error -> {
		        String fieldName = ((FieldError) error).getField();
		        String errorMessage = error.getDefaultMessage();
		        errors.put(fieldName, errorMessage);
		    });
		
		return new ResponseEntity<>( new ExceptionResponse( UUID.randomUUID(),
				SomeConstants.EX_METHOD_ARGUMENT_INVALID,
				"Think McFly, you got MethodArgumentNotValidException",
				errors ),
				HttpStatus.NOT_ACCEPTABLE );
	}
	
	@Operation( summary = SomeConstants.EX_METHOD_ARGUMENT_TYPE, description = "This exception is thrown when method argument is not the expected type, for instance the id attribute in the path of an api request")
	@ExceptionHandler(org.springframework.web.method.annotation.MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ExceptionResponse>methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex){

		logger.severe("Launch " + SomeConstants.EX_METHOD_ARGUMENT_TYPE);
		String attrName = ex.getName();
		String typeName = "Unknown type";				
		Class<?> requiredType = ex.getRequiredType();
		
		if ( requiredType != null && requiredType.getName() != null ) typeName = requiredType.getName();
		
		return new ResponseEntity<>( new ExceptionResponse( UUID.randomUUID(),
				SomeConstants.EX_METHOD_ARGUMENT_TYPE,
				"Think McFly, you got methodArgumentTypeMismatchException",
				Map.of( attrName, typeName)  ),
				HttpStatus.BAD_REQUEST );
	}	
	
	@Operation( summary = SomeConstants.EX_JAKARTA_VAL, description = " Called by Hibernate after check class/attributes annotations")
	@ExceptionHandler(
		jakarta.validation.ConstraintViolationException.class
		)		
	public ResponseEntity<ExceptionResponse> jakartaValidationConstraintViolationException(Exception ex){

		logger.severe("Cast "+ SomeConstants.EX_JAKARTA_VAL);
		return new ResponseEntity<>( new ExceptionResponse( UUID.randomUUID(),
				SomeConstants.EX_JAKARTA_VAL,
				"Whats wrong McFly?, you Chicken got jakartaValidationConstraintViolationException",
				Map.of( ex.getLocalizedMessage(), ex.getMessage())  ),
				HttpStatus.BAD_REQUEST );
	}	
	
	@Operation( summary = SomeConstants.EX_EX, description = " A GeneralException")
	@ExceptionHandler(Exception.class)		
	public ResponseEntity<ExceptionResponse> generalException(Exception ex){

		logger.severe("Spit "+ SomeConstants.EX_EX);
		return new ResponseEntity<>( new ExceptionResponse( UUID.randomUUID(),
				SomeConstants.EX_EX,
				"Whats wrong McFly?, you Chicken got GeneralException",
				Map.of( ex.getLocalizedMessage(), ex.getMessage())  ),
				HttpStatus.BAD_REQUEST );
	}	
	
}
