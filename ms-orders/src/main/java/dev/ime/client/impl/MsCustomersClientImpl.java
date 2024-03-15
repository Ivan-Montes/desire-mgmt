package dev.ime.client.impl;

import java.util.function.Supplier;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import dev.ime.client.MsCustomersClient;
import dev.ime.dto.CustomerDto;
import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.decorators.Decorators;
import io.github.resilience4j.retry.Retry;
import io.vavr.control.Try;

@Component
public class MsCustomersClientImpl implements MsCustomersClient{

	private final MsCustomersClient msCustomersClient;
	
	public MsCustomersClientImpl(MsCustomersClient msCustomersClient) {
		super();
		this.msCustomersClient = msCustomersClient;
	}

	@Override
	public ResponseEntity<CustomerDto> getCustomerById(Long id) {
		
		CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults("cbDef");
		Retry retry = Retry.ofDefaults("rDef");
		Bulkhead bulkhead = Bulkhead.ofDefaults("bhDef");
		Supplier<ResponseEntity<CustomerDto>> supplier = () -> msCustomersClient.getCustomerById(id) ;

		Supplier<ResponseEntity<CustomerDto>> decoratedSupplier = Decorators.ofSupplier(supplier)
		  .withCircuitBreaker(circuitBreaker)
		  .withBulkhead(bulkhead)
		  .withRetry(retry)
		  .decorate();

		return Try.ofSupplier(decoratedSupplier).recover(t -> new ResponseEntity<>(new CustomerDto(),HttpStatus.NOT_FOUND) ).get();		
	
	}

	
}
