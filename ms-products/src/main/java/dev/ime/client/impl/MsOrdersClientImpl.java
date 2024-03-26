package dev.ime.client.impl;

import java.util.function.Supplier;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import dev.ime.client.MsOrdersClient;
import dev.ime.tool.SomeConstants;
import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.decorators.Decorators;
import io.github.resilience4j.retry.Retry;
import io.vavr.control.Try;

@Component
public class MsOrdersClientImpl implements MsOrdersClient{

	private final MsOrdersClient msOrdersClient;	
	
	public MsOrdersClientImpl(MsOrdersClient msOrdersClient) {
		super();
		this.msOrdersClient = msOrdersClient;
	}

	@Override
	public ResponseEntity<Boolean> getAnyByProductId(Long productId) {

		CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults(SomeConstants.R4_CB);
		Retry retry = Retry.ofDefaults(SomeConstants.R4_R);
		Bulkhead bulkhead = Bulkhead.ofDefaults(SomeConstants.R4_BH);
		Supplier<ResponseEntity<Boolean>> supplier = () -> msOrdersClient.getAnyByProductId(productId) ;

		Supplier<ResponseEntity<Boolean>> decoratedSupplier = Decorators.ofSupplier(supplier)
		  .withCircuitBreaker(circuitBreaker)
		  .withBulkhead(bulkhead)
		  .withRetry(retry)
		  .decorate();

		return Try.ofSupplier(decoratedSupplier).recover(t -> new ResponseEntity<>(Boolean.FALSE, HttpStatus.NOT_FOUND) ).get();	
	}

}
