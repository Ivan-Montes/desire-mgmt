package dev.ime.service.impl;


import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import dev.ime.client.impl.MsOrdersClientImpl;
import dev.ime.dto.OrderDto;
import dev.ime.dtomvc.OrderMvcDto;
import dev.ime.exception.EntityAssociatedException;
import dev.ime.exception.ResourceNotFoundException;
import dev.ime.mapper.impl.OrderMvcMapper;


@ExtendWith(MockitoExtension.class)
class OrderServiceTest {


	@Mock
	private MsOrdersClientImpl msOrdersClient;

	@Mock
	private OrderMvcMapper orderMvcMapper;
	
	@InjectMocks
	private OrderService orderService;
	

	private List<OrderDto> orderDtoList;
	private OrderDto orderDtoTest;
	private OrderMvcDto orderMvcDtoVoid;
	
	private final Long orderId = 7L;
	private final Long customerId = 3L;
	private final String orderDate = "2013-07-20";
	
	@Mock
	private ResponseEntity<List<OrderDto>> responseListOrderDto;
	private ResponseEntity<OrderDto> responseEntityWithOrderDto;
	private ResponseEntity<OrderDto> responseEntityWithOrderDtoException;
	private ResponseEntity<Boolean> responseEntityBooleanTrue;
	
	
	@BeforeEach
	private void createObjects() {
	
		orderDtoList = new ArrayList<>();
		orderDtoTest = new OrderDto(orderId,customerId,orderDate);
		orderMvcDtoVoid = new OrderMvcDto();

		responseEntityWithOrderDto = new ResponseEntity<>(orderDtoTest, HttpStatus.OK);
		responseEntityWithOrderDtoException = new ResponseEntity<>(new OrderDto(), HttpStatus.OK);
		responseEntityBooleanTrue = new ResponseEntity<>(true, HttpStatus.OK);
	}

	@Test
	void OrderService_getAll_ReturnListOrderDto() {
		
		orderDtoList.add(orderDtoTest);
		Mockito.when(msOrdersClient.getAllOrder()).thenReturn(responseListOrderDto);
		Mockito.when(responseListOrderDto.getBody()).thenReturn(orderDtoList);
		
		List<OrderDto> list = orderService.getAll();
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(list).isNotNull(),
				()-> Assertions.assertThat(list).isNotEmpty(),
				()-> Assertions.assertThat(list).hasSize(1),
				()-> Assertions.assertThat(list.get(0).orderId()).isEqualTo(orderId),
				()-> Assertions.assertThat(list.get(0).customerId()).isEqualTo(customerId),
				()-> Assertions.assertThat(list.get(0).orderDate()).isEqualTo(orderDate)
				);
	}

	@Test
	void OrderService_getAllPaged_ReturnListOrderDto() {
		
		orderDtoList.add(orderDtoTest);
		Mockito.when(msOrdersClient.getAllOrderPaged(Mockito.anyInt(), Mockito.anyInt())).thenReturn(responseListOrderDto);
		Mockito.when(responseListOrderDto.getBody()).thenReturn(orderDtoList);
		
		List<OrderDto> list = orderService.getAllPaged(29,26);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(list).isNotNull(),
				()-> Assertions.assertThat(list).isNotEmpty(),
				()-> Assertions.assertThat(list).hasSize(1),
				()-> Assertions.assertThat(list.get(0).orderId()).isEqualTo(orderId),
				()-> Assertions.assertThat(list.get(0).customerId()).isEqualTo(customerId),
				()-> Assertions.assertThat(list.get(0).orderDate()).isEqualTo(orderDate)
				);
	}

	@Test
	void OrderService_getById_ReturnOrderDto() {
		
		Mockito.when(msOrdersClient.getOrderById(Mockito.anyLong())).thenReturn(responseEntityWithOrderDto);
		
		OrderDto orderDto = orderService.getById(57L);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(orderDto).isNotNull(),
				()-> Assertions.assertThat(orderDto.orderId()).isEqualTo(orderId),
				()-> Assertions.assertThat(orderDto.customerId()).isEqualTo(customerId),
				()-> Assertions.assertThat(orderDto.orderDate()).isEqualTo(orderDate)
				);
	}

	@Test
	void OrderService_getById_ReturnResourceNotFoundException() {

		Mockito.when(msOrdersClient.getOrderById(Mockito.anyLong())).thenReturn(responseEntityWithOrderDtoException);
		
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, ()-> orderService.getById(57L));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(ResourceNotFoundException.class)
				);		
	}

	@Test
	void OrderService_create_ReturnOrderDto() {
		
		Mockito.when(msOrdersClient.create(Mockito.any(OrderDto.class))).thenReturn(new ResponseEntity<>(orderDtoTest, HttpStatus.CREATED));
		Mockito.when(orderMvcMapper.fromMvcDtoToDto(Mockito.any(OrderMvcDto.class))).thenReturn(orderDtoTest);
		
		OrderDto orderDto = orderService.create(new OrderMvcDto());
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(orderDto).isNotNull(),
				()-> Assertions.assertThat(orderDto.orderId()).isEqualTo(orderId),
				()-> Assertions.assertThat(orderDto.customerId()).isEqualTo(customerId),
				()-> Assertions.assertThat(orderDto.orderDate()).isEqualTo(orderDate)
				);
	}

	@Test
	void OrderService_create_ReturnResourceNotFoundException() {
		
		Mockito.when(msOrdersClient.create(Mockito.any(OrderDto.class))).thenReturn(new ResponseEntity<>(new OrderDto(), HttpStatus.CREATED));
		Mockito.when(orderMvcMapper.fromMvcDtoToDto(Mockito.any(OrderMvcDto.class))).thenReturn(orderDtoTest);
		
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, ()-> orderService.create(orderMvcDtoVoid));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(ResourceNotFoundException.class)
				);		
	}

	@Test
	void OrderService_update_ReturnOrderDto() {
		
		Mockito.when(msOrdersClient.update(Mockito.anyLong(),Mockito.any(OrderDto.class))).thenReturn(responseEntityWithOrderDto);
		Mockito.when(orderMvcMapper.fromMvcDtoToDto(Mockito.any(OrderMvcDto.class))).thenReturn(orderDtoTest);
		
		OrderDto orderDto = orderService.update(orderId, new OrderMvcDto());
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(orderDto).isNotNull(),
				()-> Assertions.assertThat(orderDto.orderId()).isEqualTo(orderId),
				()-> Assertions.assertThat(orderDto.customerId()).isEqualTo(customerId),
				()-> Assertions.assertThat(orderDto.orderDate()).isEqualTo(orderDate)
				);
	}

	@Test
	void OrderService_update_ReturnResourceNotFoundException() {
		
		Mockito.when(msOrdersClient.update(Mockito.anyLong(),Mockito.any(OrderDto.class))).thenReturn(responseEntityWithOrderDtoException);
		Mockito.when(orderMvcMapper.fromMvcDtoToDto(Mockito.any(OrderMvcDto.class))).thenReturn(orderDtoTest);
		
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, ()-> orderService.update(orderId,orderMvcDtoVoid));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(ResourceNotFoundException.class)
				);		
	}

	@Test
	void OrderService_delete_ReturnBooleanTrue() {

		Mockito.when(msOrdersClient.deleteOrder(Mockito.anyLong())).thenReturn(responseEntityBooleanTrue);
		
		Boolean resultValue = orderService.delete(26L);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(resultValue).isNotNull(),
				()-> Assertions.assertThat(resultValue).isTrue()
				);
	}

	@Test
	void OrderService_delete_ReturnEntityAssociatedException() {
		
		Mockito.when(msOrdersClient.deleteOrder(Mockito.anyLong())).thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(EntityAssociatedException.class, () -> orderService.delete(26L));		

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(EntityAssociatedException.class)
				);		
	}

	@Test
	void OrderService_addOrderDetail_ReturnBooleanTrue() {
		
		Mockito.when(msOrdersClient.addOrderDetail(Mockito.anyLong(), Mockito.anyLong())).thenReturn(responseEntityBooleanTrue);
		
		Boolean resultValue = orderService.addOrderDetail(26L, 80l);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(resultValue).isNotNull(),
				()-> Assertions.assertThat(resultValue).isTrue()
				);		
	}

	@Test
	void OrderService_addOrderDetail_ReturnResourceNotFoundException() {
		
		Mockito.when(msOrdersClient.addOrderDetail(Mockito.anyLong(), Mockito.anyLong())).thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, () -> orderService.addOrderDetail(26L, 80l));		

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(ResourceNotFoundException.class)
				);	
	}
}
