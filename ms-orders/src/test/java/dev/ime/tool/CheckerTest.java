package dev.ime.tool;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import dev.ime.client.MsProductsClient;



@ExtendWith(MockitoExtension.class)
class CheckerTest {	
	
	private Checker checker;
	
	@Autowired
	private MsProductsClient msProductsClient;
	
	@BeforeEach
	private void createObjects() {
		
		checker = new Checker(msProductsClient);
	}
	
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
	
	
}
