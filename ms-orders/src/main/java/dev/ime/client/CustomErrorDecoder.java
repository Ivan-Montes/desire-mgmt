package dev.ime.client;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import dev.ime.exception.*;
import dev.ime.tool.SomeConstants;
import feign.Response;
import feign.codec.ErrorDecoder;


public class CustomErrorDecoder implements ErrorDecoder {
		
	private final ObjectMapper objectMapper;	
	private final Logger logger;
	
	public CustomErrorDecoder(ObjectMapper objectMapper, Logger logger) {
		super();
		this.objectMapper = objectMapper;
		this.logger = logger;
	}

	@Override
	public Exception decode(String methodKey, Response response) {
		
		ExceptionResponse exResponse = null;
		
		try {
			exResponse = objectMapper.readValue(
							response.body().asInputStream(),
							ExceptionResponse.class
						);
			
			if (exResponse != null) {
				
				switch(exResponse.name()) {
				
				case SomeConstants.EX_RESOURCE_NOT_FOUND:
					return new ResourceNotFoundException(exResponse.error());
					
				case SomeConstants.EX_ENTITY_ASSOCIATED:
					return new EntityAssociatedException(exResponse.error());
					
				case SomeConstants.EX_DATE_BAD:
					return new DateBadFormatException(exResponse.error());
					
				default:
					return new BasicException(
									UUID.randomUUID(), 
									exResponse.name(), 
									exResponse.description(), 
									exResponse.error());									
				}

			}
		
		} catch (Exception e) {
			logger.severe("Throw Exception in CustomErrorDecoder");
		}		
		
		Map<String,String> errorMap = new HashMap<>();
		errorMap.put("MethodKey", methodKey);
		errorMap.put("Status",String.valueOf(response.status()));
		errorMap.put("Url", response.request().url());
		
		return new BasicException(UUID.randomUUID(), "BasicException", "", errorMap);	
		
	}

}
