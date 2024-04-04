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

import dev.ime.dto.AddressDto;
import dev.ime.dto.CustomerDto;
import dev.ime.dtomvc.AddressMvcDto;
import dev.ime.exceptionhandler.GlobalExceptionHandler;
import dev.ime.service.impl.AddressService;
import dev.ime.service.impl.CustomerService;

@WebMvcTest(AddressController.class)
@AutoConfigureMockMvc(addFilters = false)
class AddressControllerTest {

	@Autowired
	private MockMvc mockMvc;	

	@MockBean
	private AddressService addressService;
	
	@MockBean
	private CustomerService customerService;	

	@MockBean
	private GlobalExceptionHandler globalExceptionHandler;
	
	@MockBean
	private Logger logger;

	private final String PATH = "/addresses";
	private final String REDIRECT_ADDRESSES = "redirect:" + PATH;

	private final Long addressId = 5L;
	private final String location = "San Marco 1";
	private final String city = "Venecia";
	private final String country = "Veneto";
	private final String email = "ven@ven.it";
	private final Long customerId = 47L;
	private AddressDto addressTestDto;

	@BeforeEach
	private void createObjects() {
		
		addressTestDto = new AddressDto(addressId,location,city,country,email,customerId);
	}
	
	
	@Test
	void AddressController_getAll_ReturnStringView() throws Exception {
		
		mockMvc.perform(MockMvcRequestBuilders.get(PATH))
		.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
		.andExpect(MockMvcResultMatchers.view().name("sections/addresses/index"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("addressList"));
		
	}

	@Test
	void AddressController_getAll_ReturnStringViewDefaultSize() throws Exception {
		
		mockMvc.perform(MockMvcRequestBuilders.get(PATH)
				.param("page", "1"))
		.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
		.andExpect(MockMvcResultMatchers.view().name("sections/addresses/index"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("addressList"));
		
	}

	@Test
	void AddressController_getAll_ReturnStringViewSizeAndPage() throws Exception {
		
		mockMvc.perform(MockMvcRequestBuilders.get(PATH)
				.param("page", "1")
				.param("size", "1"))
		.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
		.andExpect(MockMvcResultMatchers.view().name("sections/addresses/index"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("addressList"));
		
	}

	@Test
	void AddressController_getById_ReturnStringView() throws Exception {		
		
		Mockito.when(addressService.getById(Mockito.anyLong())).thenReturn(addressTestDto);
		
		mockMvc.perform(MockMvcRequestBuilders.get(PATH + "/{id}", 3L))
		.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
		.andExpect(MockMvcResultMatchers.view().name("sections/addresses/edit"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("address"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("customerList"));
		
	}

	@Test
	void AddressController_add_ReturnStringView() throws Exception {
		
		mockMvc.perform(MockMvcRequestBuilders.get(PATH + "/add"))
		.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
		.andExpect(MockMvcResultMatchers.view().name("sections/addresses/add"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("newAddress"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("customerList"));
		
	}

	@Test
	void AddressController_create_ReturnStringView() throws Exception {
		
		Mockito.when(addressService.create(Mockito.any(AddressMvcDto.class))).thenReturn(addressTestDto);
		
		mockMvc.perform(MockMvcRequestBuilders.post(PATH + "/create")
				.param("location", location)
				.param("city", city)
				.param("country", country)
				.param("email", email)
				.param("customerId", String.valueOf(customerId))
				)
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
		.andExpect(MockMvcResultMatchers.view().name(REDIRECT_ADDRESSES))
		.andExpect(MockMvcResultMatchers.redirectedUrl(PATH));		
	}

	@Test
	void AddressController_update_ReturnStringView() throws Exception {

		Mockito.when(addressService.update(Mockito.anyLong(),Mockito.any(AddressMvcDto.class))).thenReturn(addressTestDto);
		
		mockMvc.perform(MockMvcRequestBuilders.post(PATH + "/update/{id}", addressId)
				.param("location", location)
				.param("city", city)
				.param("country", country)
				.param("email", email)
				.param("customerId", String.valueOf(customerId))
				)
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
		.andExpect(MockMvcResultMatchers.view().name(REDIRECT_ADDRESSES))
		.andExpect(MockMvcResultMatchers.redirectedUrl(PATH));				
	}

	@Test
	void AddressController_confirmDelete_ReturnStringView() throws Exception {

		Mockito.when(addressService.getById(Mockito.anyLong())).thenReturn(addressTestDto);
		Mockito.when(customerService.getById(Mockito.anyLong())).thenReturn(new CustomerDto());
		
		mockMvc.perform(MockMvcRequestBuilders.get(PATH + "/confirm-delete/{id}", addressId))
		.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
		.andExpect(MockMvcResultMatchers.view().name("sections/addresses/delete"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("address"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("customer"));		
	}

	@Test
	void AddressController_delete_ReturnStringView() throws Exception {
		
		mockMvc.perform(MockMvcRequestBuilders.get(PATH + "/delete/{id}", addressId))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
		.andExpect(MockMvcResultMatchers.view().name(REDIRECT_ADDRESSES))
		.andExpect(MockMvcResultMatchers.redirectedUrl(PATH));		
	}	
	
}
