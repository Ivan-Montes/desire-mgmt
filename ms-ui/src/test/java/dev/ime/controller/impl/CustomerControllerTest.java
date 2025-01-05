package dev.ime.controller.impl;


import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import dev.ime.dto.CustomerDto;
import dev.ime.exceptionhandler.GlobalExceptionHandler;
import dev.ime.service.impl.CustomerService;


@WebMvcTest(CustomerController.class)
@AutoConfigureMockMvc(addFilters = false)
class CustomerControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockitoBean
	private CustomerService customerService;

	@MockitoBean
	private GlobalExceptionHandler globalExceptionHandler;
	
	@MockitoBean
	private Logger logger;
	
	private static final String PATH = "/customers";
	private static final String REDIRECT_CUSTOMERS = "redirect:" + PATH;

	private final Long customerId = 66L;
	private final String companyName = "Amegakure";
	private final String contactName = "Kohan";
	private CustomerDto customerDtoTest;	
	
	@BeforeEach
	public void createObjects() {
		
		customerDtoTest = new CustomerDto(customerId, companyName, contactName);
		
	}

	@Test
	void CustomerController_getAll_ReturnStringView() throws Exception {		

		mockMvc.perform(MockMvcRequestBuilders.get(PATH))
		.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
		.andExpect(MockMvcResultMatchers.view().name("sections/customers/index"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("customerList"));
	}
	
	@Test
	void CustomerController_getAll_ReturnStringViewWithPageAttr() throws Exception {		

		mockMvc.perform(MockMvcRequestBuilders.get(PATH)
		.param("page", "9"))
		.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
		.andExpect(MockMvcResultMatchers.view().name("sections/customers/index"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("customerList"));
	}

	@Test
	void CustomerController_getAll_ReturnStringViewWithPageAndSizeAttr() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.get(PATH)
		.param("page", "18")
		.param("size", "7"))
		.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
		.andExpect(MockMvcResultMatchers.view().name("sections/customers/index"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("customerList"));
	}

	@Test
	void CategoryController_getById_ReturnStringView() throws Exception {
		
		Mockito.when(customerService.getById(Mockito.anyLong())).thenReturn(customerDtoTest);
		
		mockMvc.perform(MockMvcRequestBuilders.get(PATH + "/{id}", customerId))
		.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
		.andExpect(MockMvcResultMatchers.view().name("sections/customers/edit"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("customer"));
	}

	@Test
	void CategoryController_add_ReturnStringView() throws Exception {
		
		mockMvc.perform(MockMvcRequestBuilders.get(PATH + "/add"))
		.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
		.andExpect(MockMvcResultMatchers.view().name("sections/customers/add"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("newCustomer"));		
	}	

	@Test
	void CustomerController_create_ReturnStringView() throws Exception {
		
		mockMvc.perform(MockMvcRequestBuilders.post(PATH + "/create")
				.param("companyName", companyName)
				.param("contactName", contactName))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
		.andExpect(MockMvcResultMatchers.view().name(REDIRECT_CUSTOMERS))
		.andExpect(MockMvcResultMatchers.redirectedUrl(PATH));
	}

	@Test
	void CustomerController_update_ReturnStringView() throws Exception {
		
		mockMvc.perform(MockMvcRequestBuilders.post(PATH + "/update/{id}", customerId)
				.param("categoryId", String.valueOf(customerId))
				.param("companyName", companyName)
				.param("contactName", contactName))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
		.andExpect(MockMvcResultMatchers.view().name(REDIRECT_CUSTOMERS))
		.andExpect(MockMvcResultMatchers.redirectedUrl(PATH));
	}

	@Test
	void CustomerController_confirmDelete_ReturnStringView() throws Exception {

		Mockito.when(customerService.getById(Mockito.anyLong())).thenReturn(customerDtoTest);
		
		mockMvc.perform(MockMvcRequestBuilders.get(PATH + "/confirm-delete/{id}", customerId))
		.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
		.andExpect(MockMvcResultMatchers.view().name("sections/customers/delete"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("customer"));
	}	

	@Test
	void CustomerController_delete_ReturnStringView() throws Exception {
		
		mockMvc.perform(MockMvcRequestBuilders.get(PATH + "/delete/{id}", customerId))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
		.andExpect(MockMvcResultMatchers.view().name(REDIRECT_CUSTOMERS))
		.andExpect(MockMvcResultMatchers.redirectedUrl(PATH));		
	}
	
}
