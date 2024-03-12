package dev.ime.client.impl;

import java.util.function.Supplier;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import dev.ime.client.MsProductsClient;
import dev.ime.dto.ProductDto;
import io.vavr.control.Try;
import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.decorators.Decorators;
import io.github.resilience4j.retry.Retry;

@Component
public class MsProductsClientImpl implements MsProductsClient{

	private final MsProductsClient msProductsClient;
    
	public MsProductsClientImpl(MsProductsClient msProductsClient) {
		super();
		this.msProductsClient = msProductsClient;
	}
	
	@Override
	public ResponseEntity<ProductDto> getProductById(Long id) {
		
		CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults("cbDef");
		Retry retry = Retry.ofDefaults("rDef");
		Bulkhead bulkhead = Bulkhead.ofDefaults("bhDef");
		Supplier<ResponseEntity<ProductDto>> supplier = () -> msProductsClient.getProductById(id) ;

		Supplier<ResponseEntity<ProductDto>> decoratedSupplier = Decorators.ofSupplier(supplier)
		  .withCircuitBreaker(circuitBreaker)
		  .withBulkhead(bulkhead)
		  .withRetry(retry)
		  .decorate();

		return Try.ofSupplier(decoratedSupplier).recover(t -> new ResponseEntity<>(new ProductDto(),HttpStatus.NOT_FOUND) ).get();		
	}	

}
