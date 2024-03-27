package dev.ime.tool;

import java.util.Optional;

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

    public boolean checkGetAnyByProductId(Long productId) {
    
		Optional<Boolean> optionalDto = Optional.ofNullable(msOrdersClient.getAnyByProductId(productId))
			    .filter(response -> response.getStatusCode() == HttpStatus.OK)
			    .map(ResponseEntity::getBody);

		return optionalDto.orElse(false);
		
	}
    
	
	
}
