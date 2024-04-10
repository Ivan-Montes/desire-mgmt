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
import dev.ime.dto.OrderDetailDto;
import dev.ime.dtomvc.OrderDetailMvcDto;
import dev.ime.exception.EntityAssociatedException;
import dev.ime.exception.ResourceNotFoundException;
import dev.ime.mapper.impl.OrderDetailMvcMapper;


@ExtendWith(MockitoExtension.class)
class OrderDetailServiceTest {

	@Mock
	private MsOrdersClientImpl msOrdersClient;
	
	@Mock
	private OrderDetailMvcMapper orderDetailMvcMapper;
	
	@InjectMocks
	private OrderDetailService orderDetailService;	

	private final Long orderDetailId = 2L;
	private final Long orderId = 7L;
	private final Long productId = 3L;
	private final Integer quantity = 13;
	private final Double discount = 23.9D;
	private List<OrderDetailDto> orderDetailList;
	private OrderDetailDto orderDetailDtoTest;
	private OrderDetailMvcDto orderDetailMvcDtoVoid;
	
	@Mock
	private ResponseEntity<List<OrderDetailDto>> responseListOrderDetailDto;
	private ResponseEntity<OrderDetailDto> responseEntityWithOrderDetailDto;
	private ResponseEntity<OrderDetailDto> responseEntityWithOrderDetailDtoException;
	private ResponseEntity<Boolean> responseEntityBooleanTrue;
	
	@BeforeEach
	private void createObjects() {		

		orderDetailList = new ArrayList<>();		
		orderDetailDtoTest = new OrderDetailDto(orderDetailId,quantity,discount,productId,orderId);
		orderDetailMvcDtoVoid = new OrderDetailMvcDto();
		
		responseEntityWithOrderDetailDto = new ResponseEntity<>(orderDetailDtoTest, HttpStatus.OK);
		responseEntityWithOrderDetailDtoException = new ResponseEntity<>(new OrderDetailDto(), HttpStatus.OK);
		responseEntityBooleanTrue = new ResponseEntity<>(true, HttpStatus.OK);
	}
	
	@Test
	void OrderDetailService_getAll_ReturnListOrderDetailDto() {
		
		orderDetailList.add(orderDetailDtoTest);
		Mockito.when(msOrdersClient.getAllOrderDetail()).thenReturn(responseListOrderDetailDto);
		Mockito.when(responseListOrderDetailDto.getBody()).thenReturn(orderDetailList);
		
		List<OrderDetailDto> list = orderDetailService.getAll();
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(list).isNotNull(),
				()-> Assertions.assertThat(list).isNotEmpty(),
				()-> Assertions.assertThat(list).hasSize(1),
				()-> Assertions.assertThat(list.get(0).orderDetailId()).isEqualTo(orderDetailId),
				()-> Assertions.assertThat(list.get(0).quantity()).isEqualTo(quantity),
				()-> Assertions.assertThat(list.get(0).discount()).isEqualTo(discount),
				()-> Assertions.assertThat(list.get(0).productId()).isEqualTo(productId),
				()-> Assertions.assertThat(list.get(0).orderId()).isEqualTo(orderId)
				);		
	}

	@Test
	void OrderDetailService_getAllPaged_ReturnListOrderDetailDto() {
		
		orderDetailList.add(orderDetailDtoTest);
		Mockito.when(msOrdersClient.getAllOrderDetailPaged(Mockito.anyInt(), Mockito.anyInt())).thenReturn(responseListOrderDetailDto);
		Mockito.when(responseListOrderDetailDto.getBody()).thenReturn(orderDetailList);
		
		List<OrderDetailDto> list = orderDetailService.getAllPaged(65,49);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(list).isNotNull(),
				()-> Assertions.assertThat(list).isNotEmpty(),
				()-> Assertions.assertThat(list).hasSize(1),
				()-> Assertions.assertThat(list.get(0).orderDetailId()).isEqualTo(orderDetailId),
				()-> Assertions.assertThat(list.get(0).quantity()).isEqualTo(quantity),
				()-> Assertions.assertThat(list.get(0).discount()).isEqualTo(discount),
				()-> Assertions.assertThat(list.get(0).productId()).isEqualTo(productId),
				()-> Assertions.assertThat(list.get(0).orderId()).isEqualTo(orderId)
				);		
	}

	@Test
	void OrderDetailService_getById_ReturnOrderDetailDto() {
		
		Mockito.when(msOrdersClient.getOrderDetailById(Mockito.anyLong())).thenReturn(responseEntityWithOrderDetailDto);
		
		OrderDetailDto orderDetailDto = orderDetailService.getById(649L);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(orderDetailDto).isNotNull(),
				()-> Assertions.assertThat(orderDetailDto.orderDetailId()).isEqualTo(orderDetailId),
				()-> Assertions.assertThat(orderDetailDto.quantity()).isEqualTo(quantity),
				()-> Assertions.assertThat(orderDetailDto.discount()).isEqualTo(discount),
				()-> Assertions.assertThat(orderDetailDto.productId()).isEqualTo(productId),
				()-> Assertions.assertThat(orderDetailDto.orderId()).isEqualTo(orderId)
				);		
	}

	@Test
	void OrderDetailService_getById_ReturnResourceNotFoundException() {
		
		Mockito.when(msOrdersClient.getOrderDetailById(Mockito.anyLong())).thenReturn(responseEntityWithOrderDetailDtoException);

		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, ()-> orderDetailService.getById(649L));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(ResourceNotFoundException.class)
				);
	}

	@Test
	void OrderDetailService_create_ReturnOrderDetailDto() {
		
		Mockito.when(msOrdersClient.create(Mockito.any(OrderDetailDto.class))).thenReturn(new ResponseEntity<>(orderDetailDtoTest, HttpStatus.CREATED));
		Mockito.when(orderDetailMvcMapper.fromMvcDtoToDto(Mockito.any(OrderDetailMvcDto.class))).thenReturn(orderDetailDtoTest);
		
		OrderDetailDto orderDetailDto = orderDetailService.create(new OrderDetailMvcDto());
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(orderDetailDto).isNotNull(),
				()-> Assertions.assertThat(orderDetailDto.orderDetailId()).isEqualTo(orderDetailId),
				()-> Assertions.assertThat(orderDetailDto.quantity()).isEqualTo(quantity),
				()-> Assertions.assertThat(orderDetailDto.discount()).isEqualTo(discount),
				()-> Assertions.assertThat(orderDetailDto.productId()).isEqualTo(productId),
				()-> Assertions.assertThat(orderDetailDto.orderId()).isEqualTo(orderId)
				);		
	}

	@Test
	void OrderDetailService_create_ReturnResourceNotFoundException() {

		Mockito.when(msOrdersClient.create(Mockito.any(OrderDetailDto.class))).thenReturn(new ResponseEntity<>(new OrderDetailDto(), HttpStatus.CREATED));
		Mockito.when(orderDetailMvcMapper.fromMvcDtoToDto(Mockito.any(OrderDetailMvcDto.class))).thenReturn(orderDetailDtoTest);
		
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, ()-> orderDetailService.create(orderDetailMvcDtoVoid));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(ResourceNotFoundException.class)
				);
	}

	@Test
	void OrderDetailService_update_ReturnOrderDetailDto() {		

		Mockito.when(msOrdersClient.update(Mockito.anyLong(), Mockito.any(OrderDetailDto.class))).thenReturn(responseEntityWithOrderDetailDto);
		Mockito.when(orderDetailMvcMapper.fromMvcDtoToDto(Mockito.any(OrderDetailMvcDto.class))).thenReturn(orderDetailDtoTest);		

		OrderDetailDto orderDetailDto = orderDetailService.update(orderDetailId,new OrderDetailMvcDto());
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(orderDetailDto).isNotNull(),
				()-> Assertions.assertThat(orderDetailDto.orderDetailId()).isEqualTo(orderDetailId),
				()-> Assertions.assertThat(orderDetailDto.quantity()).isEqualTo(quantity),
				()-> Assertions.assertThat(orderDetailDto.discount()).isEqualTo(discount),
				()-> Assertions.assertThat(orderDetailDto.productId()).isEqualTo(productId),
				()-> Assertions.assertThat(orderDetailDto.orderId()).isEqualTo(orderId)
				);		
	}

	@Test
	void OrderDetailService_update_ReturnResourceNotFoundException() {

		Mockito.when(msOrdersClient.update(Mockito.anyLong(), Mockito.any(OrderDetailDto.class))).thenReturn(responseEntityWithOrderDetailDtoException);
		Mockito.when(orderDetailMvcMapper.fromMvcDtoToDto(Mockito.any(OrderDetailMvcDto.class))).thenReturn(orderDetailDtoTest);		

		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, ()-> orderDetailService.update(orderDetailId, orderDetailMvcDtoVoid));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(ResourceNotFoundException.class)
				);
	}

	@Test
	void OrderDetailService_delete_ReturnBooleanTrue() {
		
		Mockito.when(msOrdersClient.deleteOrderDetail(Mockito.anyLong())).thenReturn(responseEntityBooleanTrue);
		
		Boolean resultValue = orderDetailService.delete(06L);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(resultValue).isNotNull(),
				()-> Assertions.assertThat(resultValue).isTrue()
				);
	}

	@Test
	void OrderDetailService_delete_ReturnEntityAssociatedException() {
		
		Mockito.when(msOrdersClient.deleteOrderDetail(Mockito.anyLong())).thenReturn(new ResponseEntity<>(null, HttpStatus.OK));
		
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(EntityAssociatedException.class, () -> orderDetailService.delete(orderDetailId));		

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(EntityAssociatedException.class)
				);		
	}

	@Test
	void OrderDetailService_setOrder_ReturnBooleanTrue() {
		
		Mockito.when(msOrdersClient.setOrder(Mockito.anyLong(), Mockito.anyLong())).thenReturn(responseEntityBooleanTrue);
		
		Boolean resultValue = orderDetailService.setOrder(23l,06L);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(resultValue).isNotNull(),
				()-> Assertions.assertThat(resultValue).isTrue()
				);
	}

	@Test
	void OrderDetailService_setOrder_ReturnResourceNotFoundException() {
		
		Mockito.when(msOrdersClient.setOrder(Mockito.anyLong(), Mockito.anyLong())).thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, () -> orderDetailService.setOrder(23l,06L));		

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(ResourceNotFoundException.class)
				);		
	}
	
	

}
