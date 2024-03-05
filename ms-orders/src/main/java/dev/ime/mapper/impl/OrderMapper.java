package dev.ime.mapper.impl;


import java.util.List;

import org.springframework.stereotype.Component;

import dev.ime.dto.OrderDto;
import dev.ime.entity.Order;
import dev.ime.mapper.GenericMapper;
import dev.ime.tool.Transformer;

@Component
public class OrderMapper implements GenericMapper <Order, OrderDto> {

	private Transformer transformer;	

	public OrderMapper(Transformer transformer) {
		super();
		this.transformer = transformer;
	}
	
	@Override
	public Order fromDto(OrderDto dto) {
		
		Order o = new Order();
		o.setId(dto.orderId());
		o.setCustomerId(dto.customerId());
		o.setOrderDate(transformer.fromStringToDateTimeOnErrorZeroes( dto.orderDate() ));
		
		return o;
	}
	
	@Override
	public OrderDto toDto(Order e) {
		return new OrderDto(e.getId(), e.getCustomerId(), e.getOrderDate().toString());
	}
	
	@Override
	public List<OrderDto>toListDto(List<Order>list){
		
		return list.stream()
				.map(this::toDto)
				.toList();			
	}
	
}
