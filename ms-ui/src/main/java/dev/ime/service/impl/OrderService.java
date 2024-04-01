package dev.ime.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import dev.ime.client.impl.MsOrdersClientImpl;
import dev.ime.dto.OrderDto;
import dev.ime.dtomvc.OrderMvcDto;
import dev.ime.exception.EntityAssociatedException;
import dev.ime.exception.ResourceNotFoundException;
import dev.ime.mapper.impl.OrderMvcMapper;
import dev.ime.service.GenericService;
import dev.ime.service.OrderSpecificService;
import dev.ime.tool.SomeConstants;

@Service
public class OrderService implements GenericService<OrderMvcDto, OrderDto>, OrderSpecificService{

	private final MsOrdersClientImpl msOrdersClient;
	private final OrderMvcMapper orderMvcMapper;
	
	public OrderService(MsOrdersClientImpl msOrdersClient, OrderMvcMapper orderMvcMapper) {
		super();
		this.msOrdersClient = msOrdersClient;
		this.orderMvcMapper = orderMvcMapper;
	}

	@Override
	public List<OrderDto> getAll() {
		
		return msOrdersClient.getAllOrder().getBody();
	}


	@Override
	public List<OrderDto> getAllPaged(Integer pageNumber, Integer pageSize) {
		
		return  msOrdersClient.getAllOrderPaged(pageNumber, pageSize).getBody();
	}


	@Override
	public OrderDto getById(Long id) {
		
		return  Optional.ofNullable(msOrdersClient.getOrderById(id))
				.filter( r -> r.getStatusCode().equals(HttpStatus.OK))
				.map(ResponseEntity::getBody)
				.filter( o -> o.orderId() > 0 )
				.orElseThrow( ()-> new ResourceNotFoundException(Map.of(SomeConstants.ORDERID, String.valueOf(id))));
	}


	@Override
	public OrderDto create(OrderMvcDto dto) {

		return Optional.ofNullable(msOrdersClient.create(orderMvcMapper.fromMvcDtoToDto(dto)))
				.filter( r -> r.getStatusCode().equals(HttpStatus.CREATED))
				.map(ResponseEntity::getBody)
				.filter( o -> o.orderId() > 0 )
				.orElseThrow( () -> new ResourceNotFoundException(Map.of( SomeConstants.DATELESS, dto.toString())));
	}


	@Override
	public OrderDto update(Long id, OrderMvcDto dto) {

		return Optional.ofNullable(msOrdersClient.update(id, orderMvcMapper.fromMvcDtoToDto(dto)))
				.filter( r -> r.getStatusCode().equals(HttpStatus.OK))
				.map(ResponseEntity::getBody)
				.filter( o -> o.orderId() > 0 )
				.orElseThrow( () -> new ResourceNotFoundException(Map.of( SomeConstants.ORDERID, String.valueOf(id))));
	}

	@Override
	public Boolean delete(Long id) {

		return Optional.ofNullable(msOrdersClient.deleteOrder(id))
				.filter( r -> r.getStatusCode().equals(HttpStatus.OK))
				.map(ResponseEntity::getBody)
				.orElseThrow( () -> new EntityAssociatedException(Map.of( SomeConstants.ORDERID, String.valueOf(id))));
	}

	@Override
	public Boolean addOrderDetail(Long orderId, Long orderDetailId) {

		return Optional.ofNullable(msOrdersClient.addOrderDetail(orderId, orderDetailId))
				.filter( r -> r.getStatusCode().equals(HttpStatus.OK))
				.map(ResponseEntity::getBody)
				.orElseThrow( () -> new ResourceNotFoundException(Map.of( SomeConstants.ORDERID, String.valueOf(orderId), 
																		  SomeConstants.ORDERDETAILID, String.valueOf(orderDetailId))));	
	}

}
