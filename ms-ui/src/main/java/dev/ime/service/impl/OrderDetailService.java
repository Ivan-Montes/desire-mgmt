package dev.ime.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import dev.ime.client.impl.MsOrdersClientImpl;
import dev.ime.dto.OrderDetailDto;
import dev.ime.dtomvc.OrderDetailMvcDto;
import dev.ime.exception.EntityAssociatedException;
import dev.ime.exception.ResourceNotFoundException;
import dev.ime.mapper.impl.OrderDetailMvcMapper;
import dev.ime.service.GenericService;
import dev.ime.service.OrderDetailSpecificService;
import dev.ime.tool.SomeConstants;

@Service
public class OrderDetailService  implements GenericService<OrderDetailMvcDto, OrderDetailDto>, OrderDetailSpecificService{

	private final MsOrdersClientImpl msOrdersClient;	
	private final OrderDetailMvcMapper orderDetailMvcMapper;	
	
	public OrderDetailService(MsOrdersClientImpl msOrdersClient, OrderDetailMvcMapper orderDetailMvcMapper) {
		super();
		this.msOrdersClient = msOrdersClient;
		this.orderDetailMvcMapper = orderDetailMvcMapper;
	}

	@Override
	public List<OrderDetailDto> getAll() {
		
		return msOrdersClient.getAllOrderDetail().getBody();
	}

	@Override
	public List<OrderDetailDto> getAllPaged(Integer pageNumber, Integer pageSize) {
		
		return  msOrdersClient.getAllOrderDetailPaged(pageNumber, pageSize).getBody();
	}

	@Override
	public OrderDetailDto getById(Long id) {
		
		return  Optional.ofNullable(msOrdersClient.getOrderDetailById(id))
				.filter( r -> r.getStatusCode().equals(HttpStatus.OK))
				.map(ResponseEntity::getBody)
				.filter( o -> o.orderDetailId() > 0 )
				.orElseThrow( ()-> new ResourceNotFoundException(Map.of(SomeConstants.ORDERDETAILID, String.valueOf(id))));
	}

	@Override
	public OrderDetailDto create(OrderDetailMvcDto dto) {
		
		return Optional.ofNullable(msOrdersClient.create(orderDetailMvcMapper.fromMvcDtoToDto(dto)))
				.filter( r -> r.getStatusCode().equals(HttpStatus.CREATED))
				.map(ResponseEntity::getBody)
				.filter( o -> o.orderDetailId() > 0 )
				.orElseThrow( () -> new ResourceNotFoundException(Map.of( SomeConstants.DATELESS, dto.toString())));
	}

	@Override
	public OrderDetailDto update(Long id, OrderDetailMvcDto dto) {

		return Optional.ofNullable(msOrdersClient.update(id, orderDetailMvcMapper.fromMvcDtoToDto(dto)))
				.filter( r -> r.getStatusCode().equals(HttpStatus.OK))
				.map(ResponseEntity::getBody)
				.filter( o -> o.orderDetailId().equals(id))
				.orElseThrow( () -> new ResourceNotFoundException(Map.of( SomeConstants.ORDERDETAILID, String.valueOf(id))));
	}

	@Override
	public Boolean delete(Long id) {

		return Optional.ofNullable(msOrdersClient.deleteOrderDetail(id))
				.filter( r -> r.getStatusCode().equals(HttpStatus.OK))
				.map(ResponseEntity::getBody)
				.orElseThrow( () -> new EntityAssociatedException(Map.of( SomeConstants.ORDERDETAILID, String.valueOf(id))));
	}

	@Override
	public Boolean setOrder(Long orderDetailId, Long orderId) {
		
		return Optional.ofNullable(msOrdersClient.setOrder(orderDetailId, orderId))
				.filter( r -> r.getStatusCode().equals(HttpStatus.OK))
				.map(ResponseEntity::getBody)
				.orElseThrow( () -> new ResourceNotFoundException(Map.of( SomeConstants.ORDERDETAILID, String.valueOf(orderDetailId), 
																		  SomeConstants.ORDERID, String.valueOf(orderId))));
	}

}
