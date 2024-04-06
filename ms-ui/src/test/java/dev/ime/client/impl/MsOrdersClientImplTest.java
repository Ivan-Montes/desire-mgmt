package dev.ime.client.impl;


import java.util.Collections;
import java.util.List;
import java.util.Set;

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

import dev.ime.client.MsOrdersClient;
import dev.ime.dto.OrderDetailDto;
import dev.ime.dto.OrderDto;
import dev.ime.dtomvc.OrderDetailMvcDto;
import dev.ime.dtomvc.OrderMvcDto;
import dev.ime.mapper.impl.OrderDetailMvcMapper;
import dev.ime.mapper.impl.OrderMvcMapper;

@ExtendWith(MockitoExtension.class)
class MsOrdersClientImplTest {

	@Mock
	private MsOrdersClient msOrdersClient;
	
	@InjectMocks
	private MsOrdersClientImpl msOrdersClientImpl;
	

	private ResponseEntity<Boolean> responseEntityBoolTrue;
	private ResponseEntity<OrderDto> responseEntityWithOrderDtoEmpy;
	private ResponseEntity<List<OrderDto>> responseEntityWithListOrderDtoEmpy;	
	private OrderMvcMapper orderMvcMapper;	
	private OrderDto orderDtoTest;

	private ResponseEntity<OrderDetailDto> responseEntityWithOrderDetailDtoEmpy;
	private ResponseEntity<List<OrderDetailDto>> responseEntityWithListOrderDetailDtoEmpy;
	private ResponseEntity<Set<OrderDetailDto>> responseEntityWithSetOrderDetailDtoEmpy;
	private OrderDetailMvcMapper orderDetailMvcMapper;	 
	private OrderDetailDto orderDetailDtoTest;
	
	
	
	@BeforeEach
	private void createObjects() {
		
		responseEntityBoolTrue = new ResponseEntity<>(true, HttpStatus.OK);
		responseEntityWithOrderDtoEmpy = new ResponseEntity<>(new OrderDto(), HttpStatus.OK);
		responseEntityWithListOrderDtoEmpy = new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);		
		orderMvcMapper = new OrderMvcMapper();		
		orderDtoTest = orderMvcMapper.fromMvcDtoToDto(new OrderMvcDto());

		responseEntityWithOrderDetailDtoEmpy = new ResponseEntity<>(new OrderDetailDto(), HttpStatus.OK);
		responseEntityWithListOrderDetailDtoEmpy = new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);	
		responseEntityWithSetOrderDetailDtoEmpy = new ResponseEntity<>(Collections.emptySet(), HttpStatus.OK);
		orderDetailMvcMapper = new OrderDetailMvcMapper();
		orderDetailDtoTest = orderDetailMvcMapper.fromMvcDtoToDto(new OrderDetailMvcDto());		
		
	}
	

	@Test
	void MsOrdersClientImpl_getAllOrder_ReturnResponseEntityListOrderDto() {
		
		Mockito.when(msOrdersClient.getAllOrder()).thenReturn(responseEntityWithListOrderDtoEmpy);
		
		ResponseEntity<List<OrderDto>> response = msOrdersClientImpl.getAllOrder();
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK)
				);
	}

	@Test
	void MsOrdersClientImpl_getAllOrder_ReturnResponseEntityListOrderDtoError() {
		
		Mockito.when(msOrdersClient.getAllOrder()).thenThrow(new RuntimeException());
		
		ResponseEntity<List<OrderDto>> response = msOrdersClientImpl.getAllOrder();
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND)
				);
	}

	@Test
	void MsOrdersClientImpl_getAllOrderPaged_ReturnResponseEntityListOrderDto() {
		
		Mockito.when(msOrdersClient.getAllOrderPaged(Mockito.anyInt(),Mockito.anyInt())).thenReturn(responseEntityWithListOrderDtoEmpy);
		
		ResponseEntity<List<OrderDto>> response = msOrdersClientImpl.getAllOrderPaged(3,3);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK)
				);
	}

	@Test
	void MsOrdersClientImpl_getAllOrderPaged_ReturnResponseEntityListOrderDtoError() {
		
		Mockito.when(msOrdersClient.getAllOrderPaged(Mockito.anyInt(),Mockito.anyInt())).thenThrow(new RuntimeException());
		
		ResponseEntity<List<OrderDto>> response = msOrdersClientImpl.getAllOrderPaged(3,3);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND)
				);
	}

	@Test
	void MsOrdersClientImpl_getOrderById_ReturnResponseEntityOrderDto() {
		
		Mockito.when(msOrdersClient.getOrderById(Mockito.anyLong())).thenReturn(responseEntityWithOrderDtoEmpy);
		
		ResponseEntity<OrderDto> response = msOrdersClientImpl.getOrderById(1L);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK)
				);
	}

	@Test
	void MsOrdersClientImpl_getOrderById_ReturnResponseEntityOrderDtoError() {
		
		Mockito.when(msOrdersClient.getOrderById(Mockito.anyLong())).thenThrow(new RuntimeException());
		
		ResponseEntity<OrderDto> response = msOrdersClientImpl.getOrderById(1L);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND)
				);
	}

	@Test
	void MsOrdersClientImpl_create_ReturnResponseEntityOrderDto() {
		
		Mockito.when(msOrdersClient.create(Mockito.any(OrderDto.class))).thenReturn(responseEntityWithOrderDtoEmpy);
		
		ResponseEntity<OrderDto> response = msOrdersClientImpl.create(orderDtoTest);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK)
				);
	}

	@Test
	void MsOrdersClientImpl_create_ReturnResponseEntityOrderDtoError() {
		
		Mockito.when(msOrdersClient.create(Mockito.any(OrderDto.class))).thenThrow(new RuntimeException());
		
		ResponseEntity<OrderDto> response = msOrdersClientImpl.create(orderDtoTest);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND)
				);
	}

	@Test
	void MsOrdersClientImpl_update_ReturnResponseEntityOrderDto() {
		
		Mockito.when(msOrdersClient.update(Mockito.anyLong(), Mockito.any(OrderDto.class))).thenReturn(responseEntityWithOrderDtoEmpy);
		
		ResponseEntity<OrderDto> response = msOrdersClientImpl.update(3L, orderDtoTest);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK)
				);
	}

	@Test
	void MsOrdersClientImpl_update_ReturnResponseEntityOrderDtoError() {
		
		Mockito.when(msOrdersClient.update(Mockito.anyLong(), Mockito.any(OrderDto.class))).thenThrow(new RuntimeException());
		
		ResponseEntity<OrderDto> response = msOrdersClientImpl.update(3L, orderDtoTest);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND)
				);
	}

	@Test
	void MsOrdersClientImpl_delete_ReturnResponseEntityBooleanTrue() {
		
		Mockito.when(msOrdersClient.deleteOrder(Mockito.anyLong())).thenReturn(responseEntityBoolTrue);
		
		ResponseEntity<Boolean> response = msOrdersClientImpl.deleteOrder(4L);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
				()-> Assertions.assertThat(response.getBody().booleanValue())
				);		
	}

	@Test
	void MsOrdersClientImpl_delete_ReturnResponseEntityBooleanTrueError() {
		
		Mockito.when(msOrdersClient.deleteOrder(Mockito.anyLong())).thenThrow(new RuntimeException());
		
		ResponseEntity<Boolean> response = msOrdersClientImpl.deleteOrder(4L);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND),
				()-> Assertions.assertThat(response.getBody().booleanValue())
				);		
	}

	@Test
	void MsOrdersClientImpl_getLinesByOrderId_ReturnResponseEntitySetOrderDetailDto() {
		
		Mockito.when(msOrdersClient.getLinesByOrderId(Mockito.anyLong())).thenReturn(responseEntityWithSetOrderDetailDtoEmpy);
		
		ResponseEntity<Set<OrderDetailDto>> response = msOrdersClientImpl.getLinesByOrderId(5L);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK)
				);
	}

	@Test
	void MsOrdersClientImpl_getLinesByOrderId_ReturnResponseEntitySetOrderDetailDtoError() {
		
		Mockito.when(msOrdersClient.getLinesByOrderId(Mockito.anyLong())).thenThrow(new RuntimeException());
		
		ResponseEntity<Set<OrderDetailDto>> response = msOrdersClientImpl.getLinesByOrderId(5L);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND)
				);
	}

	@Test
	void MsOrdersClientImpl_addOrderDetail_ReturnResponseEntityBooleanTrue() {
		
		Mockito.when(msOrdersClient.addOrderDetail(Mockito.anyLong(),Mockito.anyLong())).thenReturn(responseEntityBoolTrue);
		
		ResponseEntity<Boolean> response = msOrdersClientImpl.addOrderDetail(4L,6l);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
				()-> Assertions.assertThat(response.getBody().booleanValue())
				);		
	}

	@Test
	void MsOrdersClientImpl_addOrderDetail_ReturnResponseEntityBooleanTrueError() {
		
		Mockito.when(msOrdersClient.addOrderDetail(Mockito.anyLong(),Mockito.anyLong())).thenThrow(new RuntimeException());
		
		ResponseEntity<Boolean> response = msOrdersClientImpl.addOrderDetail(4L,6l);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND),
				()-> Assertions.assertThat(response.getBody().booleanValue())
				);		
	}

	@Test
	void MsOrdersClientImpl_getAllOrderDetail_ReturnResponseEntityListOrderDetailDto() {
		
		Mockito.when(msOrdersClient.getAllOrderDetail()).thenReturn(responseEntityWithListOrderDetailDtoEmpy);
		
		ResponseEntity<List<OrderDetailDto>> response = msOrdersClientImpl.getAllOrderDetail();
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK)
				);
	}

	@Test
	void MsOrdersClientImpl_getAllOrderDetail_ReturnResponseEntityListOrderDetailDtoError() {
		
		Mockito.when(msOrdersClient.getAllOrderDetail()).thenThrow(new RuntimeException());
		
		ResponseEntity<List<OrderDetailDto>> response = msOrdersClientImpl.getAllOrderDetail();
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND)
				);
	}

	@Test
	void MsOrdersClientImpl_getAllOrderDetailPaged_ReturnResponseEntityListOrderDetailDto() {
		
		Mockito.when(msOrdersClient.getAllOrderDetailPaged(Mockito.anyInt(),Mockito.anyInt())).thenReturn(responseEntityWithListOrderDetailDtoEmpy);
		
		ResponseEntity<List<OrderDetailDto>> response = msOrdersClientImpl.getAllOrderDetailPaged(Mockito.anyInt(),Mockito.anyInt());
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK)
				);
	}

	@Test
	void MsOrdersClientImpl_getAllOrderDetailPaged_ReturnResponseEntityListOrderDetailDtoError() {
		
		Mockito.when(msOrdersClient.getAllOrderDetailPaged(Mockito.anyInt(),Mockito.anyInt())).thenThrow(new RuntimeException());
		
		ResponseEntity<List<OrderDetailDto>> response = msOrdersClientImpl.getAllOrderDetailPaged(Mockito.anyInt(),Mockito.anyInt());
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND)
				);
	}

	@Test
	void MsOrdersClientImpl_getOrderDetailById_ReturnResponseEntityOrderDetailDto() {
		
		Mockito.when(msOrdersClient.getOrderDetailById(Mockito.anyLong())).thenReturn(responseEntityWithOrderDetailDtoEmpy);
		
		ResponseEntity<OrderDetailDto> response = msOrdersClientImpl.getOrderDetailById(1L);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK)
				);
	}

	@Test
	void MsOrdersClientImpl_getOrderDetailById_ReturnResponseEntityOrderDetailDtoError() {
		
		Mockito.when(msOrdersClient.getOrderDetailById(Mockito.anyLong())).thenThrow(new RuntimeException());
		
		ResponseEntity<OrderDetailDto> response = msOrdersClientImpl.getOrderDetailById(1L);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND)
				);
	}

	@Test
	void MsOrdersClientImpl_create_ReturnResponseEntityOrderDetailDto() {
		
		Mockito.when(msOrdersClient.create(Mockito.any(OrderDetailDto.class))).thenReturn(responseEntityWithOrderDetailDtoEmpy);
		
		ResponseEntity<OrderDetailDto> response = msOrdersClientImpl.create(orderDetailDtoTest);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK)
				);
	}


	@Test
	void MsOrdersClientImpl_create_ReturnResponseEntityOrderDetailDtoError() {
		
		Mockito.when(msOrdersClient.create(Mockito.any(OrderDetailDto.class))).thenThrow(new RuntimeException());
		
		ResponseEntity<OrderDetailDto> response = msOrdersClientImpl.create(orderDetailDtoTest);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND)
				);
	}

	@Test
	void MsOrdersClientImpl_update_ReturnResponseEntityOrderDetailDto() {
		
		Mockito.when(msOrdersClient.update(Mockito.anyLong(),Mockito.any(OrderDetailDto.class))).thenReturn(responseEntityWithOrderDetailDtoEmpy);
		
		ResponseEntity<OrderDetailDto> response = msOrdersClientImpl.update(6l,orderDetailDtoTest);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK)
				);
	}


	@Test
	void MsOrdersClientImpl_update_ReturnResponseEntityOrderDetailDtoError() {
		
		Mockito.when(msOrdersClient.update(Mockito.anyLong(),Mockito.any(OrderDetailDto.class))).thenThrow(new RuntimeException());
		
		ResponseEntity<OrderDetailDto> response = msOrdersClientImpl.update(7L,orderDetailDtoTest);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND)
				);
	}

	@Test
	void MsOrdersClientImpl_deleteOrderDetail_ReturnResponseEntityBooleanTrue() {
		
		Mockito.when(msOrdersClient.deleteOrderDetail(Mockito.anyLong())).thenReturn(responseEntityBoolTrue);
		
		ResponseEntity<Boolean> response = msOrdersClientImpl.deleteOrderDetail(4L);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
				()-> Assertions.assertThat(response.getBody().booleanValue())
				);		
	}

	@Test
	void MsOrdersClientImpl_deleteOrderDetail_ReturnResponseEntityBooleanTrueError() {
		
		Mockito.when(msOrdersClient.deleteOrderDetail(Mockito.anyLong())).thenThrow(new RuntimeException());
		
		ResponseEntity<Boolean> response = msOrdersClientImpl.deleteOrderDetail(4L);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND),
				()-> Assertions.assertThat(response.getBody().booleanValue())
				);		
	}

	@Test
	void MsOrdersClientImpl_setOrder_ReturnResponseEntityOrderDetailDto() {
		
		Mockito.when(msOrdersClient.setOrder(Mockito.anyLong(),Mockito.anyLong())).thenReturn(responseEntityBoolTrue);
		
		ResponseEntity<Boolean> response =  msOrdersClientImpl.setOrder(8l,9l);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
				()-> Assertions.assertThat(response.getBody().booleanValue())
				);
	}

	@Test
	void MsOrdersClientImpl_setOrder_ReturnResponseEntityOrderDetailDtoError() {
		
		Mockito.when(msOrdersClient.setOrder(Mockito.anyLong(),Mockito.anyLong())).thenThrow(new RuntimeException());
		
		ResponseEntity<Boolean> response = msOrdersClientImpl.setOrder(9l,8l);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND),
				()-> Assertions.assertThat(response.getBody().booleanValue())
				);
	}

	@Test
	void MsOrdersClientImpl_getAnyByProductId_ReturnResponseEntityBooleanTrue() {
		
		Mockito.when(msOrdersClient.getAnyByProductId(Mockito.anyLong())).thenReturn(responseEntityBoolTrue);
		
		ResponseEntity<Boolean> response = msOrdersClientImpl.getAnyByProductId(10l);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
				()-> Assertions.assertThat(response.getBody().booleanValue())
				);
	}

	@Test
	void MsOrdersClientImpl_getAnyByProductId_ReturnResponseEntityBooleanTrueError() {
		
		Mockito.when(msOrdersClient.getAnyByProductId(Mockito.anyLong())).thenThrow(new RuntimeException());
		
		ResponseEntity<Boolean> response = msOrdersClientImpl.getAnyByProductId(10l);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND),
				()-> Assertions.assertThat(response.getBody().booleanValue())
				);
	}
	
	
}
