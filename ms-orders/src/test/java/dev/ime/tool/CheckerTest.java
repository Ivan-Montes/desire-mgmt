package dev.ime.tool;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import dev.ime.client.impl.MsCustomersClientImpl;
import dev.ime.client.impl.MsProductsClientImpl;
import dev.ime.dto.CustomerDto;
import dev.ime.dto.ProductDto;


@ExtendWith(MockitoExtension.class)
class CheckerTest {	
	
	@InjectMocks
	private Checker checker;
	
	@Mock
	private MsProductsClientImpl msProductsClient;
	
	@Mock
	private MsCustomersClientImpl msCustomersClient;


	private final Long customerId = 4L;
	private final String companyName = "Konohagakure";
	private final String contactName = "Kakashi";
	private CustomerDto customerTestDto = new CustomerDto(customerId, companyName, contactName);	

	private Long proId = 77L;
	private String proName = "Yukine";
	private Double unitPrice;
	private Integer unitInStock = 13;
	private Boolean discontinued = false;
	private Long catId = 43L;
	private ProductDto proTestDto = new ProductDto(proId, proName, unitPrice, unitInStock, discontinued, catId);

	@Test
	void Checker_localDateFormat_ReturnBooleans() {
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertTrue(checker.localDateFormat("2014-05-17")),
				()-> Assertions.assertFalse(checker.localDateFormat("asdf"))
				);
	}

	@Test
	void Checker_localDateValid_ReturnTrue() {
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertTrue(checker.localDateValid("1993-08-22"))
				);
	}
	
	@Test
	void Checker_localDateValid_ReturnFalse() {
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertFalse(checker.localDateValid("1975-08-42"))
				);
	}
	
	
	@Test
	void Checker_checkProductId_ReturnBooleanTrue() {
		
		@SuppressWarnings("unchecked")
		ResponseEntity<ProductDto> responseTest = Mockito.mock(ResponseEntity.class);		
		Mockito.when(msProductsClient.getProductById(Mockito.anyLong())).thenReturn(responseTest);
		Mockito.when(responseTest.getStatusCode()).thenReturn(HttpStatus.OK);
		Mockito.when(responseTest.getBody()).thenReturn(proTestDto);
		
		boolean resultValue = checker.checkProductId(1L);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertTrue(resultValue)
				);
	}
	
	@Test
	void Checker_checkProductId_ReturnBooleanFalse() {
		
		@SuppressWarnings("unchecked")
		ResponseEntity<ProductDto> responseTest = Mockito.mock(ResponseEntity.class);		
		Mockito.when(msProductsClient.getProductById(Mockito.anyLong())).thenReturn(responseTest);
		Mockito.when(responseTest.getStatusCode()).thenReturn(HttpStatus.NOT_FOUND);
		
		boolean resultValue = checker.checkProductId(1L);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertFalse(resultValue)
				);
	}
	
	@Test
	void Checker_checkCustomerId_ReturnBooleanTrue() {

		@SuppressWarnings("unchecked")
		ResponseEntity<CustomerDto> responseTest = Mockito.mock(ResponseEntity.class);		
		Mockito.when(msCustomersClient.getCustomerById(Mockito.anyLong())).thenReturn(responseTest);
		Mockito.when(responseTest.getStatusCode()).thenReturn(HttpStatus.OK);
		Mockito.when(responseTest.getBody()).thenReturn(customerTestDto);
		boolean resultValue = checker.checkCustomerId(1L);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertTrue(resultValue)
				);
	}
	
	@Test
	void Checker_checkCustomerId_ReturnBooleanFalse() {
		
		@SuppressWarnings("unchecked")
		ResponseEntity<CustomerDto> responseTest = Mockito.mock(ResponseEntity.class);		
		Mockito.when(msCustomersClient.getCustomerById(Mockito.anyLong())).thenReturn(responseTest);
		Mockito.when(responseTest.getStatusCode()).thenReturn(HttpStatus.NOT_FOUND);
		
		boolean resultValue = checker.checkCustomerId(1L);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertFalse(resultValue)
				);
	}
}
