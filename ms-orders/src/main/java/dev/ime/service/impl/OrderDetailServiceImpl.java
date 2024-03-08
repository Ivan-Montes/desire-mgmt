package dev.ime.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import dev.ime.dto.OrderDetailDto;
import dev.ime.entity.Order;
import dev.ime.entity.OrderDetail;
import dev.ime.exception.ResourceNotFoundException;
import dev.ime.mapper.impl.OrderDetailMapper;
import dev.ime.repository.OrderDetailRepository;
import dev.ime.repository.OrderRepository;
import dev.ime.service.GenericService;
import dev.ime.service.OrderDetailSpecificService;
import dev.ime.tool.Checker;
import dev.ime.tool.SomeConstants;

@Service
public class OrderDetailServiceImpl implements GenericService<OrderDetail, OrderDetailDto>, OrderDetailSpecificService{

	private final OrderDetailRepository orderDetailRepo;
	private final OrderRepository orderRepo;
	private final OrderDetailMapper orderDetailMapper;
	private final Checker checker;		
	
	public OrderDetailServiceImpl(OrderDetailRepository orderDetailRepo, OrderRepository orderRepo,
			OrderDetailMapper orderDetailMapper, Checker checker) {
		super();
		this.orderDetailRepo = orderDetailRepo;
		this.orderRepo = orderRepo;
		this.orderDetailMapper = orderDetailMapper;
		this.checker = checker;
	}

	@Override
	public List<OrderDetail> getAll() {
		return  orderDetailRepo.findAll();
	}

	@Override
	public List<OrderDetail> getAllPaged(Integer pageNumber, Integer pageSize) {
		return orderDetailRepo.findAll(PageRequest.of(pageNumber,pageSize)).toList();
	}

	@Override
	public Optional<OrderDetail> getById(Long id) {		
		return Optional.ofNullable( orderDetailRepo.findById(id).orElseThrow( ()-> new ResourceNotFoundException(Map.of( SomeConstants.ORDERDETAILID, String.valueOf(id) ) ) ) );
	}

	@Override
	public Optional<OrderDetail> create(OrderDetailDto dto) {
		
		Order o = orderRepo.findById(dto.orderId()).orElseThrow( () -> new ResourceNotFoundException(Map.of(SomeConstants.ORDERID, String.valueOf(dto.orderId()) ) ) );
		
		if ( !checker.checkProductId(dto.productId() ) ) throw new ResourceNotFoundException( Map.of(SomeConstants.PRODUCTID, String.valueOf(dto.productId() ) ) );
		OrderDetail od = orderDetailMapper.fromDto(dto);		
		od.setOrder(o);
		
		return Optional.ofNullable(orderDetailRepo.save(od));
	}

	@Override
	public Optional<OrderDetail> update(Long id, OrderDetailDto dto) {
		
		OrderDetail od = orderDetailRepo.findById(id).orElseThrow( ()-> new ResourceNotFoundException(Map.of( SomeConstants.ORDERDETAILID, String.valueOf(id) ) ) );
		Order o = orderRepo.findById(dto.orderId()).orElseThrow( () -> new ResourceNotFoundException(Map.of(SomeConstants.ORDERID, String.valueOf(dto.orderId()) ) ) );
		
		if ( !checker.checkProductId(dto.productId() ) ) throw new ResourceNotFoundException( Map.of(SomeConstants.PRODUCTID, String.valueOf(dto.productId() ) ) );
		
		od.setQuantity(dto.quantity());
		od.setDiscount(dto.discount());
		od.setProductId(dto.productId());
		od.setOrder(o);
		
		return Optional.ofNullable( orderDetailRepo.save(od) );
	}

	@Override
	public Integer delete(Long id) {
		
		orderDetailRepo.deleteById(id);		
		Optional<OrderDetail>opt = orderDetailRepo.findById(id);
		
		return opt.isEmpty()?0:1;
	}

	@Override
	public Boolean setOrder(Long orderDetailId, Long orderId) {
		
		OrderDetail od = orderDetailRepo.findById(orderDetailId).orElseThrow( ()-> new ResourceNotFoundException(Map.of( SomeConstants.ORDERDETAILID, String.valueOf(orderDetailId) ) ) );
		Order o = orderRepo.findById(orderId).orElseThrow( () -> new ResourceNotFoundException(Map.of(SomeConstants.ORDERID, String.valueOf(orderId) ) ) );
		od.setOrder(o);
		o.getOrderDetails().add(od);
		Optional<OrderDetail> opt =  Optional.ofNullable( orderDetailRepo.save( od ));
		
		return opt.isPresent();
	}

}
