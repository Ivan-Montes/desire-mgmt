package dev.ime.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import dev.ime.dto.OrderDto;
import dev.ime.entity.*;
import dev.ime.exception.DateBadFormatException;
import dev.ime.exception.EntityAssociatedException;
import dev.ime.exception.ResourceNotFoundException;
import dev.ime.mapper.impl.OrderMapper;
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
	private final OrderMapper orderMapper;
	private final OrderDetailRepository orderDetailRepo;
	private final Checker checker;
	private final Transformer transformer;	
	
	public OrderServiceImpl(OrderRepository orderRepo, OrderMapper orderMapper, OrderDetailRepository orderDetailRepo, Checker checker, Transformer transformer) {
		super();
		this.orderRepo = orderRepo;
		this.orderMapper = orderMapper;
		this.orderDetailRepo = orderDetailRepo;
		this.checker = checker;
		this.transformer = transformer;
	}

	@Override
	public List<Order> getAll() {
		return orderRepo.findAll();
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
		
		if ( !checker.localDateValid( dto.orderDate() ) ) throw new DateBadFormatException( Map.of(SomeConstants.DATEFORMAT, String.valueOf(dto.orderDate() ) ) ); 
		//Check customerId feign
		if ( !checker.checkCustomerId(dto.customerId() ) ) throw new ResourceNotFoundException( Map.of(SomeConstants.CUSTOMERID, String.valueOf(dto.customerId() ) ) );
		
		Order o = orderMapper.fromDto(dto);
		return Optional.ofNullable(orderRepo.save(o));
	}

	@Override
	public Optional<Order> update(Long id, OrderDto dto) {
		
		Order o = orderRepo.findById(id).orElseThrow( () -> new ResourceNotFoundException(Map.of(SomeConstants.ORDERID, String.valueOf(id) ) ) );

		if ( !checker.localDateValid( dto.orderDate() ) ) throw new DateBadFormatException( Map.of(SomeConstants.DATEFORMAT, String.valueOf(dto.orderDate() ) ) ); 
		//Check customerId feign
		if ( !checker.checkCustomerId(dto.customerId() ) ) throw new ResourceNotFoundException( Map.of(SomeConstants.CUSTOMERID, String.valueOf(dto.customerId() ) ) );
		
		o.setCustomerId(dto.customerId());
		o.setOrderDate(transformer.fromStringToDateTimeOnErrorZeroes( dto.orderDate() ));
		
		return Optional.ofNullable( orderRepo.save(o));
	}

	@Override
	public Integer delete(Long id) {
		
		Order o =  orderRepo.findById(id).orElseThrow( () -> new ResourceNotFoundException( Map.of( SomeConstants.ORDERID, String.valueOf(id) )  ) );
		
		if (  o.getOrderDetails().isEmpty() ) {
			
			orderRepo.deleteById(id);
			return orderRepo.findById(id).isEmpty()?0:1;
			
		}else {
				
			throw new EntityAssociatedException( Map.of( SomeConstants.ORDERID, String.valueOf(id) ) );
		}	
	}

	@Override
	public Boolean addOrderDetail(Long orderId, Long orderDetailId) {
		Order o = orderRepo.findById(orderId).orElseThrow( () -> new ResourceNotFoundException(Map.of(SomeConstants.ORDERID, String.valueOf(orderId) ) ) );
		OrderDetail od = orderDetailRepo.findById(orderDetailId).orElseThrow( () -> new ResourceNotFoundException(Map.of(SomeConstants.ORDERDETAILID, String.valueOf(orderDetailId) ) ) );
		od.setOrder(o);
		o.getOrderDetails().add(od);
		Optional<Order> opt =  Optional.ofNullable( orderRepo.save( o ));
		
		return opt.isPresent();
	}

}
