package dev.ime.resource.impl;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import dev.ime.dto.OrderDetailDto;
import dev.ime.dto.OrderDto;
import dev.ime.entity.Order;
import dev.ime.entity.OrderDetail;
import dev.ime.mapper.impl.OrderDetailMapper;
import dev.ime.mapper.impl.OrderMapper;
import dev.ime.service.impl.OrderServiceImpl;

@WebMvcTest(OrderResource.class)
@AutoConfigureMockMvc(addFilters = false)
class OrderResourceTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private OrderServiceImpl orderService;
	
	@MockBean
	private OrderMapper orderMapper;
	
	@MockBean
	private OrderDetailMapper orderDetailMapper;
	
	@Autowired
    private ObjectMapper objectMapper;
	
	@MockBean
	private Logger logger;
	
	private final String path = "/api/orders";
	private List<OrderDto> ordersDtos;
	private List<Order> orders;
	private Set<OrderDetailDto> orderDetailDtoSet;
	private OrderDto orderDtoTest;
	private Order orderTest;
	private OrderDetail orderDetailTest;
	private OrderDetailDto orderDetailDto;
	private final Long orderId = 7L;
	private final Long customerId = 3L;
	private final String orderDate = "2013-07-20";
	private final Long orderDetailId = 12L;
	private final Long productId = 3L;
	private final Integer quantity = 13;
	private final Double discount = 23.9D;
	
	@BeforeEach
	private void createObjects() {
		
		orderDtoTest = new OrderDto(orderId,customerId,orderDate);
		
		orders = new ArrayList<>();
		ordersDtos = new ArrayList<>();
		orderDetailDtoSet = new HashSet<>();
		
		orderTest = new Order();
		orderTest.setId(orderId);
		orderTest.setCustomerId(customerId);
		orderTest.setOrderDate(LocalDate.parse(orderDate));
		
		orderDetailTest = new OrderDetail();
		orderDetailTest.setId(orderDetailId);
		
		orderDetailDto = new OrderDetailDto(orderDetailId,quantity,discount,productId,orderId);
	}
	
	@Test
	void OrderResource_getAll_ReturnEmptyList() throws Exception{
		
		Mockito.when(orderService.getAll()).thenReturn(orders);
		
		mockMvc.perform(MockMvcRequestBuilders.get(path))
		.andExpect(MockMvcResultMatchers.status().isNoContent())
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.notNullValue()))
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.empty()))
		;		
	}

	@Test
	void OrderResource_getAll_ReturnPagedListDefaultSize() throws Exception{
		
		orders.add(orderTest);
		ordersDtos.add(orderDtoTest);
		Mockito.when(orderService.getAllPaged(Mockito.anyInt(),Mockito.anyInt())).thenReturn(orders);
		Mockito.when(orderMapper.toListDto(Mockito.anyList())).thenReturn(ordersDtos);
		
		mockMvc.perform(MockMvcRequestBuilders.get(path)
				.param("page", "1"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.notNullValue()))
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.hasSize(1)))
		.andExpect(MockMvcResultMatchers.jsonPath("$[0].orderId", org.hamcrest.Matchers.equalTo(7)))
		;		
	}	

	@Test
	void OrderResource_getAll_ReturnPagedList() throws Exception{
		
		orders.add(orderTest);
		ordersDtos.add(orderDtoTest);
		Mockito.when(orderService.getAllPaged(Mockito.anyInt(),Mockito.anyInt())).thenReturn(orders);
		Mockito.when(orderMapper.toListDto(Mockito.anyList())).thenReturn(ordersDtos);
		
		mockMvc.perform(MockMvcRequestBuilders.get(path)
				.param("page", "1")
		.		param("size", "1"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.notNullValue()))
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.hasSize(1)))
		.andExpect(MockMvcResultMatchers.jsonPath("$[0].orderId", org.hamcrest.Matchers.equalTo(7)))
		;		
	}

	@Test
	void OrderResource_getById_ReturnResponseWithOrderDto() throws Exception {
		
		Mockito.when(orderService.getById(Mockito.anyLong())).thenReturn(Optional.of(orderTest));
		Mockito.when(orderMapper.toDto(Mockito.any(Order.class))).thenReturn(orderDtoTest);
		
		mockMvc.perform(MockMvcRequestBuilders.get(path + "/{id}", orderId))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.notNullValue()))
		.andExpect(MockMvcResultMatchers.jsonPath("$.orderId", org.hamcrest.Matchers.equalTo(7) ))
		;
	}
	

	@Test
	void OrderResource_getById_ReturnResponseWithNotFound() throws Exception {
		
		Mockito.when(orderService.getById(Mockito.anyLong())).thenReturn(Optional.empty());

		mockMvc.perform(MockMvcRequestBuilders.get(path + "/{id}", orderId))
		.andExpect(MockMvcResultMatchers.status().isNotFound())
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.notNullValue()))
		.andExpect(MockMvcResultMatchers.jsonPath("$.orderId",  org.hamcrest.Matchers.equalTo(0)))
		.andExpect(MockMvcResultMatchers.jsonPath("$.customerId",  org.hamcrest.Matchers.equalTo(0)))
		;
	}

	@Test
	void OrderResource_create_ReturnResponseWithOrderDto() throws Exception {
		
		Mockito.when(orderService.create(Mockito.any(OrderDto.class))).thenReturn(Optional.of(orderTest));
		Mockito.when(orderMapper.toDto(Mockito.any(Order.class))).thenReturn(orderDtoTest);
		
		mockMvc.perform(MockMvcRequestBuilders.post(path)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(orderDtoTest))
				)
		.andExpect(MockMvcResultMatchers.status().isCreated())
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.notNullValue()))
		.andExpect(MockMvcResultMatchers.jsonPath("$.customerId", org.hamcrest.Matchers.equalTo(3)))
		.andExpect(MockMvcResultMatchers.jsonPath("$.orderDate", org.hamcrest.Matchers.equalTo(orderDate)))
		;		
	}

	@Test
	void OrderResource_create_ReturnResponseWithNotFound() throws Exception {
		
		Mockito.when(orderService.create(Mockito.any(OrderDto.class))).thenReturn(Optional.empty());
		
		mockMvc.perform(MockMvcRequestBuilders.post(path)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(orderDtoTest))
				)
		.andExpect(MockMvcResultMatchers.status().isNotFound())
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.notNullValue()))
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.notNullValue()))
		.andExpect(MockMvcResultMatchers.jsonPath("$.orderId",  org.hamcrest.Matchers.equalTo(0)))
		.andExpect(MockMvcResultMatchers.jsonPath("$.customerId",  org.hamcrest.Matchers.equalTo(0)))
		;		
	}

	@Test
	void OrderResource_update_ReturnResponseWithOrderDto() throws Exception {
		
		Mockito.when(orderService.update(Mockito.anyLong(),Mockito.any(OrderDto.class))).thenReturn(Optional.of(orderTest));
		Mockito.when(orderMapper.toDto(Mockito.any(Order.class))).thenReturn(orderDtoTest);
		
		mockMvc.perform(MockMvcRequestBuilders.put(path + "/{id}", orderId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(orderDtoTest))
				)
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.notNullValue()))
		.andExpect(MockMvcResultMatchers.jsonPath("$.orderId",  org.hamcrest.Matchers.equalTo(7)))
		.andExpect(MockMvcResultMatchers.jsonPath("$.customerId", org.hamcrest.Matchers.equalTo(3)))
		.andExpect(MockMvcResultMatchers.jsonPath("$.orderDate", org.hamcrest.Matchers.equalTo(orderDate)))
		;
	}

	@Test
	void OrderResource_update_ReturnResponseWithNotFound() throws Exception {
		
		Mockito.when(orderService.update(Mockito.anyLong(),Mockito.any(OrderDto.class))).thenReturn(Optional.empty());
		
		mockMvc.perform(MockMvcRequestBuilders.put(path + "/{id}", orderId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(orderDtoTest))
				)
		.andExpect(MockMvcResultMatchers.status().isNotFound())
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.notNullValue()))
		.andExpect(MockMvcResultMatchers.jsonPath("$.orderId",  org.hamcrest.Matchers.equalTo(0)))
		.andExpect(MockMvcResultMatchers.jsonPath("$.customerId",  org.hamcrest.Matchers.equalTo(0)))
		;		
	}

	@Test
	void OrderResource_delete_ReturnResponseWithBooleanTrue() throws Exception {
		
		Mockito.when(orderService.delete(Mockito.anyLong())).thenReturn(0);
		
		mockMvc.perform(MockMvcRequestBuilders.delete(path + "/{id}", orderId))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$").exists())
		.andExpect(MockMvcResultMatchers.jsonPath("$").isBoolean())
		.andExpect(MockMvcResultMatchers.jsonPath("$").value(true))		
		;
	}

	@Test
	void OrderResource_delete_ReturnResponseWithBooleanFalse() throws Exception {
		
		Mockito.when(orderService.delete(Mockito.anyLong())).thenReturn(1);
		
		mockMvc.perform(MockMvcRequestBuilders.delete(path + "/{id}", orderId))
		.andExpect(MockMvcResultMatchers.status().isNotFound())
		.andExpect(MockMvcResultMatchers.jsonPath("$").exists())
		.andExpect(MockMvcResultMatchers.jsonPath("$").isBoolean())
		.andExpect(MockMvcResultMatchers.jsonPath("$").value(false))		
		;
	}

	@Test
	void OrderResource_addOrderDetail_ReturnResponseWithBooleanTrue() throws Exception {
		
		Mockito.when(orderService.addOrderDetail(Mockito.anyLong(),Mockito.anyLong())).thenReturn(true);
		
		mockMvc.perform(MockMvcRequestBuilders.put(path + "/{orderId}/orderdetails/{orderDetailId}", orderId, orderDetailId))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$").exists())
		.andExpect(MockMvcResultMatchers.jsonPath("$").isBoolean())
		.andExpect(MockMvcResultMatchers.jsonPath("$").value(true))		
		;
	}
	
	@Test
	void OrderResource_addOrderDetail_ReturnResponseWithBooleanFalse() throws Exception {
		
		Mockito.when(orderService.addOrderDetail(Mockito.anyLong(),Mockito.anyLong())).thenReturn(false);

		mockMvc.perform(MockMvcRequestBuilders.put(path + "/{orderId}/orderdetails/{orderDetailId}", orderId, orderDetailId))
		.andExpect(MockMvcResultMatchers.status().isNotFound())
		.andExpect(MockMvcResultMatchers.jsonPath("$").exists())
		.andExpect(MockMvcResultMatchers.jsonPath("$").isBoolean())
		.andExpect(MockMvcResultMatchers.jsonPath("$").value(false))		
		;
	}

	@Test
	void OrderResource_getLinesByOrderId_ReturnResponseWithList() throws Exception {
		
		orderDetailDtoSet.add(orderDetailDto);
		orderTest.getOrderDetails().add(orderDetailTest);
		Mockito.when(orderService.getById(Mockito.anyLong())).thenReturn(Optional.of(orderTest));
		Mockito.when(orderDetailMapper.toSetDto(Mockito.anyCollection())).thenReturn(orderDetailDtoSet);
		
		mockMvc.perform(MockMvcRequestBuilders.get(path + "/{orderId}" + "/orderdetails", orderId))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.notNullValue()))
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.hasSize(1)))
		.andExpect(MockMvcResultMatchers.jsonPath("$[0].orderId", org.hamcrest.Matchers.equalTo(7)))
		;
		
	}

	@Test
	void OrderResource_getLinesByOrderId_ReturnResponseWithNotFound() throws Exception {
		
		Mockito.when(orderService.getById(Mockito.anyLong())).thenReturn(Optional.empty());
		
		mockMvc.perform(MockMvcRequestBuilders.get(path + "/{orderId}" + "/orderdetails", orderId))
		.andExpect(MockMvcResultMatchers.status().isNotFound())
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.notNullValue()))
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.empty()))
		;
	}

	@Test
	void OrderResource_getLinesByOrderId_ReturnResponseWithNotFoundList() throws Exception {
		
		Mockito.when(orderService.getById(Mockito.anyLong())).thenReturn(Optional.of(orderTest));
		
		mockMvc.perform(MockMvcRequestBuilders.get(path + "/{orderId}" + "/orderdetails", orderId))
		.andExpect(MockMvcResultMatchers.status().isNotFound())
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.notNullValue()))
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.empty()))
		;
		
	}	
}

