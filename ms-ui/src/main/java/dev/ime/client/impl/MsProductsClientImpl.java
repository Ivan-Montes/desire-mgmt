package dev.ime.client.impl;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import dev.ime.client.MsProductsClient;
import dev.ime.dto.CategoryDto;
import dev.ime.dto.ProductDto;
import dev.ime.tool.SomeConstants;
import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.decorators.Decorators;
import io.github.resilience4j.retry.Retry;
import io.vavr.control.Try;
import jakarta.validation.Valid;

@Component
public class MsProductsClientImpl implements MsProductsClient{

	private final MsProductsClient msProductsClient;
    
	public MsProductsClientImpl(MsProductsClient msProductsClient) {
		super();
		this.msProductsClient = msProductsClient;
	}	
	
	@Override
	public ResponseEntity<List<ProductDto>> getAllProduct() {

		CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults(SomeConstants.R4_CB);
		Retry retry = Retry.ofDefaults(SomeConstants.R4_R);
		Bulkhead bulkhead = Bulkhead.ofDefaults(SomeConstants.R4_BH);
		Supplier<ResponseEntity<List<ProductDto>>> supplier = msProductsClient::getAllProduct;

		Supplier<ResponseEntity<List<ProductDto>>> decoratedSupplier = Decorators.ofSupplier(supplier)
		  .withCircuitBreaker(circuitBreaker)
		  .withBulkhead(bulkhead)
		  .withRetry(retry)
		  .decorate();

		return Try.ofSupplier(decoratedSupplier).recover(t -> new ResponseEntity<>(Collections.emptyList(), HttpStatus.NOT_FOUND) ).get();		
	
	}

	@Override
	public ResponseEntity<List<ProductDto>> getAllProductPaged(Integer page, Integer size) {
		
		CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults(SomeConstants.R4_CB);
		Retry retry = Retry.ofDefaults(SomeConstants.R4_R);
		Bulkhead bulkhead = Bulkhead.ofDefaults(SomeConstants.R4_BH);
		Supplier<ResponseEntity<List<ProductDto>>> supplier = () -> msProductsClient.getAllProductPaged(page, size);

		Supplier<ResponseEntity<List<ProductDto>>> decoratedSupplier = Decorators.ofSupplier(supplier)
		  .withCircuitBreaker(circuitBreaker)
		  .withBulkhead(bulkhead)
		  .withRetry(retry)
		  .decorate();

		return Try.ofSupplier(decoratedSupplier).recover(t -> new ResponseEntity<>(Collections.emptyList(), HttpStatus.NOT_FOUND) ).get();		
	
	}

	@Override
	public ResponseEntity<ProductDto> getProductById(Long id) {
		
		CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults(SomeConstants.R4_CB);
		Retry retry = Retry.ofDefaults(SomeConstants.R4_R);
		Bulkhead bulkhead = Bulkhead.ofDefaults(SomeConstants.R4_BH);
		Supplier<ResponseEntity<ProductDto>> supplier = () -> msProductsClient.getProductById(id);

		Supplier<ResponseEntity<ProductDto>> decoratedSupplier = Decorators.ofSupplier(supplier)
		  .withCircuitBreaker(circuitBreaker)
		  .withBulkhead(bulkhead)
		  .withRetry(retry)
		  .decorate();

		return Try.ofSupplier(decoratedSupplier).recover(t -> new ResponseEntity<>(new ProductDto(), HttpStatus.NOT_FOUND) ).get();		
	
	}

	@Override
	public ResponseEntity<ProductDto> create(@Valid ProductDto dto) {

		CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults(SomeConstants.R4_CB);
		Retry retry = Retry.ofDefaults(SomeConstants.R4_R);
		Bulkhead bulkhead = Bulkhead.ofDefaults(SomeConstants.R4_BH);
		Supplier<ResponseEntity<ProductDto>> supplier = () -> msProductsClient.create(dto);

		Supplier<ResponseEntity<ProductDto>> decoratedSupplier = Decorators.ofSupplier(supplier)
		  .withCircuitBreaker(circuitBreaker)
		  .withBulkhead(bulkhead)
		  .withRetry(retry)
		  .decorate();

		return Try.ofSupplier(decoratedSupplier).recover(t -> new ResponseEntity<>(new ProductDto(), HttpStatus.NOT_FOUND) ).get();		
	
	}

	@Override
	public ResponseEntity<ProductDto> update(Long id, @Valid ProductDto dto) {
		
		CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults(SomeConstants.R4_CB);
		Retry retry = Retry.ofDefaults(SomeConstants.R4_R);
		Bulkhead bulkhead = Bulkhead.ofDefaults(SomeConstants.R4_BH);
		Supplier<ResponseEntity<ProductDto>> supplier = () -> msProductsClient.update(id, dto);

		Supplier<ResponseEntity<ProductDto>> decoratedSupplier = Decorators.ofSupplier(supplier)
		  .withCircuitBreaker(circuitBreaker)
		  .withBulkhead(bulkhead)
		  .withRetry(retry)
		  .decorate();

		return Try.ofSupplier(decoratedSupplier).recover(t -> new ResponseEntity<>(new ProductDto(), HttpStatus.NOT_FOUND) ).get();		
	}

	@Override
	public ResponseEntity<Boolean> deleteProduct(Long id) {

		CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults(SomeConstants.R4_CB);
		Retry retry = Retry.ofDefaults(SomeConstants.R4_R);
		Bulkhead bulkhead = Bulkhead.ofDefaults(SomeConstants.R4_BH);
		Supplier<ResponseEntity<Boolean>> supplier = () -> msProductsClient.deleteProduct(id);

		Supplier<ResponseEntity<Boolean>> decoratedSupplier = Decorators.ofSupplier(supplier)
		  .withCircuitBreaker(circuitBreaker)
		  .withBulkhead(bulkhead)
		  .withRetry(retry)
		  .decorate();

		return Try.ofSupplier(decoratedSupplier).recover(t -> new ResponseEntity<>(Boolean.FALSE, HttpStatus.NOT_FOUND) ).get();	
	}

	@Override
	public ResponseEntity<Boolean> changeCategory(Long productId, Long categoryId) {
		
		CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults(SomeConstants.R4_CB);
		Retry retry = Retry.ofDefaults(SomeConstants.R4_R);
		Bulkhead bulkhead = Bulkhead.ofDefaults(SomeConstants.R4_BH);
		Supplier<ResponseEntity<Boolean>> supplier = () -> msProductsClient.changeCategory(productId, categoryId);

		Supplier<ResponseEntity<Boolean>> decoratedSupplier = Decorators.ofSupplier(supplier)
		  .withCircuitBreaker(circuitBreaker)
		  .withBulkhead(bulkhead)
		  .withRetry(retry)
		  .decorate();

		return Try.ofSupplier(decoratedSupplier).recover(t -> new ResponseEntity<>(Boolean.FALSE, HttpStatus.NOT_FOUND) ).get();	
	}
	
	@Override
	public ResponseEntity<List<CategoryDto>> getAllCategory() {

		CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults(SomeConstants.R4_CB);
		Retry retry = Retry.ofDefaults(SomeConstants.R4_R);
		Bulkhead bulkhead = Bulkhead.ofDefaults(SomeConstants.R4_BH);
		Supplier<ResponseEntity<List<CategoryDto>>> supplier = msProductsClient::getAllCategory;

		Supplier<ResponseEntity<List<CategoryDto>>> decoratedSupplier = Decorators.ofSupplier(supplier)
		  .withCircuitBreaker(circuitBreaker)
		  .withBulkhead(bulkhead)
		  .withRetry(retry)
		  .decorate();

		return Try.ofSupplier(decoratedSupplier).recover(t -> new ResponseEntity<>(Collections.emptyList(), HttpStatus.NOT_FOUND) ).get();		
	}

	@Override
	public ResponseEntity<List<CategoryDto>> getAllCategoryPaged(Integer page, Integer size) {

		CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults(SomeConstants.R4_CB);
		Retry retry = Retry.ofDefaults(SomeConstants.R4_R);
		Bulkhead bulkhead = Bulkhead.ofDefaults(SomeConstants.R4_BH);
		Supplier<ResponseEntity<List<CategoryDto>>> supplier = ()-> msProductsClient.getAllCategoryPaged(page, size);

		Supplier<ResponseEntity<List<CategoryDto>>> decoratedSupplier = Decorators.ofSupplier(supplier)
		  .withCircuitBreaker(circuitBreaker)
		  .withBulkhead(bulkhead)
		  .withRetry(retry)
		  .decorate();

		return Try.ofSupplier(decoratedSupplier).recover(t -> new ResponseEntity<>(Collections.emptyList(), HttpStatus.NOT_FOUND) ).get();		
	
	}
	@Override
	public ResponseEntity<CategoryDto> getCategoryById(Long id) {

		CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults(SomeConstants.R4_CB);
		Retry retry = Retry.ofDefaults(SomeConstants.R4_R);
		Bulkhead bulkhead = Bulkhead.ofDefaults(SomeConstants.R4_BH);
		Supplier<ResponseEntity<CategoryDto>> supplier = () -> msProductsClient.getCategoryById(id);

		Supplier<ResponseEntity<CategoryDto>> decoratedSupplier = Decorators.ofSupplier(supplier)
		  .withCircuitBreaker(circuitBreaker)
		  .withBulkhead(bulkhead)
		  .withRetry(retry)
		  .decorate();

		return Try.ofSupplier(decoratedSupplier).recover(t -> new ResponseEntity<>(new CategoryDto(), HttpStatus.NOT_FOUND) ).get();		
	
	}

	@Override
	public ResponseEntity<CategoryDto> create(@Valid CategoryDto dto) {

		CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults(SomeConstants.R4_CB);
		Retry retry = Retry.ofDefaults(SomeConstants.R4_R);
		Bulkhead bulkhead = Bulkhead.ofDefaults(SomeConstants.R4_BH);
		Supplier<ResponseEntity<CategoryDto>> supplier = () -> msProductsClient.create(dto);

		Supplier<ResponseEntity<CategoryDto>> decoratedSupplier = Decorators.ofSupplier(supplier)
		  .withCircuitBreaker(circuitBreaker)
		  .withBulkhead(bulkhead)
		  .withRetry(retry)
		  .decorate();

		return Try.ofSupplier(decoratedSupplier).recover(t -> new ResponseEntity<>(new CategoryDto(), HttpStatus.NOT_FOUND) ).get();		
	
	}

	@Override
	public ResponseEntity<CategoryDto> update(Long id, @Valid CategoryDto dto) {

		CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults(SomeConstants.R4_CB);
		Retry retry = Retry.ofDefaults(SomeConstants.R4_R);
		Bulkhead bulkhead = Bulkhead.ofDefaults(SomeConstants.R4_BH);
		Supplier<ResponseEntity<CategoryDto>> supplier = () -> msProductsClient.update(id, dto);

		Supplier<ResponseEntity<CategoryDto>> decoratedSupplier = Decorators.ofSupplier(supplier)
		  .withCircuitBreaker(circuitBreaker)
		  .withBulkhead(bulkhead)
		  .withRetry(retry)
		  .decorate();

		return Try.ofSupplier(decoratedSupplier).recover(t -> new ResponseEntity<>(new CategoryDto(), HttpStatus.NOT_FOUND) ).get();		
	}

	@Override
	public ResponseEntity<Boolean> deleteCategory(Long id) {

		CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults(SomeConstants.R4_CB);
		Retry retry = Retry.ofDefaults(SomeConstants.R4_R);
		Bulkhead bulkhead = Bulkhead.ofDefaults(SomeConstants.R4_BH);
		Supplier<ResponseEntity<Boolean>> supplier = () -> msProductsClient.deleteCategory(id);

		Supplier<ResponseEntity<Boolean>> decoratedSupplier = Decorators.ofSupplier(supplier)
		  .withCircuitBreaker(circuitBreaker)
		  .withBulkhead(bulkhead)
		  .withRetry(retry)
		  .decorate();

		return Try.ofSupplier(decoratedSupplier).recover(t -> new ResponseEntity<>(Boolean.FALSE, HttpStatus.NOT_FOUND) ).get();	
	}

	@Override
	public ResponseEntity<Boolean> addProductToCategory(Long categoryId, Long productId) {

		CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults(SomeConstants.R4_CB);
		Retry retry = Retry.ofDefaults(SomeConstants.R4_R);
		Bulkhead bulkhead = Bulkhead.ofDefaults(SomeConstants.R4_BH);
		Supplier<ResponseEntity<Boolean>> supplier = () -> msProductsClient.addProductToCategory(categoryId, productId);

		Supplier<ResponseEntity<Boolean>> decoratedSupplier = Decorators.ofSupplier(supplier)
		  .withCircuitBreaker(circuitBreaker)
		  .withBulkhead(bulkhead)
		  .withRetry(retry)
		  .decorate();

		return Try.ofSupplier(decoratedSupplier).recover(t -> new ResponseEntity<>(Boolean.FALSE, HttpStatus.NOT_FOUND) ).get();	
	}


}
