package dev.ime.client.impl;


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

import dev.ime.client.MsOrdersClient;


@ExtendWith(MockitoExtension.class)
class MsOrdersClientImplTest {
	
	@Mock
	private MsOrdersClient msOrdersClient;

	@InjectMocks
	private MsOrdersClientImpl msOrdersClientImpl;
	
	private ResponseEntity<Boolean> responseEntityWithTrue;
	
	@BeforeEach
	private void createObjects() {
		
		responseEntityWithTrue = new ResponseEntity<>( Boolean.TRUE, HttpStatus.OK)	;
	}
	
	@Test
	void MsOrdersClientImpl_getAnyByProductId_ReturnesponseEntityBooleanTrue() {
		
		Mockito.when(msOrdersClient.getAnyByProductId(Mockito.anyLong())).thenReturn(responseEntityWithTrue);
		
		ResponseEntity<Boolean> response = msOrdersClientImpl.getAnyByProductId(7L);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK)
				);
	}

	@Test
	void MsOrdersClientImpl_getAnyByProductId_ReturnesponseEntityBooleanFalseError() {
		
		Mockito.when(msOrdersClient.getAnyByProductId(Mockito.anyLong())).thenThrow(new RuntimeException());
		
		ResponseEntity<Boolean> response = msOrdersClientImpl.getAnyByProductId(7L);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND)
				);
	}

}
