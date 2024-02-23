package dev.ime.exception;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import io.swagger.v3.oas.annotations.Operation;

@ControllerAdvice
public class GlobalExceptionHandler {

	@Operation( summary = "Basic Exception", description = "Several implementations of it")
	@ExceptionHandler({
			dev.ime.exception.ResourceNotFoundException.class,
			dev.ime.exception.EntityAssociatedException.class,
			dev.ime.exception.AttributeUniqueException.class
			})
	public ResponseEntity<ExceptionResponse> basicException(BasicException ex){
		
		return new ResponseEntity<>( new ExceptionResponse( ex.getIdentifier(),ex.getName(),ex.getDescription(),ex.getErrors() ),
									HttpStatus.NOT_FOUND );
	}	
	
	@Operation( summary = "methodArgumentNotValidException", description = "Some http request attribute has not fulfilled Jakarta validation")
	@ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
	public ResponseEntity<ExceptionResponse> methodArgumentNotValidException(MethodArgumentNotValidException ex){
		
		Map<String, String> errors = new HashMap<>();
		    ex.getBindingResult().getAllErrors().forEach( error -> {
		        String fieldName = ((FieldError) error).getField();
		        String errorMessage = error.getDefaultMessage();
		        errors.put(fieldName, errorMessage);
		    });
		
		return new ResponseEntity<>( new ExceptionResponse( UUID.randomUUID(),
				"MethodArgumentNotValidException",
				"Think McFly, you got MethodArgumentNotValidException",
				errors ),
				HttpStatus.NOT_ACCEPTABLE );
	}
	
	@Operation( summary = "methodArgumentTypeMismatchException", description = "This exception is thrown when method argument is not the expected type, for instance the id attribute in the path of an api request")
	@ExceptionHandler(org.springframework.web.method.annotation.MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ExceptionResponse>methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex){
		
		String attrName = ex.getName();
		String typeName = "Unknown type";				
		Class<?> requiredType = ex.getRequiredType();
		
		if ( requiredType != null && requiredType.getName() != null ) typeName = requiredType.getName();
		
		return new ResponseEntity<>( new ExceptionResponse( UUID.randomUUID(),
				"methodArgumentTypeMismatchException",
				"Think McFly, you got methodArgumentTypeMismatchException",
				Map.of( attrName, typeName)  ),
				HttpStatus.BAD_REQUEST );
	}	
	
	@Operation( summary = "jakartaValidationConstraintViolationException", description = " Called by Hibernate after check class/attributes annotations")
	@ExceptionHandler(
		jakarta.validation.ConstraintViolationException.class
		)		
	public ResponseEntity<ExceptionResponse> jakartaValidationConstraintViolationException(Exception ex){
		
		return new ResponseEntity<>( new ExceptionResponse( UUID.randomUUID(),
				"jakartaValidationConstraintViolationException",
				"Think McFly, you got jakartaValidationConstraintViolationException",
				Map.of( ex.getLocalizedMessage(), ex.getMessage())  ),
				HttpStatus.BAD_REQUEST );
	}	
	
}
