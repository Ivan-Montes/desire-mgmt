package dev.ime.mapper.impl;

import org.springframework.stereotype.Component;

import dev.ime.dto.OrderDto;
import dev.ime.dtomvc.OrderMvcDto;
import dev.ime.mapper.GenericMvcMapper;

@Component
public class OrderMvcMapper implements GenericMvcMapper<OrderMvcDto, OrderDto> {

	@Override
	public OrderDto fromMvcDtoToDto(OrderMvcDto mvcDto) {
		
		return new OrderDto(mvcDto.getOrderId(),
							mvcDto.getCustomerId(),
							mvcDto.getOrderDate());
		
	}

}
