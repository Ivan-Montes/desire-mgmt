package dev.ime.client.impl;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import dev.ime.client.MsCustomersClient;
import dev.ime.dto.CustomerDto;



@ExtendWith(MockitoExtension.class)
class MsCustomersClientImplTest {

	@Mock
	private MsCustomersClient msCustomersClient;
	
	@InjectMocks
	private MsCustomersClientImpl msCustomersClientImpl;
	
	@Test
	void MsCustomersClientImpl_getCustomerById_ReturnResponseEntityCustomerDto() {
		
		Mockito.when(msCustomersClient.getCustomerById(Mockito.anyLong())).thenReturn(new ResponseEntity<>(new CustomerDto(),HttpStatus.OK));
		
		ResponseEntity<CustomerDto> response = msCustomersClientImpl.getCustomerById(2l);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK)
				);
	}

	@Test
	void MsCustomersClientImpl_getCustomerById_ReturnResponseEntityCustomerDtoError() {
		
		Mockito.when(msCustomersClient.getCustomerById(Mockito.anyLong())).thenThrow(new RuntimeException());
		
		ResponseEntity<CustomerDto> response = msCustomersClientImpl.getCustomerById(2l);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND)
				);
	}
	

}
