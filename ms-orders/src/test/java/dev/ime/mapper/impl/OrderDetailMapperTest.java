package dev.ime.mapper.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.ime.dto.OrderDetailDto;
import dev.ime.entity.Order;
import dev.ime.entity.OrderDetail;

@ExtendWith(MockitoExtension.class)
class OrderDetailMapperTest {
	
	@InjectMocks
	private OrderDetailMapper mapper;
	
	private List<OrderDetail>orderDetails;
	private OrderDetail orderDetailTest;
	private OrderDetailDto orderDetailDto;
	private Order orderTest;
	private final Long orderDetailId = 2L;
	private final Long orderId = 7L;
	private final Long productId = 3L;
	private final Integer quantity = 13;
	private final Double discount = 23.9D;
	
	@BeforeEach
	private void createObjects() {
		
		orderDetails = new ArrayList<>();
		
		orderDetailDto = new OrderDetailDto(orderDetailId,quantity,discount,productId,orderId);
		
		orderTest = new Order();
		orderTest.setId(orderId);
		
		orderDetailTest = new OrderDetail();
		orderDetailTest.setId(orderDetailId);
		orderDetailTest.setQuantity(quantity);
		orderDetailTest.setDiscount(discount);
		orderDetailTest.setProductId(productId);
		orderDetailTest.setOrder(orderTest);
		
	}
	
	@Test
	void OrderDetailMapper_fromDto_ReturnOrderDetail() {
		
		OrderDetail orderDetail = mapper.fromDto(orderDetailDto);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()->Assertions.assertThat(orderDetail).isNotNull(),
				()->Assertions.assertThat(orderDetail.getId()).isEqualTo(orderDetailId),
				()->Assertions.assertThat(orderDetail.getQuantity()).isEqualTo(quantity),
				()->Assertions.assertThat(orderDetail.getDiscount()).isEqualTo(discount),
				()->Assertions.assertThat(orderDetail.getProductId()).isEqualTo(productId)
				);
	}

	@Test
	void OrderDetailMapper_toListDto_ReturnListOrderDetailDto() {
		
		orderDetails.add(orderDetailTest);
		List<OrderDetailDto>list = mapper.toListDto(orderDetails);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(list).isNotNull(),
				()-> Assertions.assertThat(list).isNotEmpty(),
				()-> Assertions.assertThat(list).hasSize(1),
				()-> Assertions.assertThat(list.get(0).orderDetailId()).isEqualTo(orderDetailId),
				()-> Assertions.assertThat(list.get(0).quantity()).isEqualTo(quantity),
				()-> Assertions.assertThat(list.get(0).discount()).isEqualTo(discount),
				()-> Assertions.assertThat(list.get(0).productId()).isEqualTo(productId)
				);		
	}
	
	@Test
	void OrderDetailMapper_toSetDto_ReturnSetOrderDetailDto() {
		
		orderDetails.add(orderDetailTest);
		Set<OrderDetailDto> set = mapper.toSetDto(orderDetails);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(set).isNotNull(),
				()-> Assertions.assertThat(set).isNotEmpty(),
				()-> Assertions.assertThat(set).hasSize(1)
				);
	}
}
