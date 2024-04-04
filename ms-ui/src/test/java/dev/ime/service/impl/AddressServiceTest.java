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

import dev.ime.client.impl.MsCustomersClientImpl;
import dev.ime.dto.AddressDto;
import dev.ime.dtomvc.AddressMvcDto;
import dev.ime.exception.EntityAssociatedException;
import dev.ime.exception.ResourceNotFoundException;
import dev.ime.mapper.impl.AddressMvcMapper;



@ExtendWith(MockitoExtension.class)
class AddressServiceTest {
	
	@Mock
	private AddressMvcMapper addressMvcMapper;
	
	@Mock
	MsCustomersClientImpl msCustomersClient;
	
	@InjectMocks
	private AddressService addressService;

	@Mock
	private ResponseEntity<List<AddressDto>> responseListAddressDto;
	private ResponseEntity<AddressDto> responseEntityWithAddressDto;
	private ResponseEntity<AddressDto> responseEntityWithAddressDtoException;
	private ResponseEntity<Boolean> responseEntityBooleanTrue;
	
	private List<AddressDto>addressDtoList;
	private final Long addressId = 47L;
	private final String location = "San Marco 1";
	private final String city = "Venecia";
	private final String country = "Veneto";
	private final String email = "ven@ven.it";
	private final Long customerId = 15L;
	private AddressDto addressDto;
	private AddressMvcDto addressMvcDtoVoid;
	
	@BeforeEach
	private void createObjects() {

		addressDtoList = new ArrayList<>();

		addressDto = new AddressDto(addressId, location, city, country, email, customerId);
		addressMvcDtoVoid = new AddressMvcDto();
		responseEntityWithAddressDto = new ResponseEntity<>(addressDto, HttpStatus.OK);
		responseEntityWithAddressDtoException = new ResponseEntity<>(new AddressDto(), HttpStatus.OK);
		responseEntityBooleanTrue = new ResponseEntity<>(true, HttpStatus.OK);
	}
	
	
	@Test
	void AddressServiceTest_getAll_ReturnListAddress() {
		
		addressDtoList.add(addressDto);
		Mockito.when(msCustomersClient.getAllAddress()).thenReturn(responseListAddressDto);
		Mockito.when(responseListAddressDto.getBody()).thenReturn(addressDtoList);
		
		List<AddressDto> list = addressService.getAll();
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(list).isNotNull(),
				()-> Assertions.assertThat(list).isNotEmpty(),
				()-> Assertions.assertThat(list).hasSize(1),
				()-> Assertions.assertThat(list.get(0).addressId()).isEqualTo(addressId),
				()-> Assertions.assertThat(list.get(0).location()).isEqualTo(location),
				()-> Assertions.assertThat(list.get(0).city()).isEqualTo(city),
				()-> Assertions.assertThat(list.get(0).country()).isEqualTo(country),
				()-> Assertions.assertThat(list.get(0).email()).isEqualTo(email)
				);
	}

	@Test
	void AddressServiceTest_getAllPaged_ReturnListAddress() {
		
		addressDtoList.add(addressDto);
		Mockito.when(msCustomersClient.getAllAddressPaged(Mockito.anyInt(),Mockito.anyInt())).thenReturn(responseListAddressDto);
		Mockito.when(responseListAddressDto.getBody()).thenReturn(addressDtoList);
		
		List<AddressDto> list = addressService.getAllPaged(17,83);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(list).isNotNull(),
				()-> Assertions.assertThat(list).isNotEmpty(),
				()-> Assertions.assertThat(list).hasSize(1),
				()-> Assertions.assertThat(list.get(0).addressId()).isEqualTo(addressId),
				()-> Assertions.assertThat(list.get(0).location()).isEqualTo(location),
				()-> Assertions.assertThat(list.get(0).city()).isEqualTo(city),
				()-> Assertions.assertThat(list.get(0).country()).isEqualTo(country),
				()-> Assertions.assertThat(list.get(0).email()).isEqualTo(email)
				);
	}

	@Test
	void AddressServiceTest_getById_ReturnAddressDto() {
		
		Mockito.when(msCustomersClient.getAddressById(Mockito.anyLong())).thenReturn(responseEntityWithAddressDto);
		
		AddressDto addressDto = addressService.getById(addressId);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(addressDto).isNotNull(),
				()-> Assertions.assertThat(addressDto.addressId()).isEqualTo(addressId),
				()-> Assertions.assertThat(addressDto.location()).isEqualTo(location),
				()-> Assertions.assertThat(addressDto.city()).isEqualTo(city),
				()-> Assertions.assertThat(addressDto.country()).isEqualTo(country),
				()-> Assertions.assertThat(addressDto.email()).isEqualTo(email)
				);
	}

	@Test
	void AddressServiceTest_getById_ReturnResourceNotFoundException() {
		
		Mockito.when(msCustomersClient.getAddressById(Mockito.anyLong())).thenReturn(responseEntityWithAddressDtoException);
		
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, ()-> addressService.getById(addressId));

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(ResourceNotFoundException.class)
				);
	}

	@Test
	void AddressServiceTest_create_ReturnAddressDto() {

		Mockito.when(msCustomersClient.create(Mockito.any(AddressDto.class))).thenReturn(new ResponseEntity<>(addressDto, HttpStatus.CREATED));
		Mockito.when(addressMvcMapper.fromMvcDtoToDto(Mockito.any(AddressMvcDto.class))).thenReturn(addressDto);
		
		AddressDto addressDto = addressService.create(new AddressMvcDto());
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(addressDto).isNotNull(),
				()-> Assertions.assertThat(addressDto.addressId()).isEqualTo(addressId),
				()-> Assertions.assertThat(addressDto.location()).isEqualTo(location),
				()-> Assertions.assertThat(addressDto.city()).isEqualTo(city),
				()-> Assertions.assertThat(addressDto.country()).isEqualTo(country),
				()-> Assertions.assertThat(addressDto.email()).isEqualTo(email)
				);
	}
	

	@Test
	void AddressServiceTest_create_ReturnResourceNotFoundException() {

		Mockito.when(msCustomersClient.create(Mockito.any(AddressDto.class))).thenReturn(new ResponseEntity<>(new AddressDto(), HttpStatus.CREATED));
		Mockito.when(addressMvcMapper.fromMvcDtoToDto(Mockito.any(AddressMvcDto.class))).thenReturn(addressDto);
		
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, ()-> addressService.create(addressMvcDtoVoid));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(ResourceNotFoundException.class)
				);
	}

	@Test
	void AddressServiceTest_update_ReturnAddressDto() {

		Mockito.when(msCustomersClient.update(Mockito.anyLong(), Mockito.any(AddressDto.class))).thenReturn(new ResponseEntity<>(addressDto, HttpStatus.OK));
		Mockito.when(addressMvcMapper.fromMvcDtoToDto(Mockito.any(AddressMvcDto.class))).thenReturn(addressDto);
		
		AddressDto addressDto = addressService.update(3l, new AddressMvcDto());
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(addressDto).isNotNull(),
				()-> Assertions.assertThat(addressDto.addressId()).isEqualTo(addressId),
				()-> Assertions.assertThat(addressDto.location()).isEqualTo(location),
				()-> Assertions.assertThat(addressDto.city()).isEqualTo(city),
				()-> Assertions.assertThat(addressDto.country()).isEqualTo(country),
				()-> Assertions.assertThat(addressDto.email()).isEqualTo(email)
				);
	}

	@Test
	void AddressServiceTest_update_ReturnResourceNotFoundException() {

		Mockito.when(msCustomersClient.update(Mockito.anyLong(),Mockito.any(AddressDto.class))).thenReturn(new ResponseEntity<>(new AddressDto(), HttpStatus.OK));
		Mockito.when(addressMvcMapper.fromMvcDtoToDto(Mockito.any(AddressMvcDto.class))).thenReturn(addressDto);
		
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, ()-> addressService.update(4l, addressMvcDtoVoid));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(ResourceNotFoundException.class)
				);
	}

	@Test
	void AddressServiceTest_delete_ReturnBooleanTrue() {
		
		Mockito.when(msCustomersClient.deleteAddress(Mockito.anyLong())).thenReturn(responseEntityBooleanTrue);
		
		Boolean resultValue = addressService.delete(88l);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(resultValue).isNotNull(),
				()-> Assertions.assertThat(resultValue).isTrue()
				);
	}

	@Test
	void AddressServiceTest_delete_ReturnEntityAssociatedException() {
		
		Mockito.when(msCustomersClient.deleteAddress(Mockito.anyLong())).thenReturn(new ResponseEntity<>(null, HttpStatus.OK));
		
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(EntityAssociatedException.class, () -> addressService.delete(88l));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(EntityAssociatedException.class)
				);
	}

	@Test
	void AddressServiceTest_setCustomer_ReturnBooleanTrue() {
		
		Mockito.when(msCustomersClient.setCustomer(Mockito.anyLong(),Mockito.anyLong())).thenReturn(responseEntityBooleanTrue);
		
		Boolean resultValue = addressService.setCustomer(8l,8l);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(resultValue).isNotNull(),
				()-> Assertions.assertThat(resultValue).isTrue()
				);
	}

	@Test
	void AddressServiceTest_setCustomer_ReturnEntityAssociatedException() {
		
		Mockito.when(msCustomersClient.setCustomer(Mockito.anyLong(),Mockito.anyLong())).thenReturn(new ResponseEntity<>(null, HttpStatus.OK));
		
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, () -> addressService.setCustomer(8l,8l));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(ResourceNotFoundException.class)
				);
	}
	
}
