package dev.ime.client.impl;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import dev.ime.client.MsOrdersClient;
import dev.ime.dto.OrderDetailDto;
import dev.ime.dto.OrderDto;
import dev.ime.tool.SomeConstants;
import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.decorators.Decorators;
import io.github.resilience4j.retry.Retry;
import io.vavr.control.Try;
import jakarta.validation.Valid;

@Component
public class MsOrdersClientImpl implements MsOrdersClient{

	private final MsOrdersClient msOrdersClient;	
	
	public MsOrdersClientImpl(MsOrdersClient msOrdersClient) {
		super();
		this.msOrdersClient = msOrdersClient;
	}

	@Override
	public ResponseEntity<List<OrderDto>> getAllOrder() {
		
		CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults(SomeConstants.R4_CB);
		Retry retry = Retry.ofDefaults(SomeConstants.R4_R);
		Bulkhead bulkhead = Bulkhead.ofDefaults(SomeConstants.R4_BH);
		Supplier<ResponseEntity<List<OrderDto>>> supplier = msOrdersClient::getAllOrder;
		
		Supplier<ResponseEntity<List<OrderDto>>> decoratedSupplier = Decorators.ofSupplier(supplier)
				  .withCircuitBreaker(circuitBreaker)
				  .withBulkhead(bulkhead)
				  .withRetry(retry)
				  .decorate();
		
		return Try.ofSupplier(decoratedSupplier).recover(t -> new ResponseEntity<>(Collections.emptyList(),HttpStatus.NOT_FOUND) ).get();
	}

	@Override
	public ResponseEntity<List<OrderDto>> getAllOrderPaged(Integer page, Integer size) {

		CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults(SomeConstants.R4_CB);
		Retry retry = Retry.ofDefaults(SomeConstants.R4_R);
		Bulkhead bulkhead = Bulkhead.ofDefaults(SomeConstants.R4_BH);
		Supplier<ResponseEntity<List<OrderDto>>> supplier = () -> msOrdersClient.getAllOrderPaged(page, size);
		
		Supplier<ResponseEntity<List<OrderDto>>> decoratedSupplier = Decorators.ofSupplier(supplier)
				  .withCircuitBreaker(circuitBreaker)
				  .withBulkhead(bulkhead)
				  .withRetry(retry)
				  .decorate();
		
		return Try.ofSupplier(decoratedSupplier).recover(t -> new ResponseEntity<>(Collections.emptyList(),HttpStatus.NOT_FOUND) ).get();
	}
	
	@Override
	public ResponseEntity<OrderDto> getOrderById(Long id) {
		
		CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults(SomeConstants.R4_CB);
		Retry retry = Retry.ofDefaults(SomeConstants.R4_R);
		Bulkhead bulkhead = Bulkhead.ofDefaults(SomeConstants.R4_BH);
		Supplier<ResponseEntity<OrderDto>> supplier = () -> msOrdersClient.getOrderById(id);

		Supplier<ResponseEntity<OrderDto>> decoratedSupplier = Decorators.ofSupplier(supplier)
		  .withCircuitBreaker(circuitBreaker)
		  .withBulkhead(bulkhead)
		  .withRetry(retry)
		  .decorate();

		return Try.ofSupplier(decoratedSupplier).recover(t -> new ResponseEntity<>(new OrderDto(), HttpStatus.NOT_FOUND) ).get();	
	}

	@Override
	public ResponseEntity<OrderDto> create(@Valid OrderDto dto) {

		CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults(SomeConstants.R4_CB);
		Retry retry = Retry.ofDefaults(SomeConstants.R4_R);
		Bulkhead bulkhead = Bulkhead.ofDefaults(SomeConstants.R4_BH);
		Supplier<ResponseEntity<OrderDto>> supplier = () -> msOrdersClient.create(dto);

		Supplier<ResponseEntity<OrderDto>> decoratedSupplier = Decorators.ofSupplier(supplier)
		  .withCircuitBreaker(circuitBreaker)
		  .withBulkhead(bulkhead)
		  .withRetry(retry)
		  .decorate();

		return Try.ofSupplier(decoratedSupplier).recover(t -> new ResponseEntity<>(new OrderDto(), HttpStatus.NOT_FOUND) ).get();	
	}

	@Override
	public ResponseEntity<OrderDto> update(Long id, @Valid OrderDto dto) {
		
		CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults(SomeConstants.R4_CB);
		Retry retry = Retry.ofDefaults(SomeConstants.R4_R);
		Bulkhead bulkhead = Bulkhead.ofDefaults(SomeConstants.R4_BH);
		Supplier<ResponseEntity<OrderDto>> supplier = () -> msOrdersClient.update(id, dto);

		Supplier<ResponseEntity<OrderDto>> decoratedSupplier = Decorators.ofSupplier(supplier)
		  .withCircuitBreaker(circuitBreaker)
		  .withBulkhead(bulkhead)
		  .withRetry(retry)
		  .decorate();

		return Try.ofSupplier(decoratedSupplier).recover(t -> new ResponseEntity<>(new OrderDto(), HttpStatus.NOT_FOUND) ).get();	
	}

	@Override
	public ResponseEntity<Boolean> deleteOrder(Long id) {

		CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults(SomeConstants.R4_CB);
		Retry retry = Retry.ofDefaults(SomeConstants.R4_R);
		Bulkhead bulkhead = Bulkhead.ofDefaults(SomeConstants.R4_BH);
		Supplier<ResponseEntity<Boolean>> supplier = () -> msOrdersClient.deleteOrder(id);

		Supplier<ResponseEntity<Boolean>> decoratedSupplier = Decorators.ofSupplier(supplier)
		  .withCircuitBreaker(circuitBreaker)
		  .withBulkhead(bulkhead)
		  .withRetry(retry)
		  .decorate();

		return Try.ofSupplier(decoratedSupplier).recover(t -> new ResponseEntity<>(Boolean.FALSE, HttpStatus.NOT_FOUND) ).get();
	}

	@Override
	public ResponseEntity<Set<OrderDetailDto>> getLinesByOrderId(Long orderId) {

		CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults(SomeConstants.R4_CB);
		Retry retry = Retry.ofDefaults(SomeConstants.R4_R);
		Bulkhead bulkhead = Bulkhead.ofDefaults(SomeConstants.R4_BH);
		Supplier<ResponseEntity<Set<OrderDetailDto>>> supplier = () -> msOrdersClient.getLinesByOrderId(orderId);
		
		Supplier<ResponseEntity<Set<OrderDetailDto>>> decoratedSupplier = Decorators.ofSupplier(supplier)
				  .withCircuitBreaker(circuitBreaker)
				  .withBulkhead(bulkhead)
				  .withRetry(retry)
				  .decorate();
		
		return Try.ofSupplier(decoratedSupplier).recover(t -> new ResponseEntity<>(Collections.emptySet(),HttpStatus.NOT_FOUND) ).get();
	}

	@Override
	public ResponseEntity<Boolean> addOrderDetail(Long orderId, Long orderDetailId) {

		CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults(SomeConstants.R4_CB);
		Retry retry = Retry.ofDefaults(SomeConstants.R4_R);
		Bulkhead bulkhead = Bulkhead.ofDefaults(SomeConstants.R4_BH);
		Supplier<ResponseEntity<Boolean>> supplier = () -> msOrdersClient.addOrderDetail(orderId, orderDetailId);

		Supplier<ResponseEntity<Boolean>> decoratedSupplier = Decorators.ofSupplier(supplier)
		  .withCircuitBreaker(circuitBreaker)
		  .withBulkhead(bulkhead)
		  .withRetry(retry)
		  .decorate();

		return Try.ofSupplier(decoratedSupplier).recover(t -> new ResponseEntity<>(Boolean.FALSE, HttpStatus.NOT_FOUND) ).get();
	}

	@Override
	public ResponseEntity<List<OrderDetailDto>> getAllOrderDetail() {

		CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults(SomeConstants.R4_CB);
		Retry retry = Retry.ofDefaults(SomeConstants.R4_R);
		Bulkhead bulkhead = Bulkhead.ofDefaults(SomeConstants.R4_BH);
		Supplier<ResponseEntity<List<OrderDetailDto>>> supplier = msOrdersClient::getAllOrderDetail;
		
		Supplier<ResponseEntity<List<OrderDetailDto>>> decoratedSupplier = Decorators.ofSupplier(supplier)
				  .withCircuitBreaker(circuitBreaker)
				  .withBulkhead(bulkhead)
				  .withRetry(retry)
				  .decorate();
		
		return Try.ofSupplier(decoratedSupplier).recover(t -> new ResponseEntity<>(Collections.emptyList(),HttpStatus.NOT_FOUND) ).get();
	}

	@Override
	public ResponseEntity<List<OrderDetailDto>> getAllOrderDetailPaged(Integer page, Integer size) {

		CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults(SomeConstants.R4_CB);
		Retry retry = Retry.ofDefaults(SomeConstants.R4_R);
		Bulkhead bulkhead = Bulkhead.ofDefaults(SomeConstants.R4_BH);
		Supplier<ResponseEntity<List<OrderDetailDto>>> supplier = () -> msOrdersClient.getAllOrderDetailPaged(page, size);
		
		Supplier<ResponseEntity<List<OrderDetailDto>>> decoratedSupplier = Decorators.ofSupplier(supplier)
				  .withCircuitBreaker(circuitBreaker)
				  .withBulkhead(bulkhead)
				  .withRetry(retry)
				  .decorate();
		
		return Try.ofSupplier(decoratedSupplier).recover(t -> new ResponseEntity<>(Collections.emptyList(),HttpStatus.NOT_FOUND) ).get();
	}

	@Override
	public ResponseEntity<OrderDetailDto> getOrderDetailById(Long id) {

		CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults(SomeConstants.R4_CB);
		Retry retry = Retry.ofDefaults(SomeConstants.R4_R);
		Bulkhead bulkhead = Bulkhead.ofDefaults(SomeConstants.R4_BH);
		Supplier<ResponseEntity<OrderDetailDto>> supplier = () -> msOrdersClient.getOrderDetailById(id);

		Supplier<ResponseEntity<OrderDetailDto>> decoratedSupplier = Decorators.ofSupplier(supplier)
		  .withCircuitBreaker(circuitBreaker)
		  .withBulkhead(bulkhead)
		  .withRetry(retry)
		  .decorate();

		return Try.ofSupplier(decoratedSupplier).recover(t -> new ResponseEntity<>(new OrderDetailDto(), HttpStatus.NOT_FOUND) ).get();	
	}

	@Override
	public ResponseEntity<OrderDetailDto> create(@Valid OrderDetailDto dto) {

		CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults(SomeConstants.R4_CB);
		Retry retry = Retry.ofDefaults(SomeConstants.R4_R);
		Bulkhead bulkhead = Bulkhead.ofDefaults(SomeConstants.R4_BH);
		Supplier<ResponseEntity<OrderDetailDto>> supplier = () -> msOrdersClient.create(dto);

		Supplier<ResponseEntity<OrderDetailDto>> decoratedSupplier = Decorators.ofSupplier(supplier)
		  .withCircuitBreaker(circuitBreaker)
		  .withBulkhead(bulkhead)
		  .withRetry(retry)
		  .decorate();

		return Try.ofSupplier(decoratedSupplier).recover(t -> new ResponseEntity<>(new OrderDetailDto(), HttpStatus.NOT_FOUND) ).get();	
	}

	@Override
	public ResponseEntity<OrderDetailDto> update(Long id, @Valid OrderDetailDto dto) {

		CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults(SomeConstants.R4_CB);
		Retry retry = Retry.ofDefaults(SomeConstants.R4_R);
		Bulkhead bulkhead = Bulkhead.ofDefaults(SomeConstants.R4_BH);
		Supplier<ResponseEntity<OrderDetailDto>> supplier = () -> msOrdersClient.update(id, dto);

		Supplier<ResponseEntity<OrderDetailDto>> decoratedSupplier = Decorators.ofSupplier(supplier)
		  .withCircuitBreaker(circuitBreaker)
		  .withBulkhead(bulkhead)
		  .withRetry(retry)
		  .decorate();

		return Try.ofSupplier(decoratedSupplier).recover(t -> new ResponseEntity<>(new OrderDetailDto(), HttpStatus.NOT_FOUND) ).get();	
	}

	@Override
	public ResponseEntity<Boolean> deleteOrderDetail(Long id) {
		
		CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults(SomeConstants.R4_CB);
		Retry retry = Retry.ofDefaults(SomeConstants.R4_R);
		Bulkhead bulkhead = Bulkhead.ofDefaults(SomeConstants.R4_BH);
		Supplier<ResponseEntity<Boolean>> supplier = () -> msOrdersClient.deleteOrderDetail( id );

		Supplier<ResponseEntity<Boolean>> decoratedSupplier = Decorators.ofSupplier(supplier)
		  .withCircuitBreaker(circuitBreaker)
		  .withBulkhead(bulkhead)
		  .withRetry(retry)
		  .decorate();

		return Try.ofSupplier(decoratedSupplier).recover(t -> new ResponseEntity<>(Boolean.FALSE, HttpStatus.NOT_FOUND) ).get();
	}

	@Override
	public ResponseEntity<Boolean> setOrder(Long orderDetailId, Long orderId) {

		CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults(SomeConstants.R4_CB);
		Retry retry = Retry.ofDefaults(SomeConstants.R4_R);
		Bulkhead bulkhead = Bulkhead.ofDefaults(SomeConstants.R4_BH);
		Supplier<ResponseEntity<Boolean>> supplier = () -> msOrdersClient.setOrder( orderDetailId, orderId );

		Supplier<ResponseEntity<Boolean>> decoratedSupplier = Decorators.ofSupplier(supplier)
		  .withCircuitBreaker(circuitBreaker)
		  .withBulkhead(bulkhead)
		  .withRetry(retry)
		  .decorate();

		return Try.ofSupplier(decoratedSupplier).recover(t -> new ResponseEntity<>(Boolean.FALSE, HttpStatus.NOT_FOUND) ).get();
	}

	@Override
	public ResponseEntity<Boolean> getAnyByProductId(Long productId) {

		CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults(SomeConstants.R4_CB);
		Retry retry = Retry.ofDefaults(SomeConstants.R4_R);
		Bulkhead bulkhead = Bulkhead.ofDefaults(SomeConstants.R4_BH);
		Supplier<ResponseEntity<Boolean>> supplier = () -> msOrdersClient.getAnyByProductId( productId );

		Supplier<ResponseEntity<Boolean>> decoratedSupplier = Decorators.ofSupplier(supplier)
		  .withCircuitBreaker(circuitBreaker)
		  .withBulkhead(bulkhead)
		  .withRetry(retry)
		  .decorate();

		return Try.ofSupplier(decoratedSupplier).recover(t -> new ResponseEntity<>(Boolean.FALSE, HttpStatus.NOT_FOUND) ).get();
	}

}
