package dev.ime.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import dev.ime.dto.OrderDto;
import dev.ime.entity.*;
import dev.ime.exception.DateBadFormatException;
import dev.ime.exception.EntityAssociatedException;
import dev.ime.exception.ResourceNotFoundException;
import dev.ime.repository.OrderDetailRepository;
import dev.ime.repository.OrderRepository;
import dev.ime.service.GenericService;
import dev.ime.service.OrderSpecificService;
import dev.ime.tool.Checker;
import dev.ime.tool.SomeConstants;
import dev.ime.tool.Transformer;

@Service
public class OrderServiceImpl implements GenericService<Order,OrderDto>, OrderSpecificService{

	private final OrderRepository orderRepo;
	private final OrderDetailRepository orderDetailRepo;
	private final Checker checker;
	private final Transformer transformer;	
	
	public OrderServiceImpl(OrderRepository orderRepo, OrderDetailRepository orderDetailRepo, Checker checker, Transformer transformer) {
		super();
		this.orderRepo = orderRepo;
		this.orderDetailRepo = orderDetailRepo;
		this.checker = checker;
		this.transformer = transformer;
	}

	@Override
	public List<Order> getAll() {
		return orderRepo.findAll(Sort.by("id"));
	}

	@Override
	public List<Order> getAllPaged(Integer pageNumber, Integer pageSize) {
		return orderRepo.findAll(PageRequest.of(pageNumber, pageSize)).toList();
	}

	@Override
	public Optional<Order> getById(Long id) {
		return Optional.ofNullable( orderRepo.findById(id).orElseThrow( () -> new ResourceNotFoundException(Map.of(SomeConstants.ORDERID, String.valueOf(id) ) ) ) );
	}

	@Override
	public Optional<Order> create(OrderDto dto) {
		
		validateOrderDto(dto);
		
		return Optional.ofNullable(orderRepo.save(updateOrderFields(new Order(), dto)));
	}

	private void validateOrderDto(OrderDto dto) {
		
		if ( !checker.localDateValid( dto.orderDate() ) ) throw new DateBadFormatException( Map.of(SomeConstants.DATEFORMAT, String.valueOf(dto.orderDate() ) ) ); 
		
		if ( !checker.checkCustomerId(dto.customerId() ) ) throw new ResourceNotFoundException( Map.of(SomeConstants.CUSTOMERID, String.valueOf(dto.customerId() ) ) );
	
	}

	private Order updateOrderFields(Order order, OrderDto dto) {
	
		order.setCustomerId(dto.customerId());
		order.setOrderDate(transformer.fromStringToDateTimeOnErrorZeroes( dto.orderDate() ));
		
		return order;
		
	}
	
	@Override
	public Optional<Order> update(Long id, OrderDto dto) {
		
		Order order = searchOrderById(id);

		validateOrderDto(dto);

		return Optional.ofNullable( orderRepo.save(updateOrderFields(order, dto)));
	}

	private Order searchOrderById(Long id) {
		
		return orderRepo.findById(id).orElseThrow( () -> new ResourceNotFoundException(Map.of(SomeConstants.ORDERID, String.valueOf(id) ) ) );
	
	}

	@Override
	public Integer delete(Long id) {
		
		Order o = searchOrderById(id);
		
		if (  o.getOrderDetails().isEmpty() ) {
			
			orderRepo.deleteById(id);
			return orderRepo.findById(id).isEmpty()?0:1;
			
		}else {				
			throw new EntityAssociatedException( Map.of( SomeConstants.ORDERID, String.valueOf(id) ) );
		}	
	}

	@Override
	public Boolean addOrderDetail(Long orderId, Long orderDetailId) {
		
		Order order = searchOrderById(orderId);
		OrderDetail orderDetail = searchOrderDetailById(orderDetailId);
		
		orderDetail.setOrder(order);
		order.getOrderDetails().add(orderDetail);		
		
		return Optional.ofNullable( orderRepo.save( order )).isPresent();
	}

	private OrderDetail searchOrderDetailById(Long orderDetailId) {
		
		return orderDetailRepo.findById(orderDetailId).orElseThrow( () -> new ResourceNotFoundException(Map.of(SomeConstants.ORDERDETAILID, String.valueOf(orderDetailId) ) ) );
	
	}

}
