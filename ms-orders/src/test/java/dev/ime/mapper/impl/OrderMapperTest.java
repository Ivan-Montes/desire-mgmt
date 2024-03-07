package dev.ime.mapper.impl;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import dev.ime.dto.OrderDto;
import dev.ime.entity.Order;
import dev.ime.tool.Transformer;


@ExtendWith(MockitoExtension.class)
class OrderMapperTest {

	@Mock
	private Transformer transformer;
	
	@InjectMocks
	private OrderMapper mapper;	
	
	private List<Order> orders;
	private OrderDto orderDtoTest;
	private Order orderTest;
	private final Long orderId = 7L;
	private final Long customerId = 3L;
	private final String orderDate = "2013-07-20";
	
	@BeforeEach
	private void createObjects() {
		
		orderDtoTest = new OrderDto(orderId,customerId,orderDate);
		orders = new ArrayList<>();
		orderTest = new Order();
		orderTest.setId(orderId);
		orderTest.setCustomerId(customerId);
		orderTest.setOrderDate(LocalDate.parse(orderDate));
	}
	
	@Test
	void OrderMapper_fromDto_ReturnOrder() {
		
		Mockito.when(transformer.fromStringToDateTimeOnErrorZeroes(Mockito.anyString())).thenReturn(LocalDate.parse(orderDate));
		Order order = mapper.fromDto(orderDtoTest);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()->Assertions.assertThat(order).isNotNull(),
				()->Assertions.assertThat(order.getId()).isEqualTo(orderId),
				()->Assertions.assertThat(order.getCustomerId()).isEqualTo(customerId)
				);
	}

	@Test
	void OrderMapper_toDto_ReturnListOrderDto() {
		
		orders.add(orderTest);
		List<OrderDto>list = mapper.toListDto(orders);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(list).isNotNull(),
				()-> Assertions.assertThat(list).isNotEmpty(),
				()-> Assertions.assertThat(list).hasSize(1),
				()-> Assertions.assertThat(list.get(0).orderId()).isEqualTo(orderId),
				()-> Assertions.assertThat(list.get(0).customerId()).isEqualTo(customerId)		
		);
		
	}
	
	
}
