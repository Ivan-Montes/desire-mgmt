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

import dev.ime.dto.CustomerDto;
import dev.ime.dto.OrderDto;
import dev.ime.exceptionhandler.GlobalExceptionHandler;
import dev.ime.service.impl.CustomerService;
import dev.ime.service.impl.OrderService;


@WebMvcTest(OrderController.class)
@AutoConfigureMockMvc(addFilters = false)
class OrderControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private OrderService orderService;
	
	@MockBean
	private CustomerService customerService;

	@MockBean
	private GlobalExceptionHandler globalExceptionHandler;
	
	@MockBean
	private Logger logger;
	
	private final String PATH = "/orders";
	private final String REDIRECT_ORDERS = "redirect:" + PATH;

	private final Long orderId = 7L;
	private final Long customerId = 3L;
	private final String orderDate = "1980-07-20";
	private OrderDto orderDtoTest;
	
	
	@BeforeEach
	public void createObjects() {

		orderDtoTest = new OrderDto(orderId,customerId,orderDate);
		
	}
	
	@Test
	void OrderController_getAll_ReturnStringView() throws Exception {		

		mockMvc.perform(MockMvcRequestBuilders.get(PATH))
		.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
		.andExpect(MockMvcResultMatchers.view().name("sections/orders/index"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("orderList"));
	}
	
	@Test
	void OrderController_getAll_ReturnStringViewWithPageAttr() throws Exception {		

		mockMvc.perform(MockMvcRequestBuilders.get(PATH)
		.param("page", "9"))
		.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
		.andExpect(MockMvcResultMatchers.view().name("sections/orders/index"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("orderList"));
	}

	@Test
	void OrderController_getAll_ReturnStringViewWithPageAndSizeAttr() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.get(PATH)
		.param("page", "18")
		.param("size", "7"))
		.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
		.andExpect(MockMvcResultMatchers.view().name("sections/orders/index"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("orderList"));
	}


	@Test
	void OrderController_getById_ReturnStringView() throws Exception {
		
		Mockito.when(orderService.getById(Mockito.anyLong())).thenReturn(orderDtoTest);
		
		mockMvc.perform(MockMvcRequestBuilders.get(PATH + "/{id}", orderId))
		.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
		.andExpect(MockMvcResultMatchers.view().name("sections/orders/edit"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("order"));
	}

	@Test
	void OrderController_add_ReturnStringView() throws Exception {
		
		mockMvc.perform(MockMvcRequestBuilders.get(PATH + "/add"))
		.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
		.andExpect(MockMvcResultMatchers.view().name("sections/orders/add"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("newOrder"));		
	}
	

	@Test
	void OrderController_create_ReturnStringView() throws Exception {
		
		mockMvc.perform(MockMvcRequestBuilders.post(PATH + "/create")
				.param("customerId", String.valueOf(customerId))
				.param("orderDate", orderDate))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
		.andExpect(MockMvcResultMatchers.view().name(REDIRECT_ORDERS))
		.andExpect(MockMvcResultMatchers.redirectedUrl(PATH));
	}

	@Test
	void OrderController_update_ReturnStringView() throws Exception {
		
		mockMvc.perform(MockMvcRequestBuilders.post(PATH + "/update/{id}", orderId)
				.param("categoryId", String.valueOf(orderId))
				.param("customerId", String.valueOf(customerId))
				.param("orderDate", orderDate))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
		.andExpect(MockMvcResultMatchers.view().name(REDIRECT_ORDERS))
		.andExpect(MockMvcResultMatchers.redirectedUrl(PATH));
	}

	@Test
	void OrderController_confirmDelete_ReturnStringView() throws Exception {

		Mockito.when(orderService.getById(Mockito.anyLong())).thenReturn(orderDtoTest);
		Mockito.when(customerService.getById(Mockito.anyLong())).thenReturn(new CustomerDto());
		
		mockMvc.perform(MockMvcRequestBuilders.get(PATH + "/confirm-delete/{id}", orderId))
		.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
		.andExpect(MockMvcResultMatchers.view().name("sections/orders/delete"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("order"));
	}
	

	@Test
	void OrderController_delete_ReturnStringView() throws Exception {
		
		mockMvc.perform(MockMvcRequestBuilders.get(PATH + "/delete/{id}", orderId))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
		.andExpect(MockMvcResultMatchers.view().name(REDIRECT_ORDERS))
		.andExpect(MockMvcResultMatchers.redirectedUrl(PATH));		
	}
	
	

}
