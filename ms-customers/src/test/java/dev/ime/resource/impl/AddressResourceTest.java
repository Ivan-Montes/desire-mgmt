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

import dev.ime.dto.AddressDto;
import dev.ime.entity.Address;
import dev.ime.mapper.impl.AddressMapper;
import dev.ime.service.impl.AddressServiceImpl;

@WebMvcTest(AddressResource.class)
@AutoConfigureMockMvc(addFilters = false)
class AddressResourceTest {

	@MockBean
	private AddressServiceImpl addressService;
	
	@MockBean
	private AddressMapper addressMapper;
	
	@Autowired
	private MockMvc mockMvc;

	@Autowired
    private ObjectMapper objectMapper;

	@MockBean
	private Logger logger;
	
	private final String path = "/api/addresses";
	private List<Address>addresses;
	private List<AddressDto>addressesDtos;
	private final Long addressId = 4L;
	private final String location = "Cattedrale di Santa Maria del Fiore";
	private final String city = "Florence";
	private final String country = "Italy";
	private final String email = "florence@florence.it";
	private Address addressTest;
	private AddressDto addressTestDto;
	private final Long addressIdDto = 5L;
	private final String locationDto = "San Marco 1";
	private final String cityDto = "Venecia";
	private final String countryDto = "Veneto";
	private final String emailDto = "ven@ven.it";
	private final Long customerId = 47L;
	
	@BeforeEach
	private void createObjects() {
		
		addresses = new ArrayList<>();
		addressesDtos = new ArrayList<>();
		
		addressTest = new Address();
		addressTest.setId(addressId);
		addressTest.setLocation(location);
		addressTest.setCity(city);
		addressTest.setCountry(country);
		addressTest.setEmail(email);
		
		addressTestDto = new AddressDto(addressIdDto,locationDto,cityDto,countryDto,emailDto,customerId);
	}
	
	
	@Test
	void AddressResource_getAll_ReturnListEmpty() throws Exception{
		
		Mockito.when(addressService.getAll()).thenReturn(addresses);
		
		mockMvc.perform(MockMvcRequestBuilders.get(path))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.notNullValue()))
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.empty()))
		;
	}

	@Test
	void AddressResource_getAll_ReturnListPagedDefaultSize() throws Exception{
		
		addresses.add(addressTest);
		addressesDtos.add(addressTestDto);
		Mockito.when(addressService.getAllPaged(Mockito.anyInt(), Mockito.anyInt())).thenReturn(addresses);
		Mockito.when(addressMapper.toListDto(Mockito.anyList())).thenReturn(addressesDtos);
		
		mockMvc.perform(MockMvcRequestBuilders.get(path)
				.param("page", "1"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.notNullValue()))
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.hasSize(1)))
		.andExpect(MockMvcResultMatchers.jsonPath("$[0].addressId", org.hamcrest.Matchers.equalTo(addressIdDto.intValue())))
		.andExpect(MockMvcResultMatchers.jsonPath("$[0].location", org.hamcrest.Matchers.equalTo(locationDto)))
		.andExpect(MockMvcResultMatchers.jsonPath("$[0].city", org.hamcrest.Matchers.equalTo(cityDto)))
		.andExpect(MockMvcResultMatchers.jsonPath("$[0].country", org.hamcrest.Matchers.equalTo(countryDto)))
		.andExpect(MockMvcResultMatchers.jsonPath("$[0].email", org.hamcrest.Matchers.equalTo(emailDto)))
		;
	}

	@Test
	void AddressResource_getAll_ReturnListPaged() throws Exception{
		
		addresses.add(addressTest);
		addressesDtos.add(addressTestDto);
		Mockito.when(addressService.getAllPaged(Mockito.anyInt(), Mockito.anyInt())).thenReturn(addresses);
		Mockito.when(addressMapper.toListDto(Mockito.anyList())).thenReturn(addressesDtos);
		
		mockMvc.perform(MockMvcRequestBuilders.get(path)
				.param("page", "1")
				.param("size", "1"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.notNullValue()))
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.hasSize(1)))
		.andExpect(MockMvcResultMatchers.jsonPath("$[0].addressId", org.hamcrest.Matchers.equalTo(addressIdDto.intValue())))
		.andExpect(MockMvcResultMatchers.jsonPath("$[0].location", org.hamcrest.Matchers.equalTo(locationDto)))
		.andExpect(MockMvcResultMatchers.jsonPath("$[0].city", org.hamcrest.Matchers.equalTo(cityDto)))
		.andExpect(MockMvcResultMatchers.jsonPath("$[0].country", org.hamcrest.Matchers.equalTo(countryDto)))
		.andExpect(MockMvcResultMatchers.jsonPath("$[0].email", org.hamcrest.Matchers.equalTo(emailDto)))
		;
	}

	@Test
	void AddressResource_getById_ReturnResponseWithCustomerDto() throws Exception {
		
		Mockito.when(addressService.getById(Mockito.anyLong())).thenReturn(Optional.of(addressTest));
		Mockito.when(addressMapper.toDto(Mockito.any(Address.class))).thenReturn(addressTestDto);
		
		mockMvc.perform(MockMvcRequestBuilders.get(path + "/{id}", addressId))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.notNullValue()))
		.andExpect(MockMvcResultMatchers.jsonPath("$.addressId", org.hamcrest.Matchers.equalTo(addressIdDto.intValue())))
		.andExpect(MockMvcResultMatchers.jsonPath("$.location", org.hamcrest.Matchers.equalTo(locationDto)))
		.andExpect(MockMvcResultMatchers.jsonPath("$.city", org.hamcrest.Matchers.equalTo(cityDto)))
		.andExpect(MockMvcResultMatchers.jsonPath("$.country", org.hamcrest.Matchers.equalTo(countryDto)))
		.andExpect(MockMvcResultMatchers.jsonPath("$.email", org.hamcrest.Matchers.equalTo(emailDto)))
		;
	}

	@Test
	void AddressResource_getById_ReturnResponseWithNotFound() throws Exception {
		
		Mockito.when(addressService.getById(Mockito.anyLong())).thenReturn(Optional.empty());

		mockMvc.perform(MockMvcRequestBuilders.get(path + "/{id}", addressId))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.notNullValue()))
		.andExpect(MockMvcResultMatchers.jsonPath("$.addressId",  org.hamcrest.Matchers.equalTo(0)))
		;
	}

	@Test
	void AddressResource_create_ReturnResponseWithCustomerDto() throws Exception {

		Mockito.when(addressService.create(Mockito.any(AddressDto.class))).thenReturn(Optional.of(addressTest));
		Mockito.when(addressMapper.toDto(Mockito.any(Address.class))).thenReturn(addressTestDto);
		
		mockMvc.perform(MockMvcRequestBuilders.post(path)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(addressTestDto))
				)
		.andExpect(MockMvcResultMatchers.status().isCreated())
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.notNullValue()))
		.andExpect(MockMvcResultMatchers.jsonPath("$.addressId", org.hamcrest.Matchers.equalTo(addressIdDto.intValue())))
		.andExpect(MockMvcResultMatchers.jsonPath("$.location", org.hamcrest.Matchers.equalTo(locationDto)))
		.andExpect(MockMvcResultMatchers.jsonPath("$.city", org.hamcrest.Matchers.equalTo(cityDto)))
		.andExpect(MockMvcResultMatchers.jsonPath("$.country", org.hamcrest.Matchers.equalTo(countryDto)))
		.andExpect(MockMvcResultMatchers.jsonPath("$.email", org.hamcrest.Matchers.equalTo(emailDto)))
		;		
	}

	@Test
	void AddressResource_create_ReturnResponseWithCustomerDtoEmpty() throws Exception {

		Mockito.when(addressService.create(Mockito.any(AddressDto.class))).thenReturn(Optional.empty());
		
		mockMvc.perform(MockMvcRequestBuilders.post(path)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(addressTestDto))
				)
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.notNullValue()))
		.andExpect(MockMvcResultMatchers.jsonPath("$.addressId",  org.hamcrest.Matchers.equalTo(0)))
		;		
	}

	@Test
	void AddressResource_update_ReturnResponseWithCustomerDto() throws Exception {
		
		Mockito.when(addressService.update(Mockito.anyLong(),Mockito.any(AddressDto.class))).thenReturn(Optional.of(addressTest));
		Mockito.when(addressMapper.toDto(Mockito.any(Address.class))).thenReturn(addressTestDto);
		
		mockMvc.perform(MockMvcRequestBuilders.put(path + "/{id}", addressId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(addressTestDto))
				)
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.notNullValue()))
		.andExpect(MockMvcResultMatchers.jsonPath("$.addressId", org.hamcrest.Matchers.equalTo(addressIdDto.intValue())))
		.andExpect(MockMvcResultMatchers.jsonPath("$.location", org.hamcrest.Matchers.equalTo(locationDto)))
		.andExpect(MockMvcResultMatchers.jsonPath("$.city", org.hamcrest.Matchers.equalTo(cityDto)))
		.andExpect(MockMvcResultMatchers.jsonPath("$.country", org.hamcrest.Matchers.equalTo(countryDto)))
		.andExpect(MockMvcResultMatchers.jsonPath("$.email", org.hamcrest.Matchers.equalTo(emailDto)))
		;		
	}

	@Test
	void AddressResource_update_ReturnResponseWithCustomerDtoEmpty() throws Exception {
		
		Mockito.when(addressService.update(Mockito.anyLong(),Mockito.any(AddressDto.class))).thenReturn(Optional.empty());
		
		mockMvc.perform(MockMvcRequestBuilders.put(path + "/{id}", addressId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(addressTestDto))
				)
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.notNullValue()))
		.andExpect(MockMvcResultMatchers.jsonPath("$.addressId",  org.hamcrest.Matchers.equalTo(0)))
		;		
	}

	@Test
	void AddressResource_delete_ResturnResponseWithBooleanTrue()  throws Exception {
	
		Mockito.when(addressService.delete(Mockito.anyLong())).thenReturn(0);
		
		mockMvc.perform(MockMvcRequestBuilders.delete(path + "/{id}", addressId))	
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$").exists())
		.andExpect(MockMvcResultMatchers.jsonPath("$").isBoolean())
		.andExpect(MockMvcResultMatchers.jsonPath("$").value(true))		
		;		
	}

	@Test
	void AddressResource_delete_ResturnResponseWithBooleanFalse()  throws Exception {
		
		Mockito.when(addressService.delete(Mockito.anyLong())).thenReturn(1);
		
		mockMvc.perform(MockMvcRequestBuilders.delete(path + "/{id}", addressId))	
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$").exists())
		.andExpect(MockMvcResultMatchers.jsonPath("$").isBoolean())
		.andExpect(MockMvcResultMatchers.jsonPath("$").value(false))		
		;
	}

	@Test
	void AddressResource_setCustomerResturnResponseWithBooleanTrue()  throws Exception {
		
		Mockito.when(addressService.setCustomer(Mockito.anyLong(), Mockito.anyLong())).thenReturn(true);
		
		mockMvc.perform(MockMvcRequestBuilders.put(path + "/{addressId}/customers/{customerId}", addressId, customerId))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$").exists())
		.andExpect(MockMvcResultMatchers.jsonPath("$").isBoolean())
		.andExpect(MockMvcResultMatchers.jsonPath("$").value(true))	
		;
	}

	@Test
	void AddressResource_setCustomerResturnResponseWithBooleanFalse()  throws Exception {

		Mockito.when(addressService.setCustomer(Mockito.anyLong(), Mockito.anyLong())).thenReturn(false);
		
		mockMvc.perform(MockMvcRequestBuilders.put(path + "/{addressId}/customers/{customerId}", addressId, customerId))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$").exists())
		.andExpect(MockMvcResultMatchers.jsonPath("$").isBoolean())
		.andExpect(MockMvcResultMatchers.jsonPath("$").value(false))	
		;
	}

}
