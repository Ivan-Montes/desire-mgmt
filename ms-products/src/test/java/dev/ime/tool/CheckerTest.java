package dev.ime.tool;


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

import dev.ime.client.impl.MsOrdersClientImpl;


@ExtendWith(MockitoExtension.class)
class CheckerTest {

	@Mock
	private MsOrdersClientImpl msOrdersClient;
	
	@InjectMocks
	private Checker checker;
	
	private ResponseEntity<Boolean> responseEntityBooleanTrue;
	private ResponseEntity<Boolean> responseEntityBooleanFalse;
	
	@BeforeEach
	private void createObjects() {
		
		responseEntityBooleanTrue = new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);
		responseEntityBooleanFalse = new ResponseEntity<>(Boolean.TRUE, HttpStatus.NOT_FOUND);
		
	}
	
	@Test
	void Checker_checkProductId_ReturnBooleanTrue() {
		
		Mockito.when(msOrdersClient.getAnyByProductId(Mockito.anyLong())).thenReturn(responseEntityBooleanTrue);
		
		boolean resultValue = checker.checkProductId(2L);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(resultValue).isNotNull(),
				()-> Assertions.assertThat(resultValue).isTrue()
				);
	}

	@Test
	void Checker_checkProductId_ReturnBooleanFalse() {
		
		Mockito.when(msOrdersClient.getAnyByProductId(Mockito.anyLong())).thenReturn(responseEntityBooleanFalse);
		
		boolean resultValue = checker.checkProductId(2L);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(resultValue).isNotNull(),
				()-> Assertions.assertThat(resultValue).isFalse()
				);
	}
}
