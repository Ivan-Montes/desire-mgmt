package dev.ime.exception;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import jakarta.validation.ConstraintViolationException;

class GlobalExceptionHandlerTest {

	private GlobalExceptionHandler globalExceptionHandler;
	private final UUID uuid = UUID.randomUUID();
	private final String name = "Our Exception";
	private final String description = "Our Exception, born and raised here";
	private Map<String, String> errors;
	private Logger logger = Mockito.mock(Logger.class);
	
	@BeforeEach
	private void createObjects() {
		
		globalExceptionHandler = new GlobalExceptionHandler(logger);
		errors = new HashMap<String, String>();
	}
	
	@Test
	void GlobalExceptionHandler_basicException_ReturnResponseEntity() {
		
		BasicException ex = new BasicException(uuid, name, description, errors);
		
		ResponseEntity<ExceptionResponse>responseEntity = globalExceptionHandler.basicException(ex);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(responseEntity).isNotNull(),
				()-> Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND)
				);		
	}
	

	@Test
	void GlobalExceptionHandler_methodArgumentNotValidException_ReturnResponseEntity() {
		
		FieldError fieldError = new FieldError("objectName", name, null, false, null, null, description);
		List<ObjectError>list = new ArrayList<>();
		list.add(fieldError);
		MethodArgumentNotValidException ex = Mockito.mock(MethodArgumentNotValidException.class);
		BindingResult bindingResult = Mockito.mock(BindingResult.class);
		Mockito.when(ex.getBindingResult()).thenReturn(bindingResult);
		Mockito.when(bindingResult.getAllErrors()).thenReturn(list);
		
		ResponseEntity<ExceptionResponse>responseEntity = globalExceptionHandler.methodArgumentNotValidException(ex);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(responseEntity).isNotNull(),
				()-> Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_ACCEPTABLE)
				);	
	}
	
	@Test
	void GlobalExceptionHandler_methodArgumentTypeMismatchException_ReturnResponseEntity() {
		
		MethodArgumentTypeMismatchException ex = new MethodArgumentTypeMismatchException(null, null, name, null, new Exception());
		
		ResponseEntity<ExceptionResponse>responseEntity = globalExceptionHandler.methodArgumentTypeMismatchException(ex);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(responseEntity).isNotNull(),
				()-> Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST)
				);			
	}

	@Test
	void GlobalExceptionHandler_jakartaValidationConstraintViolationExceptionReturnResponseEntity() {
		
	ConstraintViolationException ex = Mockito.mock(ConstraintViolationException.class);
	Mockito.when(ex.getLocalizedMessage()).thenReturn(name);
	Mockito.when(ex.getMessage()).thenReturn(description);
	
	ResponseEntity<ExceptionResponse>responseEntity = globalExceptionHandler.jakartaValidationConstraintViolationException(ex);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(responseEntity).isNotNull(),
				()-> Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST)
				);	
	}
	

}
