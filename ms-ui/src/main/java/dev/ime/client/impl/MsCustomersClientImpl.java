package dev.ime.client.impl;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import dev.ime.client.MsCustomersClient;
import dev.ime.dto.AddressDto;
import dev.ime.dto.CustomerDto;
import dev.ime.tool.SomeConstants;
import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.decorators.Decorators;
import io.github.resilience4j.retry.Retry;
import io.vavr.control.Try;
import jakarta.validation.Valid;

@Component
public class MsCustomersClientImpl implements MsCustomersClient{

	private final MsCustomersClient msCustomersClient;	
	
	
	public MsCustomersClientImpl(MsCustomersClient msCustomersClient) {
		super();
		this.msCustomersClient = msCustomersClient;
	}

	@Override
	public ResponseEntity<List<CustomerDto>> getAllCustomer() {
		
		CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults(SomeConstants.R4_CB);
		Retry retry = Retry.ofDefaults(SomeConstants.R4_R);
		Bulkhead bulkhead = Bulkhead.ofDefaults(SomeConstants.R4_BH);
		Supplier<ResponseEntity<List<CustomerDto>>> supplier = msCustomersClient::getAllCustomer;

		Supplier<ResponseEntity<List<CustomerDto>>> decoratedSupplier = Decorators.ofSupplier(supplier)
		  .withCircuitBreaker(circuitBreaker)
		  .withBulkhead(bulkhead)
		  .withRetry(retry)
		  .decorate();

		return Try.ofSupplier(decoratedSupplier).recover(t -> new ResponseEntity<>(Collections.emptyList(),HttpStatus.NOT_FOUND) ).get();		
	}

	@Override
	public ResponseEntity<List<CustomerDto>> getAllCustomerPaged(Integer page, Integer size) {

		CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults(SomeConstants.R4_CB);
		Retry retry = Retry.ofDefaults(SomeConstants.R4_R);
		Bulkhead bulkhead = Bulkhead.ofDefaults(SomeConstants.R4_BH);
		Supplier<ResponseEntity<List<CustomerDto>>> supplier = () -> msCustomersClient.getAllCustomerPaged(page, size);

		Supplier<ResponseEntity<List<CustomerDto>>> decoratedSupplier = Decorators.ofSupplier(supplier)
		  .withCircuitBreaker(circuitBreaker)
		  .withBulkhead(bulkhead)
		  .withRetry(retry)
		  .decorate();

		return Try.ofSupplier(decoratedSupplier).recover(t -> new ResponseEntity<>(Collections.emptyList(),HttpStatus.NOT_FOUND) ).get();		
	}
	
	@Override
	public ResponseEntity<CustomerDto> getCustomerById(Long id) {
		
		CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults(SomeConstants.R4_CB);
		Retry retry = Retry.ofDefaults(SomeConstants.R4_R);
		Bulkhead bulkhead = Bulkhead.ofDefaults(SomeConstants.R4_BH);
		Supplier<ResponseEntity<CustomerDto>> supplier = () -> msCustomersClient.getCustomerById(id);

		Supplier<ResponseEntity<CustomerDto>> decoratedSupplier = Decorators.ofSupplier(supplier)
		  .withCircuitBreaker(circuitBreaker)
		  .withBulkhead(bulkhead)
		  .withRetry(retry)
		  .decorate();

		return Try.ofSupplier(decoratedSupplier).recover(t -> new ResponseEntity<>(new CustomerDto(), HttpStatus.NOT_FOUND) ).get();
	}

	@Override
	public ResponseEntity<CustomerDto> create(@Valid CustomerDto dto) {

		CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults(SomeConstants.R4_CB);
		Retry retry = Retry.ofDefaults(SomeConstants.R4_R);
		Bulkhead bulkhead = Bulkhead.ofDefaults(SomeConstants.R4_BH);
		Supplier<ResponseEntity<CustomerDto>> supplier = () -> msCustomersClient.create(dto);

		Supplier<ResponseEntity<CustomerDto>> decoratedSupplier = Decorators.ofSupplier(supplier)
		  .withCircuitBreaker(circuitBreaker)
		  .withBulkhead(bulkhead)
		  .withRetry(retry)
		  .decorate();

		return Try.ofSupplier(decoratedSupplier).recover(t -> new ResponseEntity<>(new CustomerDto(), HttpStatus.NOT_FOUND) ).get();
	}

	@Override
	public ResponseEntity<CustomerDto> update(Long id, @Valid CustomerDto dto) {

		CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults(SomeConstants.R4_CB);
		Retry retry = Retry.ofDefaults(SomeConstants.R4_R);
		Bulkhead bulkhead = Bulkhead.ofDefaults(SomeConstants.R4_BH);
		Supplier<ResponseEntity<CustomerDto>> supplier = () -> msCustomersClient.update(id, dto);

		Supplier<ResponseEntity<CustomerDto>> decoratedSupplier = Decorators.ofSupplier(supplier)
		  .withCircuitBreaker(circuitBreaker)
		  .withBulkhead(bulkhead)
		  .withRetry(retry)
		  .decorate();

		return Try.ofSupplier(decoratedSupplier).recover(t -> new ResponseEntity<>(new CustomerDto(), HttpStatus.NOT_FOUND) ).get();
	}

	@Override
	public ResponseEntity<Boolean> deleteCustomer(Long id) {

		CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults(SomeConstants.R4_CB);
		Retry retry = Retry.ofDefaults(SomeConstants.R4_R);
		Bulkhead bulkhead = Bulkhead.ofDefaults(SomeConstants.R4_BH);
		Supplier<ResponseEntity<Boolean>> supplier = () -> msCustomersClient.deleteCustomer(id);

		Supplier<ResponseEntity<Boolean>> decoratedSupplier = Decorators.ofSupplier(supplier)
		  .withCircuitBreaker(circuitBreaker)
		  .withBulkhead(bulkhead)
		  .withRetry(retry)
		  .decorate();

		return Try.ofSupplier(decoratedSupplier).recover(t -> new ResponseEntity<>(Boolean.FALSE, HttpStatus.NOT_FOUND) ).get();	
	}

	@Override
	public ResponseEntity<Boolean> addAddress(Long customerId, Long addressId) {

		CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults(SomeConstants.R4_CB);
		Retry retry = Retry.ofDefaults(SomeConstants.R4_R);
		Bulkhead bulkhead = Bulkhead.ofDefaults(SomeConstants.R4_BH);
		Supplier<ResponseEntity<Boolean>> supplier = () -> msCustomersClient.addAddress(customerId, addressId);

		Supplier<ResponseEntity<Boolean>> decoratedSupplier = Decorators.ofSupplier(supplier)
		  .withCircuitBreaker(circuitBreaker)
		  .withBulkhead(bulkhead)
		  .withRetry(retry)
		  .decorate();

		return Try.ofSupplier(decoratedSupplier).recover(t -> new ResponseEntity<>(Boolean.FALSE, HttpStatus.NOT_FOUND) ).get();	
	}

	@Override
	public ResponseEntity<List<AddressDto>> getAddressesInCustomer(Long customerId) {

		CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults(SomeConstants.R4_CB);
		Retry retry = Retry.ofDefaults(SomeConstants.R4_R);
		Bulkhead bulkhead = Bulkhead.ofDefaults(SomeConstants.R4_BH);
		Supplier<ResponseEntity<List<AddressDto>>> supplier = () -> msCustomersClient.getAddressesInCustomer(customerId);

		Supplier<ResponseEntity<List<AddressDto>>> decoratedSupplier = Decorators.ofSupplier(supplier)
		  .withCircuitBreaker(circuitBreaker)
		  .withBulkhead(bulkhead)
		  .withRetry(retry)
		  .decorate();

		return Try.ofSupplier(decoratedSupplier).recover(t -> new ResponseEntity<>(Collections.emptyList(),HttpStatus.NOT_FOUND) ).get();	
	}

	
	
	
	
	@Override
	public ResponseEntity<List<AddressDto>> getAllAddress() {

		CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults(SomeConstants.R4_CB);
		Retry retry = Retry.ofDefaults(SomeConstants.R4_R);
		Bulkhead bulkhead = Bulkhead.ofDefaults(SomeConstants.R4_BH);
		Supplier<ResponseEntity<List<AddressDto>>> supplier = msCustomersClient::getAllAddress;

		Supplier<ResponseEntity<List<AddressDto>>> decoratedSupplier = Decorators.ofSupplier(supplier)
		  .withCircuitBreaker(circuitBreaker)
		  .withBulkhead(bulkhead)
		  .withRetry(retry)
		  .decorate();

		return Try.ofSupplier(decoratedSupplier).recover(t -> new ResponseEntity<>(Collections.emptyList(),HttpStatus.NOT_FOUND) ).get();		
	}

	@Override
	public ResponseEntity<AddressDto> getAddressById(Long id) {

		CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults(SomeConstants.R4_CB);
		Retry retry = Retry.ofDefaults(SomeConstants.R4_R);
		Bulkhead bulkhead = Bulkhead.ofDefaults(SomeConstants.R4_BH);
		Supplier<ResponseEntity<AddressDto>> supplier = () -> msCustomersClient.getAddressById(id);

		Supplier<ResponseEntity<AddressDto>> decoratedSupplier = Decorators.ofSupplier(supplier)
		  .withCircuitBreaker(circuitBreaker)
		  .withBulkhead(bulkhead)
		  .withRetry(retry)
		  .decorate();

		return Try.ofSupplier(decoratedSupplier).recover(t -> new ResponseEntity<>(new AddressDto(), HttpStatus.NOT_FOUND) ).get();
	}


	@Override
	public ResponseEntity<List<AddressDto>> getAllAddressPaged(Integer page, Integer size) {

		CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults(SomeConstants.R4_CB);
		Retry retry = Retry.ofDefaults(SomeConstants.R4_R);
		Bulkhead bulkhead = Bulkhead.ofDefaults(SomeConstants.R4_BH);
		Supplier<ResponseEntity<List<AddressDto>>> supplier = () -> msCustomersClient.getAllAddressPaged(page, size);

		Supplier<ResponseEntity<List<AddressDto>>> decoratedSupplier = Decorators.ofSupplier(supplier)
		  .withCircuitBreaker(circuitBreaker)
		  .withBulkhead(bulkhead)
		  .withRetry(retry)
		  .decorate();

		return Try.ofSupplier(decoratedSupplier).recover(t -> new ResponseEntity<>(Collections.emptyList(),HttpStatus.NOT_FOUND) ).get();		
	}

	@Override
	public ResponseEntity<AddressDto> create(@Valid AddressDto dto) {

		CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults(SomeConstants.R4_CB);
		Retry retry = Retry.ofDefaults(SomeConstants.R4_R);
		Bulkhead bulkhead = Bulkhead.ofDefaults(SomeConstants.R4_BH);
		Supplier<ResponseEntity<AddressDto>> supplier = () -> msCustomersClient.create(dto);

		Supplier<ResponseEntity<AddressDto>> decoratedSupplier = Decorators.ofSupplier(supplier)
		  .withCircuitBreaker(circuitBreaker)
		  .withBulkhead(bulkhead)
		  .withRetry(retry)
		  .decorate();

		return Try.ofSupplier(decoratedSupplier).recover(t -> new ResponseEntity<>(new AddressDto(), HttpStatus.NOT_FOUND) ).get();
	}

	@Override
	public ResponseEntity<AddressDto> update(Long id, @Valid AddressDto dto) {

		CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults(SomeConstants.R4_CB);
		Retry retry = Retry.ofDefaults(SomeConstants.R4_R);
		Bulkhead bulkhead = Bulkhead.ofDefaults(SomeConstants.R4_BH);
		Supplier<ResponseEntity<AddressDto>> supplier = () -> msCustomersClient.update(id, dto);

		Supplier<ResponseEntity<AddressDto>> decoratedSupplier = Decorators.ofSupplier(supplier)
		  .withCircuitBreaker(circuitBreaker)
		  .withBulkhead(bulkhead)
		  .withRetry(retry)
		  .decorate();

		return Try.ofSupplier(decoratedSupplier).recover(t -> new ResponseEntity<>(new AddressDto(), HttpStatus.NOT_FOUND) ).get();
	}

	@Override
	public ResponseEntity<Boolean> deleteAddress(Long id) {

		CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults(SomeConstants.R4_CB);
		Retry retry = Retry.ofDefaults(SomeConstants.R4_R);
		Bulkhead bulkhead = Bulkhead.ofDefaults(SomeConstants.R4_BH);
		Supplier<ResponseEntity<Boolean>> supplier = () -> msCustomersClient.deleteAddress(id);

		Supplier<ResponseEntity<Boolean>> decoratedSupplier = Decorators.ofSupplier(supplier)
		  .withCircuitBreaker(circuitBreaker)
		  .withBulkhead(bulkhead)
		  .withRetry(retry)
		  .decorate();

		return Try.ofSupplier(decoratedSupplier).recover(t -> new ResponseEntity<>(Boolean.FALSE, HttpStatus.NOT_FOUND) ).get();	
	}

	@Override
	public ResponseEntity<Boolean> setCustomer(Long addressId, Long customerId) {

		CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults(SomeConstants.R4_CB);
		Retry retry = Retry.ofDefaults(SomeConstants.R4_R);
		Bulkhead bulkhead = Bulkhead.ofDefaults(SomeConstants.R4_BH);
		Supplier<ResponseEntity<Boolean>> supplier = () -> msCustomersClient.setCustomer(addressId, customerId);

		Supplier<ResponseEntity<Boolean>> decoratedSupplier = Decorators.ofSupplier(supplier)
		  .withCircuitBreaker(circuitBreaker)
		  .withBulkhead(bulkhead)
		  .withRetry(retry)
		  .decorate();

		return Try.ofSupplier(decoratedSupplier).recover(t -> new ResponseEntity<>(Boolean.FALSE, HttpStatus.NOT_FOUND) ).get();	
	}

}
