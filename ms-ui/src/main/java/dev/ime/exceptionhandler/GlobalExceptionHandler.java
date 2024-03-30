package dev.ime.exceptionhandler;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import org.springframework.ui.Model;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;

import dev.ime.exception.*;
import dev.ime.exceptionresponse.ExceptionResponse;
import dev.ime.tool.SomeConstants;
import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionHandler {

	private static final String PATH_ERROR_EXCEPTION = "error/exception";
	private final Logger logger;	
	
	public GlobalExceptionHandler(Logger logger) {
		super();
		this.logger = logger;
	}

	@ExceptionHandler({
			dev.ime.exception.ResourceNotFoundException.class,
			dev.ime.exception.EntityAssociatedException.class,
			dev.ime.exception.DateBadFormatException.class,
			dev.ime.exception.AttributeUniqueException.class,
			dev.ime.exception.BasicException.class
			})
	public String basicException(BasicException ex, Model model){

		logger.severe("Threw " + ex.getName() + " :=: An implementation of Basic Exception Class");
		model.addAttribute(SomeConstants.EX_EX, new ExceptionResponse(  ex.getIdentifier(),
																	ex.getName(),
																	ex.getDescription(),
																	ex.getErrors() ));
		
		return PATH_ERROR_EXCEPTION;
	}	
	
	@ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
	public String methodArgumentNotValidException(MethodArgumentNotValidException ex, Model model){

		logger.severe("Throw " + SomeConstants.EX_METHOD_ARGUMENT_INVALID);
		Map<String, String> errors = new HashMap<>();
		    ex.getBindingResult().getAllErrors().forEach( error -> {
		        String fieldName = ((FieldError) error).getField();
		        String errorMessage = error.getDefaultMessage();
		        errors.put(fieldName, errorMessage);
		    });
		
		    model.addAttribute(SomeConstants.EX_EX, new ExceptionResponse(  UUID.randomUUID(),
																		SomeConstants.EX_METHOD_ARGUMENT_INVALID,
																		"Think McFly, you got MethodArgumentNotValidException",
																		errors ));
			
		    return PATH_ERROR_EXCEPTION;
	}
	
	@ExceptionHandler(org.springframework.web.method.annotation.MethodArgumentTypeMismatchException.class)
	public String methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex, Model model){

		logger.severe("Launch " + SomeConstants.EX_METHOD_ARGUMENT_TYPE);
		String attrName = ex.getName();
		String typeName = "Unknown type";				
		Class<?> requiredType = ex.getRequiredType();
		
		if ( requiredType != null && requiredType.getName() != null ) typeName = requiredType.getName();
		
		model.addAttribute(SomeConstants.EX_EX, new ExceptionResponse(  UUID.randomUUID(),
																	SomeConstants.EX_METHOD_ARGUMENT_TYPE,
																	"Think McFly, you got methodArgumentTypeMismatchException",
																	Map.of( attrName, typeName) ));
		
		return PATH_ERROR_EXCEPTION;
	}	
	
	@ExceptionHandler(
		jakarta.validation.ConstraintViolationException.class
		)		
	public String jakartaValidationConstraintViolationException(ConstraintViolationException ex, Model model){

		logger.severe("Cast "+ SomeConstants.EX_JAKARTA_VAL);
		model.addAttribute(SomeConstants.EX_EX, new ExceptionResponse(  UUID.randomUUID(),
																	SomeConstants.EX_JAKARTA_VAL,
																	"Whats wrong McFly?, you Chicken got jakartaValidationConstraintViolationException",
																	Map.of( ex.getLocalizedMessage(), ex.getMessage())));
		
		return PATH_ERROR_EXCEPTION;
	}
	
	@ExceptionHandler(java.lang.NumberFormatException.class)
	public String numberFormatException(NumberFormatException ex, Model model) {
		
		logger.severe("Run "+ "NumberFormatException");
		model.addAttribute(SomeConstants.EX_EX, new ExceptionResponse(  UUID.randomUUID(),
																	"NumberFormatException",
																	"Whats wrong McFly?, you Chicken got NumberFormatException",
																	Map.of( ex.getLocalizedMessage(), ex.getMessage())));
		
		return PATH_ERROR_EXCEPTION;
	}


	@ExceptionHandler(Exception.class)
	public String generalException(Exception ex, Model model) {
		
		logger.severe("Spit "+ SomeConstants.EX_EX);
		model.addAttribute(SomeConstants.EX_EX, new ExceptionResponse(  UUID.randomUUID(),
																	SomeConstants.EX_EX,
																	"Whats wrong McFly?, you Chicken got GeneralException",
																	Map.of( ex.getLocalizedMessage(), ex.getMessage())));
		
		return PATH_ERROR_EXCEPTION;
	}

}
