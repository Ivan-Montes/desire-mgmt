package dev.ime.client;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.ime.exceptionresponse.ExceptionResponse;
import dev.ime.tool.SomeConstants;
import feign.Request;
import feign.Response;
import feign.Response.Body;


@ExtendWith(MockitoExtension.class)
class CustomErrorDecoderTest {

	@Mock
	private  ObjectMapper objectMapper;	
	
	@Mock
	private  Logger logger;
	
	@InjectMocks
	private CustomErrorDecoder customErrorDecoder;
	
	@Mock
	private Request request;
	
	@Mock
	private	Response response;
	
	private ExceptionResponse exResponse;
	private final UUID uuid = UUID.randomUUID();
	private final String name = "Our Exception";
	private final String description = "Our Exception, born and raised here";
	private Map<String, String> errors;
	private String initialString = "text";
	private InputStream targetStream;
	
	@BeforeEach
	private void createObjects() {		
		
		exResponse = new ExceptionResponse(uuid, name, description, errors);
	    targetStream = new ByteArrayInputStream(initialString.getBytes());
	}	
	
	@SuppressWarnings("unchecked")
	@Test
	void CustomErrorDecoder_decode_ReturnBasicExceptionAfterCatch() throws StreamReadException, DatabindException, IOException {		

		Body body = Mockito.mock(Body.class);		
		Mockito.when(body.asInputStream()).thenReturn(null);
		Mockito.when(objectMapper.readValue(Mockito.any(InputStream.class), Mockito.any(Class.class))).thenReturn(exResponse);
		Mockito.when(response.body()).thenReturn(body);
		Mockito.when(response.request()).thenReturn(request);
		Mockito.when(request.url()).thenReturn("http://");
		
		Exception ex = customErrorDecoder.decode("Ex", response);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull()
				);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	void CustomErrorDecoder_decode_ReturnBasicExceptionByNullResponse() throws StreamReadException, DatabindException, IOException {
		

		Body body = Mockito.mock(Body.class);	
		Mockito.when(objectMapper.readValue(Mockito.any(InputStream.class), Mockito.any(Class.class))).thenReturn(null);
		Mockito.when(response.body()).thenReturn(body);	
		Mockito.when(body.asInputStream()).thenReturn(targetStream);
		Mockito.when(response.body()).thenReturn(body);
		Mockito.when(response.request()).thenReturn(request);
		Mockito.when(request.url()).thenReturn("http://web.com");
		
		Exception ex = customErrorDecoder.decode("Ex", response);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull()
				);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	void CustomErrorDecoder_decode_ReturnBasicException() throws StreamReadException, DatabindException, IOException {
		
		
		Body body = Mockito.mock(Body.class);	
		Mockito.when(objectMapper.readValue(Mockito.any(InputStream.class), Mockito.any(Class.class))).thenReturn(exResponse);
		Mockito.when(response.body()).thenReturn(body);	
		Mockito.when(body.asInputStream()).thenReturn(targetStream);
		
		Exception ex = customErrorDecoder.decode("Ex", response);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull()
				);
	}

	@SuppressWarnings("unchecked")
	@Test
	void CustomErrorDecoder_decode_ReturnResourceNotFoundExcepetion() throws StreamReadException, DatabindException, IOException {
		
		exResponse = new ExceptionResponse(uuid, SomeConstants.EX_RESOURCE_NOT_FOUND, description, errors);
		Body body = Mockito.mock(Body.class);	
		Mockito.when(objectMapper.readValue(Mockito.any(InputStream.class), Mockito.any(Class.class))).thenReturn(exResponse);
		Mockito.when(response.body()).thenReturn(body);	
		Mockito.when(body.asInputStream()).thenReturn(targetStream);
		
		Exception ex = customErrorDecoder.decode("Ex", response);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull()
				);
	}

	@SuppressWarnings("unchecked")
	@Test
	void CustomErrorDecoder_decode_ReturnEntityExcepetion() throws StreamReadException, DatabindException, IOException {
		
		exResponse = new ExceptionResponse(uuid, SomeConstants.EX_ENTITY_ASSOCIATED, description, errors);
		Body body = Mockito.mock(Body.class);	
		Mockito.when(objectMapper.readValue(Mockito.any(InputStream.class), Mockito.any(Class.class))).thenReturn(exResponse);
		Mockito.when(response.body()).thenReturn(body);	
		Mockito.when(body.asInputStream()).thenReturn(targetStream);
		
		Exception ex = customErrorDecoder.decode("Ex", response);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull()
				);
	}

	@SuppressWarnings("unchecked")
	@Test
	void CustomErrorDecoder_decode_ReturnDateExcepetion() throws StreamReadException, DatabindException, IOException {
		
		exResponse = new ExceptionResponse(uuid, SomeConstants.EX_DATE_BAD, description, errors);
		Body body = Mockito.mock(Body.class);	
		Mockito.when(objectMapper.readValue(Mockito.any(InputStream.class), Mockito.any(Class.class))).thenReturn(exResponse);
		Mockito.when(response.body()).thenReturn(body);	
		Mockito.when(body.asInputStream()).thenReturn(targetStream);
		
		Exception ex = customErrorDecoder.decode("Ex", response);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull()
				);
	}
	
}
