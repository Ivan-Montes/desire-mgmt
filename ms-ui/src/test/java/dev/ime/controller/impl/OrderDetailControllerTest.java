package dev.ime.controller.impl;


import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import dev.ime.dto.OrderDetailDto;
import dev.ime.dto.OrderDto;
import dev.ime.dto.ProductDto;
import dev.ime.exceptionhandler.GlobalExceptionHandler;
import dev.ime.service.impl.OrderDetailService;
import dev.ime.service.impl.OrderService;
import dev.ime.service.impl.ProductService;


@WebMvcTest(OrderDetailController.class)
@AutoConfigureMockMvc(addFilters = false)
class OrderDetailControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private OrderDetailService orderDetailService;
	
	@MockBean
	private OrderService orderService;
	
	@MockBean
	private ProductService productService;
	
	@MockBean
	private GlobalExceptionHandler globalExceptionHandler;
	
	@MockBean
	private Logger logger;

	private final String PATH = "/orderdetails";
	private final String REDIRECT_ORDERDETAILS = "redirect:" + PATH;

	private final Long orderDetailId = 2L;
	private final Long orderId = 7L;
	private final Long productId = 3L;
	private final Integer quantity = 13;
	private final Double discount = 23.9D;
	private OrderDetailDto orderDetailDtoTest;
	

	@BeforeEach
	private void createObjects() {	
		
		orderDetailDtoTest = new OrderDetailDto(orderDetailId,quantity,discount,productId,orderId);
		
	}
	

	@Test
	void OrderDetailController_getAll_ReturnStringView() throws Exception {		

		mockMvc.perform(MockMvcRequestBuilders.get(PATH))
		.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
		.andExpect(MockMvcResultMatchers.view().name("sections/orderdetails/index"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("orderDetailList"));
	}
	
	@Test
	void OrderDetailController_getAll_ReturnStringViewWithPageAttr() throws Exception {		

		mockMvc.perform(MockMvcRequestBuilders.get(PATH)
		.param("page", "9"))
		.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
		.andExpect(MockMvcResultMatchers.view().name("sections/orderdetails/index"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("orderDetailList"));
	}

	@Test
	void OrderDetailController_getAll_ReturnStringViewWithPageAndSizeAttr() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.get(PATH)
		.param("page", "18")
		.param("size", "7"))
		.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
		.andExpect(MockMvcResultMatchers.view().name("sections/orderdetails/index"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("orderDetailList"));
	}


	@Test
	void CategoryController_getById_ReturnStringView() throws Exception {
		
		Mockito.when(orderDetailService.getById(Mockito.anyLong())).thenReturn(orderDetailDtoTest);
		
		mockMvc.perform(MockMvcRequestBuilders.get(PATH + "/{id}", orderDetailId))
		.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
		.andExpect(MockMvcResultMatchers.view().name("sections/orderdetails/edit"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("orderDetail"));
	}

	@Test
	void CategoryController_add_ReturnStringView() throws Exception {
		
		mockMvc.perform(MockMvcRequestBuilders.get(PATH + "/add"))
		.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
		.andExpect(MockMvcResultMatchers.view().name("sections/orderdetails/add"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("newOrderDetail"));		
	}
	

	@Test
	void OrderDetailController_create_ReturnStringView() throws Exception {
		
		mockMvc.perform(MockMvcRequestBuilders.post(PATH + "/create")
				.param("quantity", String.valueOf(quantity))
				.param("discount", String.valueOf(discount))
				.param("productId", String.valueOf(quantity))
				.param("orderId", String.valueOf(orderId)))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
		.andExpect(MockMvcResultMatchers.view().name(REDIRECT_ORDERDETAILS))
		.andExpect(MockMvcResultMatchers.redirectedUrl(PATH));
	}

	@Test
	void OrderDetailController_update_ReturnStringView() throws Exception {
		
		mockMvc.perform(MockMvcRequestBuilders.post(PATH + "/update/{id}", orderDetailId)
				.param("orderDetailId", String.valueOf(orderDetailId))
				.param("quantity", String.valueOf(quantity))
				.param("discount", String.valueOf(discount))
				.param("productId", String.valueOf(quantity))
				.param("orderId", String.valueOf(orderId)))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
		.andExpect(MockMvcResultMatchers.view().name(REDIRECT_ORDERDETAILS))
		.andExpect(MockMvcResultMatchers.redirectedUrl(PATH));
	}

	@Test
	void OrderDetailController_confirmDelete_ReturnStringView() throws Exception {

		Mockito.when(orderDetailService.getById(Mockito.anyLong())).thenReturn(orderDetailDtoTest);
		Mockito.when(productService.getById(Mockito.anyLong())).thenReturn(new ProductDto());
		Mockito.when(orderService.getById(Mockito.anyLong())).thenReturn(new OrderDto());
		
		mockMvc.perform(MockMvcRequestBuilders.get(PATH + "/confirm-delete/{id}", orderDetailId))
		.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
		.andExpect(MockMvcResultMatchers.view().name("sections/orderdetails/delete"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("orderDetail"));
	}
	

	@Test
	void OrderDetailController_delete_ReturnStringView() throws Exception {
		
		mockMvc.perform(MockMvcRequestBuilders.get(PATH + "/delete/{id}", orderDetailId))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
		.andExpect(MockMvcResultMatchers.view().name(REDIRECT_ORDERDETAILS))
		.andExpect(MockMvcResultMatchers.redirectedUrl(PATH));		
	}
	

}
