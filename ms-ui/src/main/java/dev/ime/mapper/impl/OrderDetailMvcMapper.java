package dev.ime.mapper.impl;

import org.springframework.stereotype.Component;

import dev.ime.dto.OrderDetailDto;
import dev.ime.dtomvc.OrderDetailMvcDto;
import dev.ime.mapper.GenericMvcMapper;

@Component
public class OrderDetailMvcMapper implements GenericMvcMapper<OrderDetailMvcDto, OrderDetailDto>{

	@Override
	public OrderDetailDto fromMvcDtoToDto(OrderDetailMvcDto mvcDto) {
		
		return new OrderDetailDto(mvcDto.getOrderDetailId(),
								  mvcDto.getQuantity(),
								  mvcDto.getDiscount(),
								  mvcDto.getProductId(),
								  mvcDto.getOrderId());
	}

}
