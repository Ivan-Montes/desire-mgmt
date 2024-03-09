package dev.ime.client.impl;

import java.util.function.Supplier;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import dev.ime.client.MsProductsClient;
import dev.ime.dto.ProductDto;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.vavr.control.Try;

@Component
public class MsProductsClientImpl implements MsProductsClient{

	private final MsProductsClient msProductsClient;

	public MsProductsClientImpl(MsProductsClient msProductsClient) {
		super();
		this.msProductsClient = msProductsClient;
	}
	
	@Override
	public ResponseEntity<ProductDto> getProductById(Long productId) {
		
		CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults("defaultName");
    	
    	Supplier<ResponseEntity<ProductDto>> decoratedSupplier = CircuitBreaker
    		    .decorateSupplier(circuitBreaker, ()-> msProductsClient.getProductById(productId));

    	return Try.ofSupplier(decoratedSupplier)
    		    .recover(throwable -> new ResponseEntity<>(new ProductDto(),HttpStatus.NOT_FOUND))
    		    .get();
	}

}
