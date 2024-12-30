package dev.ime.client;

import java.util.List;
import java.util.Set;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import dev.ime.dto.OrderDetailDto;
import dev.ime.dto.OrderDto;
import jakarta.validation.Valid;

@FeignClient( name = "ms-orders" )
public interface MsOrdersClient {

	@GetMapping("/api/orders")
	ResponseEntity<List<OrderDto>> getAllOrder();

	@GetMapping("/api/orders")
	ResponseEntity<List<OrderDto>> getAllOrderPaged(
			@RequestParam(required = false) Integer page, 
			@RequestParam(required = false) Integer size);
	
	@GetMapping("/api/orders/{id}")
	ResponseEntity<OrderDto> getOrderById(@PathVariable Long id);
	
	@PostMapping("/api/orders")
	ResponseEntity<OrderDto> create(@Valid @RequestBody OrderDto dto);

	@PutMapping("/api/orders/{id}")
	ResponseEntity<OrderDto> update(@PathVariable Long id, @Valid @RequestBody OrderDto dto);

	@DeleteMapping("/api/orders/{id}")
	ResponseEntity<Boolean> deleteOrder(@PathVariable Long id);

	@PutMapping("/api/orders/{orderId}/orderdetails/{orderDetailId}")
	ResponseEntity<Boolean> addOrderDetail(@PathVariable Long orderId, @PathVariable Long orderDetailId);

	@GetMapping("/api/orders/{orderId}/orderdetails")
	ResponseEntity<Set<OrderDetailDto>> getLinesByOrderId(@PathVariable Long orderId);
	
	
	
	@GetMapping("/api/orderdetails")
	ResponseEntity<List<OrderDetailDto>> getAllOrderDetail();

	@GetMapping("/api/orderdetails")
	ResponseEntity<List<OrderDetailDto>> getAllOrderDetailPaged(
			@RequestParam(required = false ) Integer page, 
			@RequestParam(required = false ) Integer size);
	
	@GetMapping("/api/orderdetails/{id}")
	ResponseEntity<OrderDetailDto> getOrderDetailById(@PathVariable Long id);
	
	@PostMapping("/api/orderdetails")
	ResponseEntity<OrderDetailDto> create(@Valid @RequestBody OrderDetailDto dto);

	@PutMapping("/api/orderdetails/{id}")
	ResponseEntity<OrderDetailDto> update(@PathVariable Long id, @Valid @RequestBody OrderDetailDto dto);

	@DeleteMapping("/api/orderdetails/{id}")
	ResponseEntity<Boolean> deleteOrderDetail(@PathVariable Long id);

	@PutMapping("/api/orderdetails/{orderDetailId}/orders/{orderId}")
	ResponseEntity<Boolean> setOrder(@PathVariable Long orderDetailId, @PathVariable Long orderId);

	@PutMapping("/api/orderdetails/products/{productId}")
	ResponseEntity<Boolean> getAnyByProductId(@PathVariable Long productId);
	
}
