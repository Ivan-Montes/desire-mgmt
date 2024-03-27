package dev.ime.resource.impl;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
import dev.ime.dto.OrderDto;
import dev.ime.entity.Order;
import dev.ime.mapper.impl.OrderDetailMapper;
import dev.ime.mapper.impl.OrderMapper;
import dev.ime.resource.GenericResource;
import dev.ime.resource.OrderSpecificResource;
import dev.ime.service.impl.OrderServiceImpl;
import dev.ime.tool.SomeConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Order", description="Order Operations")
public class OrderResource implements GenericResource<OrderDto>, OrderSpecificResource {

	private final OrderServiceImpl orderService;
	private final OrderMapper orderMapper;	
	private final OrderDetailMapper orderDetailMapper;
	
	public OrderResource(OrderServiceImpl orderService, OrderMapper orderMapper, OrderDetailMapper orderDetailMapper) {
		super();
		this.orderService = orderService;
		this.orderMapper = orderMapper;
		this.orderDetailMapper = orderDetailMapper;
	}

	@GetMapping
	@Operation(summary="Get a List of all Order", description="Get a List of all Order, @return an object Response with a List of DTO's")
	@Override
	public ResponseEntity<List<OrderDto>> getAll(
			@RequestParam( value="page", required = false) Integer page, 
			@RequestParam( value="size", required = false) Integer size) {
		
		List<Order>list;
		
		if ( ( page != null && page >= 0 ) && ( size != null && size >= 1 ) ) {
			list = orderService.getAllPaged(page, size);
		}
		else if ( page != null && page >= 0 ) {
			list = orderService.getAllPaged(page, SomeConstants.SIZE_REQUEST);
		}
		else  {
			list = orderService.getAll();			
		}	
		
		return list.isEmpty()? new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK)
				:new ResponseEntity<>(orderMapper.toListDto(list), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	@Operation(summary="Get a Order according to an Id", description="Get a Order according to an Id, @return an object Response with the entity required in a DTO")
	@Override
	public ResponseEntity<OrderDto> getById(@PathVariable Long id) {
		
		Optional<Order> opt = orderService.getById(id);
		
		return opt.isPresent()? new ResponseEntity<>(orderMapper.toDto(opt.get()),HttpStatus.OK)
				:new ResponseEntity<>(new OrderDto(), HttpStatus.OK);	
	}

	@PostMapping
	@Operation(summary="Create a new Order", description="Create a new Order, @return an object Response with the entity in a DTO")
	@Override
	public ResponseEntity<OrderDto> create(@Valid @RequestBody OrderDto entity) {
		
		Optional<Order> opt = orderService.create(entity);
		
		return opt.isPresent()? new ResponseEntity<>(orderMapper.toDto(opt.get()),HttpStatus.CREATED)
				:new ResponseEntity<>(new OrderDto(), HttpStatus.OK);	
	}

	@PutMapping("/{id}")
	@Override
	@Operation(summary="Update fields in a Order", description="Update fields in a Order, @return an object Response with the entity modified in a DTO")
	public ResponseEntity<OrderDto> update(@PathVariable Long id, @Valid @RequestBody OrderDto entity) {
		
		Optional<Order> opt = orderService.update(id, entity);
		
		return opt.isPresent()? new ResponseEntity<>(orderMapper.toDto(opt.get()),HttpStatus.OK)
				:new ResponseEntity<>(new OrderDto(), HttpStatus.OK);	
	}

	@DeleteMapping("/{id}")
	@Override
	@Operation(summary="Delete a Order by its Id", description="Delete a Order by its Id, @return an object Response with a message")
	public ResponseEntity<Boolean> delete(@PathVariable Long id) {
		
		return orderService.delete(id) == 0 ? new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK)
				:new ResponseEntity<>(Boolean.FALSE, HttpStatus.OK);
	}

	@PutMapping("/{orderId}/orderdetails/{orderDetailId}")
	@Override
	@Operation(summary="Add in a Order a OrderDetail", description="Add in a Order a OrderDetail, @return an object Response with a message")
	public ResponseEntity<Boolean> addOrderDetail(@PathVariable Long orderId, @PathVariable Long orderDetailId) {
		
		return Boolean.TRUE.equals(orderService.addOrderDetail(orderId, orderDetailId))? new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK)
				:new ResponseEntity<>(Boolean.FALSE, HttpStatus.OK);
	}

	@GetMapping("/{orderId}/orderdetails")
	@Override
	@Operation(summary="Get the lines of a Order", description="Get the lines of a Order, AKA OrderDetail objects")
	public ResponseEntity<Set<OrderDetailDto>> getLinesByOrderId(@PathVariable Long orderId) {
		
		Optional<Order> optOrd = orderService.getById(orderId);
	
		return ( optOrd.isPresent() && !optOrd.get().getOrderDetails().isEmpty() )?
				new ResponseEntity<>(orderDetailMapper.toSetDto(optOrd.get().getOrderDetails()), HttpStatus.OK)
				:new ResponseEntity<>(new HashSet<>(), HttpStatus.OK);
	}
	
	
}
