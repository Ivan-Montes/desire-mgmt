package dev.ime.client.impl;


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

import dev.ime.client.MsCustomersClient;
import dev.ime.dto.AddressDto;
import dev.ime.dto.CustomerDto;
import dev.ime.dtomvc.AddressMvcDto;
import dev.ime.dtomvc.CustomerMvcDto;
import dev.ime.mapper.impl.AddressMvcMapper;
import dev.ime.mapper.impl.CustomerMvcMapper;

@ExtendWith(MockitoExtension.class)
class MsCustomersClientImplTest {
	
	@Mock
	private MsCustomersClient msCustomersClient;

	@InjectMocks
	private MsCustomersClientImpl msCustomersClientImpl;
	
	
	private List<CustomerDto> customerDtoList;	
	private final Long customerId = 4L;
	private final String companyName = "Konohagakure";
	private final String contactName = "Kakashi";
	private CustomerMvcDto customerMvcDto;
	private CustomerDto customerDto;
	private CustomerMvcMapper customerMvcMapper;
	private ResponseEntity<Boolean> responseEntityBoolTrue;
	private ResponseEntity<CustomerDto> responseEntityWithCustomerDto;
	private ResponseEntity<List<CustomerDto>> responseEntityWithListCustomerDto;

	private List<AddressDto> addressDtoList;
	private final Long addressId = 7L;
	private final String location = "Cattedrale di Santa Maria del Fiore";
	private final String city = "Florence";
	private final String country = "Italy";
	private final String email = "florence@florence.it";
	private AddressMvcDto addressMvcDto;
	private AddressDto addressDto;
	private AddressMvcMapper addressMvcMapper;
	private ResponseEntity<AddressDto> responseEntityWithAddressDto;
	private ResponseEntity<List<AddressDto>> responseEntityWithListAddressDto;
	
	@BeforeEach
	private void createObjects() {

		customerDtoList = new ArrayList<>();
		
		customerMvcMapper = new CustomerMvcMapper();		
		
		customerMvcDto = new CustomerMvcDto();
		customerMvcDto.setCustomerId(customerId);
		customerMvcDto.setCompanyName(companyName);
		customerMvcDto.setContactName(contactName);
		
		customerDto = customerMvcMapper.fromMvcDtoToDto(customerMvcDto);

		responseEntityBoolTrue = new ResponseEntity<>(true, HttpStatus.OK);
		responseEntityWithCustomerDto = new ResponseEntity<>(customerDto, HttpStatus.OK);
		responseEntityWithListCustomerDto = new ResponseEntity<>(customerDtoList, HttpStatus.OK);
		

		addressDtoList = new ArrayList<>();
		
		addressMvcMapper = new AddressMvcMapper();
		
		addressMvcDto = new AddressMvcDto();
		addressMvcDto.setAddressId(addressId);
		addressMvcDto.setLocation(location);
		addressMvcDto.setCity(city);
		addressMvcDto.setCountry(country);
		addressMvcDto.setEmail(email);
		addressMvcDto.setCustomerId(customerId);
		
		addressDto = addressMvcMapper.fromMvcDtoToDto(addressMvcDto);
		
		responseEntityWithAddressDto = new ResponseEntity<>(addressDto, HttpStatus.OK);
		responseEntityWithListAddressDto = new ResponseEntity<>(addressDtoList, HttpStatus.OK);		
		
	}
	
	@Test
	void MsCustomersClientImpl_getAllCustomer_ReturnResponseEntityListCustomerDto() {
		
		customerDtoList.add(customerDto);
		Mockito.when(msCustomersClient.getAllCustomer()).thenReturn(responseEntityWithListCustomerDto);
		
		ResponseEntity<List<CustomerDto>> response = msCustomersClientImpl.getAllCustomer();
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
				()-> Assertions.assertThat(response.getBody()).hasSize(1)
				);
	}

	@Test
	void MsCustomersClientImpl_getAllCustomer_ReturnResponseEntityListCustomerDtoError() {
		
		
		Mockito.when(msCustomersClient.getAllCustomer()).thenThrow(new RuntimeException());
		
		ResponseEntity<List<CustomerDto>> response = msCustomersClientImpl.getAllCustomer();
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND),
				()-> Assertions.assertThat(response.getBody()).isEmpty()
				);
	}

	@Test
	void MsCustomersClientImpl_getAllCustomerPaged_ReturnResponseEntityListCustomerDto() {
		
		customerDtoList.add(customerDto);
		Mockito.when(msCustomersClient.getAllCustomerPaged(Mockito.anyInt(),Mockito.anyInt())).thenReturn(responseEntityWithListCustomerDto);
		
		ResponseEntity<List<CustomerDto>> response = msCustomersClientImpl.getAllCustomerPaged(1,2);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
				()-> Assertions.assertThat(response.getBody()).hasSize(1)
				);
	}

	@Test
	void MsCustomersClientImpl_getAllCustomerPaged_ReturnResponseEntityListCustomerDtoError() {
		
		customerDtoList.add(customerDto);
		Mockito.when(msCustomersClient.getAllCustomerPaged(Mockito.anyInt(),Mockito.anyInt())).thenThrow(new RuntimeException());
		
		ResponseEntity<List<CustomerDto>> response = msCustomersClientImpl.getAllCustomerPaged(1,2);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND),
				()-> Assertions.assertThat(response.getBody()).isEmpty()
				);
	}

	@Test
	void MsCustomersClientImpl_getCustomerById_ReturnResponseEntityCustomerDto() {
		
		Mockito.when(msCustomersClient.getCustomerById(Mockito.anyLong())).thenReturn(responseEntityWithCustomerDto);
		
		ResponseEntity<CustomerDto> response = msCustomersClientImpl.getCustomerById(customerId);

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
				()-> Assertions.assertThat(response.getBody()).isNotNull(),
				()-> Assertions.assertThat(response.getBody().customerId()).isEqualTo(customerId)
				);
	}

	@Test
	void MsCustomersClientImpl_getCustomerById_ReturnResponseEntityCustomerDtoError() {
		
		Mockito.when(msCustomersClient.getCustomerById(Mockito.anyLong())).thenThrow(new RuntimeException());
		
		ResponseEntity<CustomerDto> response = msCustomersClientImpl.getCustomerById(customerId);

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND),
				()-> Assertions.assertThat(response.getBody()).isNotNull(),
				()-> Assertions.assertThat(response.getBody().customerId()).isZero()
				);
	}
	
	@Test
	void MsCustomersClientImpl_create_ReturnResponseEntityCustomerDto() {
		
		Mockito.when(msCustomersClient.create(Mockito.any(CustomerDto.class))).thenReturn(responseEntityWithCustomerDto);
		
		ResponseEntity<CustomerDto> response = msCustomersClientImpl.create(customerDto);

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
				()-> Assertions.assertThat(response.getBody()).isNotNull(),
				()-> Assertions.assertThat(response.getBody().customerId()).isEqualTo(customerId)
				);
	}

	@Test
	void MsCustomersClientImpl_create_ReturnResponseEntityCustomerDtoError() {
		
		Mockito.when(msCustomersClient.create(Mockito.any(CustomerDto.class))).thenThrow(new RuntimeException());
		
		ResponseEntity<CustomerDto> response = msCustomersClientImpl.create(customerDto);

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND),
				()-> Assertions.assertThat(response.getBody()).isNotNull(),
				()-> Assertions.assertThat(response.getBody().customerId()).isZero()
				);
	}

	@Test
	void MsCustomersClientImpl_update_ReturnResponseEntityCustomerDto() {
		
		Mockito.when(msCustomersClient.update(Mockito.anyLong(),Mockito.any(CustomerDto.class))).thenReturn(responseEntityWithCustomerDto);
		
		ResponseEntity<CustomerDto> response = msCustomersClientImpl.update(customerId, customerDto);

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
				()-> Assertions.assertThat(response.getBody()).isNotNull(),
				()-> Assertions.assertThat(response.getBody().customerId()).isEqualTo(customerId)
				);
	}

	@Test
	void MsCustomersClientImpl_update_ReturnResponseEntityCustomerDtoErrOr() {
		
		Mockito.when(msCustomersClient.update(Mockito.anyLong(),Mockito.any(CustomerDto.class))).thenThrow(new RuntimeException());
		
		ResponseEntity<CustomerDto> response = msCustomersClientImpl.update(customerId, customerDto);

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND),
				()-> Assertions.assertThat(response.getBody()).isNotNull(),
				()-> Assertions.assertThat(response.getBody().customerId()).isZero()
				);
	}

	@Test
	void MsCustomersClientImpl_deleteCustomer_ReturnResponseEntityBooleanTrue() {
		
		Mockito.when(msCustomersClient.deleteCustomer(Mockito.anyLong())).thenReturn(responseEntityBoolTrue);
		
		ResponseEntity<Boolean>response = msCustomersClientImpl.deleteCustomer(customerId);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
				()-> Assertions.assertThat(response.getBody().booleanValue())
				);
	}

	@Test
	void MsCustomersClientImpl_deleteCustomer_ReturnResponseEntityBooleanError() {
		
		Mockito.when(msCustomersClient.deleteCustomer(Mockito.anyLong())).thenThrow(new RuntimeException());
		
		ResponseEntity<Boolean>response = msCustomersClientImpl.deleteCustomer(customerId);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND),
				()-> Assertions.assertThat(response.getBody().booleanValue())
				);
	}

	@Test
	void MsCustomersClientImpl_addAddress_ReturnResponseEntityBooleanTrue() {
		
		Mockito.when(msCustomersClient.addAddress(Mockito.anyLong(),Mockito.anyLong())).thenReturn(responseEntityBoolTrue);
		
		ResponseEntity<Boolean>response = msCustomersClientImpl.addAddress(customerId, addressId);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
				()-> Assertions.assertThat(response.getBody().booleanValue())
				);
	}

	@Test
	void MsCustomersClientImpl_addAddress_ReturnResponseEntityBooleanError() {
		
		Mockito.when(msCustomersClient.addAddress(Mockito.anyLong(),Mockito.anyLong())).thenThrow(new RuntimeException());
		
		ResponseEntity<Boolean>response = msCustomersClientImpl.addAddress(customerId, addressId);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND),
				()-> Assertions.assertThat(response.getBody().booleanValue())
				);
	}

	@Test
	void MsCustomersClientImpl_getAddressesInCustomer_ReturnResponseEntityListAddressDto() {
		
		addressDtoList.add(addressDto);
		Mockito.when(msCustomersClient.getAddressesInCustomer(Mockito.anyLong())).thenReturn(responseEntityWithListAddressDto);

		ResponseEntity<List<AddressDto>> response = msCustomersClientImpl.getAddressesInCustomer(2l);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
				()-> Assertions.assertThat(response.getBody()).hasSize(1)
				);
	}

	@Test
	void MsCustomersClientImpl_getAddressesInCustomer_ReturnResponseEntityListAddressDtoError() {
		
		addressDtoList.add(addressDto);
		Mockito.when(msCustomersClient.getAddressesInCustomer(Mockito.anyLong())).thenThrow(new RuntimeException());

		ResponseEntity<List<AddressDto>> response = msCustomersClientImpl.getAddressesInCustomer(2l);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND),
				()-> Assertions.assertThat(response.getBody()).isEmpty()
				);
	}
	
	@Test
	void MsCustomersClientImpl_getAllAddress_ReturnResponseEntityListAddressDto() {
		
		addressDtoList.add(addressDto);
		Mockito.when(msCustomersClient.getAllAddress()).thenReturn(responseEntityWithListAddressDto);
		
		ResponseEntity<List<AddressDto>> response = msCustomersClientImpl.getAllAddress();
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
				()-> Assertions.assertThat(response.getBody()).hasSize(1)
				);
	}

	@Test
	void MsCustomersClientImpl_getAllAddress_ReturnResponseEntityListAddressDtoError() {
		
		addressDtoList.add(addressDto);
		Mockito.when(msCustomersClient.getAllAddress()).thenThrow(new RuntimeException());
		
		ResponseEntity<List<AddressDto>> response = msCustomersClientImpl.getAllAddress();
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND),
				()-> Assertions.assertThat(response.getBody()).isEmpty()
				);
	}
	
	@Test
	void MsCustomersClientImpl_getAllAddressPaged_ReturnResponseEntityListAddressDto() {
		
		addressDtoList.add(addressDto);
		Mockito.when(msCustomersClient.getAllAddressPaged(Mockito.anyInt(), Mockito.anyInt())).thenReturn(responseEntityWithListAddressDto);
		
		ResponseEntity<List<AddressDto>> response = msCustomersClientImpl.getAllAddressPaged(1,7);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
				()-> Assertions.assertThat(response.getBody()).hasSize(1)
				);
	}

	@Test
	void MsCustomersClientImpl_getAllAddressPaged_ReturnResponseEntityListAddressDtoError() {
		
		addressDtoList.add(addressDto);
		Mockito.when(msCustomersClient.getAllAddressPaged(Mockito.anyInt(), Mockito.anyInt())).thenThrow(new RuntimeException());
		
		ResponseEntity<List<AddressDto>> response = msCustomersClientImpl.getAllAddressPaged(1,7);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND),
				()-> Assertions.assertThat(response.getBody()).isEmpty()
				);
	}	

	@Test
	void MsCustomersClientImpl_getAddressById_ReturnResponseEntityAddressDto() {

		Mockito.when(msCustomersClient.getAddressById(Mockito.anyLong())).thenReturn(responseEntityWithAddressDto);
		
		ResponseEntity<AddressDto> response = msCustomersClientImpl.getAddressById(addressId);

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
				()-> Assertions.assertThat(response.getBody()).isNotNull(),
				()-> Assertions.assertThat(response.getBody().addressId()).isEqualTo(addressId)
				);
	}

	@Test
	void MsCustomersClientImpl_getAddressById_ReturnResponseEntityAddressDtoError() {

		Mockito.when(msCustomersClient.getAddressById(Mockito.anyLong())).thenThrow(new RuntimeException());
		
		ResponseEntity<AddressDto> response = msCustomersClientImpl.getAddressById(addressId);

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND),
				()-> Assertions.assertThat(response.getBody()).isNotNull(),
				()-> Assertions.assertThat(response.getBody().addressId()).isZero()
				);
	}

	@Test
	void MsCustomersClientImpl_create_ReturnResponseEntityAddressDto() {		

		Mockito.when(msCustomersClient.create(Mockito.any(AddressDto.class))).thenReturn(responseEntityWithAddressDto);
		
		ResponseEntity<AddressDto> response = msCustomersClientImpl.create(addressDto);

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
				()-> Assertions.assertThat(response.getBody()).isNotNull(),
				()-> Assertions.assertThat(response.getBody().addressId()).isEqualTo(addressId)
				);
	}

	@Test
	void MsCustomersClientImpl_create_ReturnResponseEntityAddressDtoError() {		

		Mockito.when(msCustomersClient.create(Mockito.any(AddressDto.class))).thenThrow(new RuntimeException());
		
		ResponseEntity<AddressDto> response = msCustomersClientImpl.create(addressDto);

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND),
				()-> Assertions.assertThat(response.getBody()).isNotNull(),
				()-> Assertions.assertThat(response.getBody().addressId()).isZero()
				);
	}
	
	@Test
	void MsCustomersClientImpl_update_ReturnResponseEntityAddressDto() {		

		Mockito.when(msCustomersClient.update(Mockito.anyLong(),Mockito.any(AddressDto.class))).thenReturn(responseEntityWithAddressDto);
		
		ResponseEntity<AddressDto> response = msCustomersClientImpl.update(addressId, addressDto);

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
				()-> Assertions.assertThat(response.getBody()).isNotNull(),
				()-> Assertions.assertThat(response.getBody().addressId()).isEqualTo(addressId)
				);
	}

	@Test
	void MsCustomersClientImpl_update_ReturnResponseEntityAddressDtoError() {		

		Mockito.when(msCustomersClient.update(Mockito.anyLong(),Mockito.any(AddressDto.class))).thenThrow(new RuntimeException());
		
		ResponseEntity<AddressDto> response = msCustomersClientImpl.update(addressId, addressDto);

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND),
				()-> Assertions.assertThat(response.getBody()).isNotNull(),
				()-> Assertions.assertThat(response.getBody().addressId()).isZero()
				);
	}

	@Test
	void MsCustomersClientImpl_deleteAddress_ReturnResponseEntityBooleanTrue() {
		
		Mockito.when(msCustomersClient.deleteAddress(Mockito.anyLong())).thenReturn(responseEntityBoolTrue);
		
		ResponseEntity<Boolean>response = msCustomersClientImpl.deleteAddress(addressId);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
				()-> Assertions.assertThat(response.getBody().booleanValue())
				);
	}

	@Test
	void MsCustomersClientImpl_deleteAddress_ReturnResponseEntityBooleanTrueError() {
		
		Mockito.when(msCustomersClient.deleteAddress(Mockito.anyLong())).thenThrow(new RuntimeException());
		
		ResponseEntity<Boolean>response = msCustomersClientImpl.deleteAddress(addressId);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND),
				()-> Assertions.assertThat(response.getBody().booleanValue())
				);
	}

	@Test
	void MsCustomersClientImpl_setCustomerReturnResponseEntityBooleanTrue() {
		
		Mockito.when(msCustomersClient.setCustomer(Mockito.anyLong(),Mockito.anyLong())).thenReturn(responseEntityBoolTrue);
		
		ResponseEntity<Boolean>response = msCustomersClientImpl.setCustomer(addressId, customerId);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
				()-> Assertions.assertThat(response.getBody().booleanValue())
				);
	}

	@Test
	void MsCustomersClientImpl_setCustomerReturnResponseEntityBooleanTrueError() {
		
		Mockito.when(msCustomersClient.setCustomer(Mockito.anyLong(),Mockito.anyLong())).thenThrow(new RuntimeException());
		
		ResponseEntity<Boolean>response = msCustomersClientImpl.setCustomer(addressId, customerId);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND),
				()-> Assertions.assertThat(response.getBody().booleanValue())
				);
	}
	
}
