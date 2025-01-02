package dev.ime.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import dev.ime.dto.OrderDetailDto;
import dev.ime.entity.Order;
import dev.ime.entity.OrderDetail;
import dev.ime.exception.ResourceNotFoundException;
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
	private final Checker checker;		
	
	public OrderDetailServiceImpl(OrderDetailRepository orderDetailRepo, OrderRepository orderRepo,
			Checker checker) {
		super();
		this.orderDetailRepo = orderDetailRepo;
		this.orderRepo = orderRepo;
		this.checker = checker;
	}

	@Override
	public List<OrderDetail> getAll() {
		return  orderDetailRepo.findAll(Sort.by("id"));
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
		
		Order orderFound = searchOrderById(dto.orderId());
		
		if ( !checkProductId(dto.productId() ) ) throw new ResourceNotFoundException( Map.of(SomeConstants.PRODUCTID, String.valueOf(dto.productId() ) ) );
		
		return Optional.ofNullable(orderDetailRepo.save(updateOrderDetailFields(new OrderDetail(), dto, orderFound)));
	}

	private boolean checkProductId(Long productId) {
		return checker.checkProductId(productId);
	}
	
	private OrderDetail updateOrderDetailFields(OrderDetail orderDetail, OrderDetailDto dto, Order order) {
		
		orderDetail.setQuantity(dto.quantity());
		orderDetail.setDiscount(dto.discount());
		orderDetail.setProductId(dto.productId());
		orderDetail.setOrder(order);
		
		return orderDetail;
		
	}
	
	@Override
	public Optional<OrderDetail> update(Long id, OrderDetailDto dto) {
		
		OrderDetail orderDetail = searchOrderDetailById(id);
		Order orderFound = searchOrderById(dto.orderId());
		
		if ( !checkProductId(dto.productId() ) ) throw new ResourceNotFoundException( Map.of(SomeConstants.PRODUCTID, String.valueOf(dto.productId() ) ) );
		
		return Optional.ofNullable( orderDetailRepo.save(updateOrderDetailFields(orderDetail, dto, orderFound)) );
	}

	@Override
	public Integer delete(Long id) {
		
		orderDetailRepo.deleteById(id);		
		Optional<OrderDetail>opt = orderDetailRepo.findById(id);
		
		return opt.isEmpty()?0:1;
	}

	@Override
	public Boolean setOrder(Long orderDetailId, Long orderId) {
		
		OrderDetail od = searchOrderDetailById(orderDetailId);
		Order orderFound = searchOrderById(orderId);
		od.setOrder(orderFound);
		orderFound.getOrderDetails().add(od);
		Optional<OrderDetail> opt =  Optional.ofNullable( orderDetailRepo.save( od ));
		
		return opt.isPresent();
	}

	private OrderDetail searchOrderDetailById(Long orderDetailId) {
		
		return orderDetailRepo.findById(orderDetailId).orElseThrow( ()-> new ResourceNotFoundException(Map.of( SomeConstants.ORDERDETAILID, String.valueOf(orderDetailId) ) ) );
		
	}

	private Order searchOrderById(Long orderId) {
		
		return orderRepo.findById(orderId).orElseThrow( () -> new ResourceNotFoundException(Map.of(SomeConstants.ORDERID, String.valueOf(orderId) ) ) );
		
	}

	@Override
	public Boolean getAnyByProductId(Long productId) {
		
		return !orderDetailRepo.findByProductId(productId).isEmpty();
	}

}
