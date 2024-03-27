package dev.ime.resource.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.ime.dto.OrderDetailDto;
import dev.ime.entity.OrderDetail;
import dev.ime.mapper.impl.OrderDetailMapper;
import dev.ime.resource.GenericResource;
import dev.ime.resource.OrderDetailSpecificResource;
import dev.ime.service.impl.OrderDetailServiceImpl;
import dev.ime.tool.SomeConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/orderdetails")
@Tag(name = "OrderDetail", description="OrderDetail Operations")
public class OrderDetailResource implements GenericResource<OrderDetailDto>, OrderDetailSpecificResource{

	private final OrderDetailServiceImpl orderDetailService;
	private final OrderDetailMapper orderDetailMapper;	
	
	public OrderDetailResource(OrderDetailServiceImpl orderDetailService, OrderDetailMapper orderDetailMapper) {
		super();
		this.orderDetailService = orderDetailService;
		this.orderDetailMapper = orderDetailMapper;
	}

	@GetMapping
	@Operation(summary="Get a List of all OrderDetail", description="Get a List of all OrderDetail, @return an object Response with a List of DTO's")
	@Override
	public ResponseEntity<List<OrderDetailDto>> getAll(
			@RequestParam( name = "page", required = false ) Integer page, 
			@RequestParam( name = "size", required = false ) Integer size) {

		List<OrderDetail>list;
		
		if ( ( page != null && page >= 0 ) && ( size != null && size >= 1 ) ) {
			list = orderDetailService.getAllPaged(page, size);
		}
		else if ( page != null && page >= 0 ) {
			list = orderDetailService.getAllPaged(page, SomeConstants.SIZE_REQUEST);
		}
		else  {
			list = orderDetailService.getAll();			
		}	
		
		return list.isEmpty()? new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK)
				:new ResponseEntity<>(orderDetailMapper.toListDto(list), HttpStatus.OK);
	}

	@GetMapping("/{id}")	
	@Operation(summary="Get a OrderDetail according to an Id", description="Get a OrderDetail according to an Id, @return an object Response with the entity required in a DTO")
	@Override
	public ResponseEntity<OrderDetailDto> getById(@PathVariable Long id) {

		Optional<OrderDetail> opt = orderDetailService.getById(id);
		
		return opt.isPresent()? new ResponseEntity<>(orderDetailMapper.toDto(opt.get()),HttpStatus.OK)
				:new ResponseEntity<>(new OrderDetailDto(), HttpStatus.OK);
	}
	
	@PostMapping
	@Operation(summary="Create a new OrderDetail", description="Create a new OrderDetail, @return an object Response with the entity in a DTO")
	@Override
	public ResponseEntity<OrderDetailDto> create(@Valid @RequestBody OrderDetailDto entity) {

		Optional<OrderDetail> opt = orderDetailService.create(entity);
		
		return opt.isPresent()? new ResponseEntity<>(orderDetailMapper.toDto(opt.get()),HttpStatus.CREATED)
				:new ResponseEntity<>(new OrderDetailDto(), HttpStatus.OK);
	}

	@PutMapping("/{id}")
	@Operation(summary="Update fields in a OrderDetail", description="Update fields in a OrderDetail, @return an object Response with the entity modified in a DTO")
	@Override
	public ResponseEntity<OrderDetailDto> update(@PathVariable Long id, @Valid @RequestBody OrderDetailDto entity) {
		
		Optional<OrderDetail> opt = orderDetailService.update(id, entity);		

		return opt.isPresent()? new ResponseEntity<>(orderDetailMapper.toDto(opt.get()),HttpStatus.OK)
				:new ResponseEntity<>(new OrderDetailDto(), HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	@Operation(summary="Delete a OrderDetail by its Id", description="Delete a OrderDetail by its Id, @return an object Response with a message")
	@Override
	public ResponseEntity<Boolean> delete(@PathVariable Long id) {
		
		return orderDetailService.delete(id) == 0 ? new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK)
				:new ResponseEntity<>(Boolean.FALSE, HttpStatus.OK);
	}

	@PutMapping("/{orderDetailId}/orders/{orderId}")
	@Operation(summary="Set in a OrderDetail a Order", description="Set in a OrderDetail a Order, @return an object Response with a message")
	@Override
	public ResponseEntity<Boolean> setOrder(@PathVariable Long orderDetailId, @PathVariable Long orderId) {
		
		return Boolean.TRUE.equals(orderDetailService.setOrder(orderDetailId, orderId))? new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK)
				:new ResponseEntity<>(Boolean.FALSE, HttpStatus.OK);
	}

	@PutMapping("/products/{productId}")
	@Operation(summary="Find by Product Id", description="Find by Product Id, @return a Response with True/False whether exists any coincidence ")
	@Override
	public ResponseEntity<Boolean> getAnyByProductId(@PathVariable Long productId) {
		
		return Boolean.TRUE.equals(orderDetailService.getAnyByProductId(productId))? new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK)
				:new ResponseEntity<>(Boolean.FALSE, HttpStatus.OK);
	}

}
