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
import dev.ime.dto.CustomerDto;
import dev.ime.dtomvc.CustomerMvcDto;
import dev.ime.exception.EntityAssociatedException;
import dev.ime.exception.ResourceNotFoundException;
import dev.ime.mapper.impl.CustomerMvcMapper;


@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

	@Mock
	private MsCustomersClientImpl msCustomersClient;

	@Mock
	private CustomerMvcMapper customerMvcMapper;	

	@InjectMocks
	private CustomerService customerService;

	@Mock
	private ResponseEntity<List<CustomerDto>> responseListCustomerDto;
	private ResponseEntity<CustomerDto> responseCustomerDto;
	private ResponseEntity<CustomerDto> responseCustomerDtoException;
	private ResponseEntity<Boolean> responseEntityBooleanTrue;
	
	private List<CustomerDto>customerDtoList;

	private final Long customerId = 66L;
	private final String companyName = "Amegakure";
	private final String contactName = "Kohan";
	private CustomerDto customerDtoTest;
	private CustomerMvcDto customerMvcDtoVoid;
	
	@BeforeEach
	private void createObjects() {
		
		customerDtoList = new ArrayList<>();
		customerDtoTest = new CustomerDto(customerId, companyName, contactName);
		customerMvcDtoVoid = new CustomerMvcDto();
		
		responseCustomerDto = new ResponseEntity<>(customerDtoTest, HttpStatus.OK);
		responseCustomerDtoException = new ResponseEntity<>(new CustomerDto(), HttpStatus.OK);
		responseEntityBooleanTrue = new ResponseEntity<>(true, HttpStatus.OK);
		
	}
		

	@Test
	void CustomerService_getAll_ResturnListCustomer() {
		
		customerDtoList.add(customerDtoTest);
		Mockito.when(msCustomersClient.getAllCustomer()).thenReturn(responseListCustomerDto);
		Mockito.when(responseListCustomerDto.getBody()).thenReturn(customerDtoList);
		
		List<CustomerDto> list = customerService.getAll();
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(list).isNotNull(),
				()-> Assertions.assertThat(list).isNotEmpty(),
				()-> Assertions.assertThat(list).hasSize(1),
				()-> Assertions.assertThat(list.get(0).customerId()).isEqualTo(customerId),
				()-> Assertions.assertThat(list.get(0).companyName()).isEqualTo(companyName),
				()-> Assertions.assertThat(list.get(0).contactName()).isEqualTo(contactName)
				);
	}

	@Test
	void CustomerService_getAllPaged_ResturnListCustomer() {
		
		customerDtoList.add(customerDtoTest);
		Mockito.when(msCustomersClient.getAllCustomerPaged(Mockito.anyInt(),Mockito.anyInt())).thenReturn(responseListCustomerDto);
		Mockito.when(responseListCustomerDto.getBody()).thenReturn(customerDtoList);
		
		List<CustomerDto> list = customerService.getAllPaged(11,3);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(list).isNotNull(),
				()-> Assertions.assertThat(list).isNotEmpty(),
				()-> Assertions.assertThat(list).hasSize(1),
				()-> Assertions.assertThat(list.get(0).customerId()).isEqualTo(customerId),
				()-> Assertions.assertThat(list.get(0).companyName()).isEqualTo(companyName),
				()-> Assertions.assertThat(list.get(0).contactName()).isEqualTo(contactName)
				);
	}

	@Test
	void CustomerService_getById_ReturnCustomerDto() {
		
		Mockito.when(msCustomersClient.getCustomerById(Mockito.anyLong())).thenReturn(responseCustomerDto);
		
		CustomerDto customerDto = customerService.getById(77l);		

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(customerDto).isNotNull(),
				()-> Assertions.assertThat(customerDto.customerId()).isEqualTo(customerId),
				()-> Assertions.assertThat(customerDto.companyName()).isEqualTo(companyName),
				()-> Assertions.assertThat(customerDto.contactName()).isEqualTo(contactName)
				);
		
	}

	@Test
	void CustomerService_getById_ReturnResourceNotFoundException() {
		
		Mockito.when(msCustomersClient.getCustomerById(Mockito.anyLong())).thenReturn(responseCustomerDtoException);
		
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, () -> customerService.getById(77l))  ;		

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(ResourceNotFoundException.class)
				);
		
	}
	

	@Test
	void CustomerService_create_ReturnCustomerDto() {
		
		Mockito.when(msCustomersClient.create(Mockito.any(CustomerDto.class))).thenReturn(new ResponseEntity<>(customerDtoTest, HttpStatus.CREATED));
		Mockito.when(customerMvcMapper.fromMvcDtoToDto(Mockito.any(CustomerMvcDto.class))).thenReturn(customerDtoTest);
		
		CustomerDto customerDto = customerService.create(new CustomerMvcDto());
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(customerDto).isNotNull(),
				()-> Assertions.assertThat(customerDto.customerId()).isEqualTo(customerId),
				()-> Assertions.assertThat(customerDto.companyName()).isEqualTo(companyName),
				()-> Assertions.assertThat(customerDto.contactName()).isEqualTo(contactName)
				);		
	}
	

	@Test
	void CustomerService_create_ReturnResourceNotFoundException() {
		
		Mockito.when(msCustomersClient.create(Mockito.any(CustomerDto.class))).thenReturn(new ResponseEntity<>(new CustomerDto(), HttpStatus.CREATED));
		Mockito.when(customerMvcMapper.fromMvcDtoToDto(Mockito.any(CustomerMvcDto.class))).thenReturn(customerDtoTest);
		
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, () -> customerService.create(customerMvcDtoVoid))  ;		

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(ResourceNotFoundException.class)
				);		
	}

	@Test
	void CustomerService_update_ReturnCustomerDto() {
		
		Mockito.when(msCustomersClient.update(Mockito.anyLong(), Mockito.any(CustomerDto.class))).thenReturn(responseCustomerDto);
		Mockito.when(customerMvcMapper.fromMvcDtoToDto(Mockito.any(CustomerMvcDto.class))).thenReturn(customerDtoTest);
		
		CustomerDto customerDto = customerService.update(customerId, new CustomerMvcDto());
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(customerDto).isNotNull(),
				()-> Assertions.assertThat(customerDto.customerId()).isEqualTo(customerId),
				()-> Assertions.assertThat(customerDto.companyName()).isEqualTo(companyName),
				()-> Assertions.assertThat(customerDto.contactName()).isEqualTo(contactName)
				);		
	}

	@Test
	void CustomerService_update_ReturnResourceNotFoundException() {
		
		Mockito.when(msCustomersClient.update(Mockito.anyLong(), Mockito.any(CustomerDto.class))).thenReturn(responseCustomerDtoException);
		Mockito.when(customerMvcMapper.fromMvcDtoToDto(Mockito.any(CustomerMvcDto.class))).thenReturn(customerDtoTest);
		
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, () -> customerService.update(customerId, customerMvcDtoVoid));		

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(ResourceNotFoundException.class)
				);
	}

	@Test
	void CustomerService_delete_ReturnBooleanTrue() {
		
		Mockito.when(msCustomersClient.deleteCustomer(Mockito.anyLong())).thenReturn(responseEntityBooleanTrue);
		
		Boolean resultValue = customerService.delete(06L);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(resultValue).isNotNull(),
				()-> Assertions.assertThat(resultValue).isTrue()
				);
	}

	@Test
	void CustomerService_delete_ReturnEntityAssociatedException() {
		
		Mockito.when(msCustomersClient.deleteCustomer(Mockito.anyLong())).thenReturn(new ResponseEntity<>(null, HttpStatus.OK));
		
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(EntityAssociatedException.class, () -> customerService.delete(customerId));		

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(EntityAssociatedException.class)
				);		
	}

	@Test
	void CustomerService_addAddress_ReturnBooleanTrue() {
		
		Mockito.when(msCustomersClient.addAddress(Mockito.anyLong(), Mockito.anyLong())).thenReturn(responseEntityBooleanTrue);
		
		Boolean resultValue = customerService.addAddress(073L, 06L);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(resultValue).isNotNull(),
				()-> Assertions.assertThat(resultValue).isTrue()
				);		
	}

	@Test
	void CustomerService_addAddress_ReturnResourceNotFoundException() {
		
		Mockito.when(msCustomersClient.addAddress(Mockito.anyLong(), Mockito.anyLong())).thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, () -> customerService.addAddress(073L, 06L));		

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(ResourceNotFoundException.class)
				);		
	}
	
}
