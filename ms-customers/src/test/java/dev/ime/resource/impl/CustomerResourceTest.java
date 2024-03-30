package dev.ime.resource.impl;


import java.util.ArrayList;
import java.util.Collections;
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

import dev.ime.dto.CustomerDto;
import dev.ime.entity.Address;
import dev.ime.entity.Customer;
import dev.ime.mapper.impl.AddressMapper;
import dev.ime.mapper.impl.CustomerMapper;
import dev.ime.service.impl.CustomerServiceImpl;


@WebMvcTest(CustomerResource.class)
@AutoConfigureMockMvc(addFilters = false)
class CustomerResourceTest {

	@MockBean
	private CustomerServiceImpl customerService;
	
	@MockBean
	private CustomerMapper customerMapper;
	
	@MockBean
	private AddressMapper addressMapper;
	
	@Autowired
	private MockMvc mockMvc;

	@Autowired
    private ObjectMapper objectMapper;

	@MockBean
	private Logger logger;
	
	private final String path = "/api/customers";
	private List<Customer>customers;
	private List<CustomerDto>customersDtos;
	private Customer customerTest;
	private CustomerDto customerDtoTest;
	private final Long customerId = 4L;
	private final String companyName = "Konohagakure";
	private final String contactName = "Kakashi";
	private final Long customerDtoId = 5L;
	private final String companyNameDto = "Amegakure";
	private final String contactNameDto = "Pain";
	
	
	@BeforeEach
	private void createObjects() {
		
		customers = new ArrayList<>();
		customersDtos = new ArrayList<>();
		
		customerTest = new Customer();
		customerTest.setId(customerId);
		customerTest.setCompanyName(companyName);
		customerTest.setContactName(contactName);
	
		customerDtoTest = new CustomerDto(customerDtoId, companyNameDto, contactNameDto);
	}
		
	@Test
	void CustomerResource_getAll_ReturnListEmpty() throws Exception{
		
		Mockito.when(customerService.getAll()).thenReturn(customers);
		
		mockMvc.perform(MockMvcRequestBuilders.get(path))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.notNullValue()))
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.empty()))
		;
	}

	@Test
	void CustomerResource_getAll_ReturnListPagedDefaultSize() throws Exception{
		
		customers.add(customerTest);
		customersDtos.add(customerDtoTest);
		Mockito.when(customerService.getAllPaged(Mockito.anyInt(), Mockito.anyInt())).thenReturn(customers);
		Mockito.when(customerMapper.toListDto(Mockito.anyList())).thenReturn(customersDtos);
		
		mockMvc.perform(MockMvcRequestBuilders.get(path)
				.param("page", "1"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.notNullValue()))
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.hasSize(1)))
		.andExpect(MockMvcResultMatchers.jsonPath("$[0].customerId", org.hamcrest.Matchers.equalTo(customerDtoId.intValue())))
		.andExpect(MockMvcResultMatchers.jsonPath("$[0].companyName", org.hamcrest.Matchers.equalTo(companyNameDto)))
		.andExpect(MockMvcResultMatchers.jsonPath("$[0].contactName", org.hamcrest.Matchers.equalTo(contactNameDto)))
		;
	}

	@Test
	void CustomerResource_getAll_ReturnListPaged() throws Exception{
		
		customers.add(customerTest);
		customersDtos.add(customerDtoTest);
		Mockito.when(customerService.getAllPaged(Mockito.anyInt(), Mockito.anyInt())).thenReturn(customers);
		Mockito.when(customerMapper.toListDto(Mockito.anyList())).thenReturn(customersDtos);
		
		mockMvc.perform(MockMvcRequestBuilders.get(path)
				.param("page", "1")
				.param("size", "1"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.notNullValue()))
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.hasSize(1)))
		.andExpect(MockMvcResultMatchers.jsonPath("$[0].customerId", org.hamcrest.Matchers.equalTo(customerDtoId.intValue())))
		.andExpect(MockMvcResultMatchers.jsonPath("$[0].companyName", org.hamcrest.Matchers.equalTo(companyNameDto)))
		.andExpect(MockMvcResultMatchers.jsonPath("$[0].contactName", org.hamcrest.Matchers.equalTo(contactNameDto)))
		;
	}

	@Test
	void CustomerResource_getById_ReturnResponseWithCustomerDto() throws Exception {
		
		Mockito.when(customerService.getById(Mockito.anyLong())).thenReturn(Optional.of(customerTest));
		Mockito.when(customerMapper.toDto(Mockito.any(Customer.class))).thenReturn(customerDtoTest);
		
		mockMvc.perform(MockMvcRequestBuilders.get(path + "/{id}", customerId))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.notNullValue()))
		.andExpect(MockMvcResultMatchers.jsonPath("$.customerId",  org.hamcrest.Matchers.equalTo(customerDtoId.intValue())))
		.andExpect(MockMvcResultMatchers.jsonPath("$.companyName", org.hamcrest.Matchers.equalTo(companyNameDto)))
		.andExpect(MockMvcResultMatchers.jsonPath("$.contactName", org.hamcrest.Matchers.equalTo(contactNameDto)))
		;
	}

	@Test
	void CustomerResource_getById_ReturnResponseWithNotFound() throws Exception {
		
		Mockito.when(customerService.getById(Mockito.anyLong())).thenReturn(Optional.empty());

		mockMvc.perform(MockMvcRequestBuilders.get(path + "/{id}", customerId))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.notNullValue()))
		.andExpect(MockMvcResultMatchers.jsonPath("$.customerId",  org.hamcrest.Matchers.equalTo(0)))
		;
	}

	@Test
	void CustomerResource_create_ReturnResponseWithCustomerDto() throws Exception {

		Mockito.when(customerService.create(Mockito.any(CustomerDto.class))).thenReturn(Optional.of(customerTest));
		Mockito.when(customerMapper.toDto(Mockito.any(Customer.class))).thenReturn(customerDtoTest);
		
		mockMvc.perform(MockMvcRequestBuilders.post(path)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(customerDtoTest))
				)
		.andExpect(MockMvcResultMatchers.status().isCreated())
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.notNullValue()))
		.andExpect(MockMvcResultMatchers.jsonPath("$.customerId",  org.hamcrest.Matchers.equalTo(customerDtoId.intValue())))
		.andExpect(MockMvcResultMatchers.jsonPath("$.companyName", org.hamcrest.Matchers.equalTo(companyNameDto)))
		.andExpect(MockMvcResultMatchers.jsonPath("$.contactName", org.hamcrest.Matchers.equalTo(contactNameDto)))
		;		
	}

	@Test
	void CustomerResource_create_ReturnResponseWithCustomerDtoEmpty() throws Exception {

		Mockito.when(customerService.create(Mockito.any(CustomerDto.class))).thenReturn(Optional.empty());
		
		mockMvc.perform(MockMvcRequestBuilders.post(path)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(customerDtoTest))
				)
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.notNullValue()))
		.andExpect(MockMvcResultMatchers.jsonPath("$.customerId",  org.hamcrest.Matchers.equalTo(0)))
		;		
	}

	@Test
	void CustomerResource_update_ReturnResponseWithCustomerDto() throws Exception {
		
		Mockito.when(customerService.update(Mockito.anyLong(),Mockito.any(CustomerDto.class))).thenReturn(Optional.of(customerTest));
		Mockito.when(customerMapper.toDto(Mockito.any(Customer.class))).thenReturn(customerDtoTest);
		
		mockMvc.perform(MockMvcRequestBuilders.put(path + "/{id}", customerId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(customerDtoTest))
				)
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.notNullValue()))
		.andExpect(MockMvcResultMatchers.jsonPath("$.customerId",  org.hamcrest.Matchers.equalTo(customerDtoId.intValue())))
		.andExpect(MockMvcResultMatchers.jsonPath("$.companyName", org.hamcrest.Matchers.equalTo(companyNameDto)))
		.andExpect(MockMvcResultMatchers.jsonPath("$.contactName", org.hamcrest.Matchers.equalTo(contactNameDto)))
		;		
	}

	@Test
	void CustomerResource_update_ReturnResponseWithCustomerDtoEmpty() throws Exception {
		
		Mockito.when(customerService.update(Mockito.anyLong(),Mockito.any(CustomerDto.class))).thenReturn(Optional.empty());
		
		mockMvc.perform(MockMvcRequestBuilders.put(path + "/{id}", customerId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(customerDtoTest))
				)
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.notNullValue()))
		.andExpect(MockMvcResultMatchers.jsonPath("$.customerId",  org.hamcrest.Matchers.equalTo(0)))
		;		
	}

	@Test
	void CustomerResource_delete_ResturnResponseWithBooleanTrue()  throws Exception {
	
		Mockito.when(customerService.delete(Mockito.anyLong())).thenReturn(0);
		
		mockMvc.perform(MockMvcRequestBuilders.delete(path + "/{id}", customerId))	
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$").exists())
		.andExpect(MockMvcResultMatchers.jsonPath("$").isBoolean())
		.andExpect(MockMvcResultMatchers.jsonPath("$").value(true))		
		;		
	}

	@Test
	void CustomerResource_delete_ResturnResponseWithBooleanFalse()  throws Exception {
		
		Mockito.when(customerService.delete(Mockito.anyLong())).thenReturn(1);
		
		mockMvc.perform(MockMvcRequestBuilders.delete(path + "/{id}", customerId))	
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$").exists())
		.andExpect(MockMvcResultMatchers.jsonPath("$").isBoolean())
		.andExpect(MockMvcResultMatchers.jsonPath("$").value(false))		
		;
	}

	@Test
	void CustomerResource_addAddress_ResturnResponseWithBooleanTrue()  throws Exception {
		
		Mockito.when(customerService.addAddress(Mockito.anyLong(), Mockito.anyLong())).thenReturn(true);
		
		mockMvc.perform(MockMvcRequestBuilders.put(path +"/{customerId}/addresses/{addressId}", customerId, customerId))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$").exists())
		.andExpect(MockMvcResultMatchers.jsonPath("$").isBoolean())
		.andExpect(MockMvcResultMatchers.jsonPath("$").value(true))
		;
	}

	@Test
	void CustomerResource_addAddress_ResturnResponseWithBooleanFalse()  throws Exception {
		
		Mockito.when(customerService.addAddress(Mockito.anyLong(), Mockito.anyLong())).thenReturn(false);
		
		mockMvc.perform(MockMvcRequestBuilders.put(path +"/{customerId}/addresses/{addressId}", customerId, customerId))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$").exists())
		.andExpect(MockMvcResultMatchers.jsonPath("$").isBoolean())
		.andExpect(MockMvcResultMatchers.jsonPath("$").value(false))
		;
	}

	@Test
	void CustomerResource_getAddressesInCustomer_ResturnResponseWithList() throws Exception {
		
		customerTest.getAddresses().add(new Address());
		Mockito.when(customerService.getById(Mockito.anyLong())).thenReturn(Optional.of(customerTest));
		Mockito.when(addressMapper.toListDto(Mockito.anyList())).thenReturn(Collections.emptyList());
		
		mockMvc.perform(MockMvcRequestBuilders.get(path + "/{customerId}/addresses", customerId))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.notNullValue()))
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.empty()))
		;		
	}

	@Test
	void CustomerResource_getAddressesInCustomer_ResturnResponseWithNoContent() throws Exception {
		
		Mockito.when(customerService.getById(Mockito.anyLong())).thenReturn(Optional.of(customerTest));
		Mockito.when(addressMapper.toListDto(Mockito.anyList())).thenReturn(Collections.emptyList());
		
		mockMvc.perform(MockMvcRequestBuilders.get(path + "/{customerId}/addresses", customerId))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.notNullValue()))
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.empty()))
		;		
	}
}
