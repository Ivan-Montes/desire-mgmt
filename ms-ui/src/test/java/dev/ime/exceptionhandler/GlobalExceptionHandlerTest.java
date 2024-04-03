package dev.ime.exceptionhandler;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import dev.ime.exception.BasicException;
import jakarta.validation.ConstraintViolationException;


@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {
	
	private GlobalExceptionHandler globalExceptionHandler;
	private final UUID uuid = UUID.randomUUID();
	private final String name = "Our Exception";
	private final String description = "Our Exception, born and raised here";
	private Map<String, String> errors;
	private Logger logger = Mockito.mock(Logger.class);
	private static final String PATH_ERROR_EXCEPTION = "error/exception";
	private Model model;

	@BeforeEach
	private void createObjects() {
		
		globalExceptionHandler = new GlobalExceptionHandler(logger);
		errors = new HashMap<String, String>();
		model = Mockito.mock(Model.class);
	}

	@Test
	void GlobalExceptionHandler_basicException_ReturnStringView() {
		
		BasicException ex = new BasicException(uuid, name, description, errors);
		
		String stringView = globalExceptionHandler.basicException(ex, model);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(stringView).isNotNull(),
				()-> Assertions.assertThat(stringView).isEqualTo(PATH_ERROR_EXCEPTION)
				);		
	}

	@Test
	void GlobalExceptionHandler_methodArgumentNotValidException_ReturnStringView() {
		
		FieldError fieldError = new FieldError("objectName", name, null, false, null, null, description);
		List<ObjectError>list = new ArrayList<>();
		list.add(fieldError);
		MethodArgumentNotValidException ex = Mockito.mock(MethodArgumentNotValidException.class);
		BindingResult bindingResult = Mockito.mock(BindingResult.class);
		Mockito.when(ex.getBindingResult()).thenReturn(bindingResult);
		Mockito.when(bindingResult.getAllErrors()).thenReturn(list);
		
		String stringView = globalExceptionHandler.methodArgumentNotValidException(ex, model);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(stringView).isNotNull(),
				()-> Assertions.assertThat(stringView).isEqualTo(PATH_ERROR_EXCEPTION)
				);	
	}

	@Test
	void GlobalExceptionHandler_methodArgumentTypeMismatchException_ReturnStringView() {
		
		MethodArgumentTypeMismatchException ex = new MethodArgumentTypeMismatchException(null, null, name, null, new Exception());
		
		String stringView = globalExceptionHandler.methodArgumentTypeMismatchException(ex, model);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(stringView).isNotNull(),
				()-> Assertions.assertThat(stringView).isEqualTo(PATH_ERROR_EXCEPTION)
				);			
	}

	@Test
	void GlobalExceptionHandler_jakartaValidationConstraintViolationException_ReturnStringView() {
		
		ConstraintViolationException ex = Mockito.mock(ConstraintViolationException.class);
		Mockito.when(ex.getLocalizedMessage()).thenReturn(name);
		Mockito.when(ex.getMessage()).thenReturn(description);
		
		String stringView = globalExceptionHandler.jakartaValidationConstraintViolationException(ex, model);
			
			org.junit.jupiter.api.Assertions.assertAll(
					()-> Assertions.assertThat(stringView).isNotNull(),
					()-> Assertions.assertThat(stringView).isEqualTo(PATH_ERROR_EXCEPTION)
					);	
	}

	@Test
	void GlobalExceptionHandler_numberFormatException_ReturnStringView() {
			
		NumberFormatException ex = Mockito.mock(NumberFormatException.class);
		Mockito.when(ex.getLocalizedMessage()).thenReturn(name);
		Mockito.when(ex.getMessage()).thenReturn(description);
		
		String stringView = globalExceptionHandler.numberFormatException(ex, model);
			
			org.junit.jupiter.api.Assertions.assertAll(
					()-> Assertions.assertThat(stringView).isNotNull(),
					()-> Assertions.assertThat(stringView).isEqualTo(PATH_ERROR_EXCEPTION)
					);	
	}

	@Test
	void GlobalExceptionHandler_generalException_ReturnStringView() {
		
		Exception ex = Mockito.mock(Exception.class);
		Mockito.when(ex.getLocalizedMessage()).thenReturn(name);
		Mockito.when(ex.getMessage()).thenReturn(description);
		
		String stringView = globalExceptionHandler.generalException(ex, model);
			
			org.junit.jupiter.api.Assertions.assertAll(
					()-> Assertions.assertThat(stringView).isNotNull(),
					()-> Assertions.assertThat(stringView).isEqualTo(PATH_ERROR_EXCEPTION)
					);	
	}

}
