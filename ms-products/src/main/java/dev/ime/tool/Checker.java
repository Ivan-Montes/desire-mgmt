package dev.ime.tool;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import dev.ime.client.impl.MsOrdersClientImpl;

@Component
public class Checker {

	private final MsOrdersClientImpl msOrdersClient;

	public Checker(MsOrdersClientImpl msOrdersClient) {
		super();
		this.msOrdersClient = msOrdersClient;
	}

    public boolean checkProductId(Long productId) {
    
		ResponseEntity<Boolean> response = msOrdersClient.getAnyByProductId(productId);
		return response.getStatusCode() == HttpStatus.OK;
		
	}
    
	
	
}
