package dev.ime.resource.impl;



import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
import dev.ime.entity.Order;
import dev.ime.entity.OrderDetail;
import dev.ime.mapper.impl.OrderDetailMapper;
import dev.ime.service.impl.OrderDetailServiceImpl;

@WebMvcTest(OrderDetailResource.class)
@AutoConfigureMockMvc(addFilters = false)
class OrderDetailResourceTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private OrderDetailServiceImpl orderDetailService;
	
	@MockBean
	private OrderDetailMapper orderDetailMapper;	

	@Autowired
    private ObjectMapper objectMapper;
	
	@MockBean
	private Logger logger;
	
	private final String path = "/api/orderdetails";
	private List<OrderDetail>orderDetails;
	private List<OrderDetailDto>orderDetailDtos;
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
		orderDetailDtos = new ArrayList<>();
		
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
	void OrderDetailResource_getAll_ResponseWithEmpyList() throws Exception{
		
		Mockito.when(orderDetailService.getAll()).thenReturn(orderDetails);
		
		mockMvc.perform(MockMvcRequestBuilders.get(path))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.notNullValue()))
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.empty()))
		;
	}

	@Test
	void OrderDetailResource_getAll_ResponseWithListDefaultSize() throws Exception{
		
		orderDetails.add(orderDetailTest);
		orderDetailDtos.add(orderDetailDto);
		Mockito.when(orderDetailService.getAllPaged(Mockito.anyInt(),Mockito.anyInt())).thenReturn(orderDetails);
		Mockito.when(orderDetailMapper.toListDto(Mockito.anyList())).thenReturn(orderDetailDtos);
		
		mockMvc.perform(MockMvcRequestBuilders.get(path)
				.param("page", "1"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.notNullValue()))
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.hasSize(1)))
		;
	}

	@Test
	void OrderDetailResource_getAll_ReturnPagedList() throws Exception{
		
		orderDetails.add(orderDetailTest);
		orderDetailDtos.add(orderDetailDto);
		Mockito.when(orderDetailService.getAllPaged(Mockito.anyInt(),Mockito.anyInt())).thenReturn(orderDetails);
		Mockito.when(orderDetailMapper.toListDto(Mockito.anyList())).thenReturn(orderDetailDtos);
		
		mockMvc.perform(MockMvcRequestBuilders.get(path)
				.param("page", "1")
				.param("size", "1"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.notNullValue()))
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.hasSize(1)))
		;
	}

	@Test
	void OrderDetailResource_getById_ReturnResponseWithOrderDetailDto() throws Exception {
		
		Mockito.when(orderDetailService.getById(Mockito.anyLong())).thenReturn(Optional.of(orderDetailTest));
		Mockito.when(orderDetailMapper.toDto(Mockito.any(OrderDetail.class))).thenReturn(orderDetailDto);
		
		mockMvc.perform(MockMvcRequestBuilders.get(path + "/{id}", orderDetailId))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.notNullValue()))
		.andExpect(MockMvcResultMatchers.jsonPath("$.orderDetailId", org.hamcrest.Matchers.equalTo(2)))
		;		
	}	

	@Test
	void OrderDetailResource_getById_ReturnResponseWithNotFound() throws Exception {
		
		Mockito.when(orderDetailService.getById(Mockito.anyLong())).thenReturn(Optional.empty());

		mockMvc.perform(MockMvcRequestBuilders.get(path + "/{id}", orderDetailId))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.notNullValue()))
		.andExpect(MockMvcResultMatchers.jsonPath("$.orderDetailId", org.hamcrest.Matchers.equalTo(0)))
		;
	}

	@Test
	void OrderDetailResource_create_ReturnResponseWithOrderDetailDto() throws Exception {
		
		Mockito.when(orderDetailService.create(Mockito.any(OrderDetailDto.class))).thenReturn(Optional.of(orderDetailTest));
		Mockito.when(orderDetailMapper.toDto(Mockito.any(OrderDetail.class))).thenReturn(orderDetailDto);

		mockMvc.perform(MockMvcRequestBuilders.post(path)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(orderDetailDto))
				)
		.andExpect(MockMvcResultMatchers.status().isCreated())
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.notNullValue()))
		.andExpect(MockMvcResultMatchers.jsonPath("$.quantity", org.hamcrest.Matchers.equalTo(13)))
		.andExpect(MockMvcResultMatchers.jsonPath("$.discount", org.hamcrest.Matchers.equalTo(23.9)))
		.andExpect(MockMvcResultMatchers.jsonPath("$.productId", org.hamcrest.Matchers.equalTo(3)))
		;
	}		

	@Test
	void OrderDetailResource_create_ReturnResponseWithNotFound() throws Exception {
		
		Mockito.when(orderDetailService.create(Mockito.any(OrderDetailDto.class))).thenReturn(Optional.empty());
		
		mockMvc.perform(MockMvcRequestBuilders.post(path)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(orderDetailDto))
				)
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.notNullValue()))
		.andExpect(MockMvcResultMatchers.jsonPath("$.orderDetailId", org.hamcrest.Matchers.equalTo(0)))
		;
	}	

	@Test
	void OrderDetailResource_update_ReturnResponseWithOrderDetailDto() throws Exception {
		
		Mockito.when(orderDetailService.update(Mockito.anyLong(),Mockito.any(OrderDetailDto.class))).thenReturn(Optional.of(orderDetailTest));
		Mockito.when(orderDetailMapper.toDto(Mockito.any(OrderDetail.class))).thenReturn(orderDetailDto);

		mockMvc.perform(MockMvcRequestBuilders.put(path + "/{id}", orderDetailId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(orderDetailDto))
				)
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.notNullValue()))
		.andExpect(MockMvcResultMatchers.jsonPath("$.quantity", org.hamcrest.Matchers.equalTo(13)))
		.andExpect(MockMvcResultMatchers.jsonPath("$.discount", org.hamcrest.Matchers.equalTo(23.9)))
		.andExpect(MockMvcResultMatchers.jsonPath("$.productId", org.hamcrest.Matchers.equalTo(3)))
		;
	}

	@Test
	void OrderDetailResource_update_ReturnResponseWithNotFound() throws Exception {
		
		Mockito.when(orderDetailService.update(Mockito.anyLong(),Mockito.any(OrderDetailDto.class))).thenReturn(Optional.empty());
		Mockito.when(orderDetailMapper.toDto(Mockito.any(OrderDetail.class))).thenReturn(orderDetailDto);

		mockMvc.perform(MockMvcRequestBuilders.put(path + "/{id}", orderDetailId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(orderDetailDto))
				)
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.notNullValue()))
		.andExpect(MockMvcResultMatchers.jsonPath("$.orderDetailId", org.hamcrest.Matchers.equalTo(0)))
		;
	}

	@Test
	void OrderDetailResource_delete_ReturnResponseWithBooleanTrue() throws Exception {
		
		Mockito.when(orderDetailService.delete(Mockito.anyLong())).thenReturn(0);
		
		mockMvc.perform(MockMvcRequestBuilders.delete(path + "/{id}", orderDetailId))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$").exists())
		.andExpect(MockMvcResultMatchers.jsonPath("$").isBoolean())
		.andExpect(MockMvcResultMatchers.jsonPath("$").value(true))
		;
	}

	@Test
	void OrderDetailResource_delete_ReturnResponseWithBooleanFalse() throws Exception {
		
		Mockito.when(orderDetailService.delete(Mockito.anyLong())).thenReturn(1);
		
		mockMvc.perform(MockMvcRequestBuilders.delete(path + "/{id}", orderDetailId))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$").exists())
		.andExpect(MockMvcResultMatchers.jsonPath("$").isBoolean())
		.andExpect(MockMvcResultMatchers.jsonPath("$").value(false))
		;
	}
	
	@Test
	void OrderDetailResource_setOrder_ReturnResponseWithBooleanTrue() throws Exception {
		
		Mockito.when(orderDetailService.setOrder(Mockito.anyLong(),Mockito.anyLong())).thenReturn(true);
		
		mockMvc.perform(MockMvcRequestBuilders.put(path + "/{orderDetailId}/orders/{orderId}", orderDetailId, orderId))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$").exists())
		.andExpect(MockMvcResultMatchers.jsonPath("$").isBoolean())
		.andExpect(MockMvcResultMatchers.jsonPath("$").value(true))
		;
	}
		
	@Test
	void OrderDetailResource_setOrder_ReturnResponseWithBooleanFalse() throws Exception {
		
		Mockito.when(orderDetailService.setOrder(Mockito.anyLong(),Mockito.anyLong())).thenReturn(false);
		
		mockMvc.perform(MockMvcRequestBuilders.put(path + "/{orderDetailId}/orders/{orderId}", orderDetailId, orderId))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$").exists())
		.andExpect(MockMvcResultMatchers.jsonPath("$").isBoolean())
		.andExpect(MockMvcResultMatchers.jsonPath("$").value(false))
		;
	}
	
	@Test
	void OrderDetailResource_getAnyByProductId_ReturnResponseWithBooleanTrue() throws Exception {
		
		Mockito.when(orderDetailService.getAnyByProductId(Mockito.anyLong())).thenReturn(true);
		
		mockMvc.perform(MockMvcRequestBuilders.put(path + "/products/{productId}", productId))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$").exists())
		.andExpect(MockMvcResultMatchers.jsonPath("$").isBoolean())
		.andExpect(MockMvcResultMatchers.jsonPath("$").value(true))
		;
	}

	@Test
	void OrderDetailResource_getAnyByProductId_ReturnResponseWithBooleanFalse() throws Exception {
		
		Mockito.when(orderDetailService.getAnyByProductId(Mockito.anyLong())).thenReturn(false);
		
		mockMvc.perform(MockMvcRequestBuilders.put(path + "/products/{productId}", productId))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$").exists())
		.andExpect(MockMvcResultMatchers.jsonPath("$").isBoolean())
		.andExpect(MockMvcResultMatchers.jsonPath("$").value(false))
		;
	}
}
