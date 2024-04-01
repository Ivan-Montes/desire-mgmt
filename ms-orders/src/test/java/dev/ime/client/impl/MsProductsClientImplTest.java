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

import dev.ime.client.MsProductsClient;
import dev.ime.dto.ProductDto;

@ExtendWith(MockitoExtension.class)
class MsProductsClientImplTest {

	@Mock
	private MsProductsClient msProductsClient;

	@InjectMocks
	private MsProductsClientImpl msProductsClientImpl;
	
	private ResponseEntity<ProductDto> responseEntityWithDtoEmpty;
	
	@BeforeEach
	private void createObjects() {
		
		responseEntityWithDtoEmpty = new ResponseEntity<>(new ProductDto(),HttpStatus.OK);
	}
	
	@Test
	void MsProductsClientImpl_getProductById_ReturnesponseEntityProductDto() {
		
		Mockito.when(msProductsClient.getProductById(Mockito.anyLong())).thenReturn(responseEntityWithDtoEmpty);
		
		ResponseEntity<ProductDto> response = msProductsClientImpl.getProductById(3L);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK)
				);
	}

	@Test
	void MsProductsClientImpl_getProductById_ReturnesponseEntityProductDtoError() {
		
		Mockito.when(msProductsClient.getProductById(Mockito.anyLong())).thenThrow(new RuntimeException());
		
		ResponseEntity<ProductDto> response = msProductsClientImpl.getProductById(3L);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND)
				);
	}

}
