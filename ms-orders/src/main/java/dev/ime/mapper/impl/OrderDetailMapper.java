package dev.ime.mapper.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import dev.ime.dto.OrderDetailDto;
import dev.ime.entity.OrderDetail;
import dev.ime.mapper.GenericMapper;

@Component
public class OrderDetailMapper  implements GenericMapper <OrderDetail, OrderDetailDto>{
	
	public OrderDetailMapper() {
		super();
	}

	@Override
	public OrderDetail fromDto(OrderDetailDto dto) {
		
		OrderDetail o = new OrderDetail();
		o.setId(dto.orderDetailId());
		o.setQuantity(dto.quantity());
		o.setDiscount(dto.discount());
		o.setProductId(dto.productId());
		o.setOrder(null);
		
		return o;
	}

	@Override
	public OrderDetailDto toDto(OrderDetail e) {
		return new OrderDetailDto(e.getId(),e.getQuantity(),e.getDiscount(),e.getProductId(),e.getOrder().getId());
	}

	@Override
	public List<OrderDetailDto> toListDto(List<OrderDetail> list) {
		
		return list.stream()
				.map(this::toDto)
				.toList();	
	}

}
