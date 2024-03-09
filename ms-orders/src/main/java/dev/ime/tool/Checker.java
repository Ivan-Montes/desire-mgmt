package dev.ime.tool;

import java.time.LocalDate;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import dev.ime.client.impl.MsProductsClientImpl;
import dev.ime.dto.ProductDto;

@Component
public class Checker {

	private final MsProductsClientImpl msProductsClient;
	
	public Checker(MsProductsClientImpl msProductsClient) {
		super();
		this.msProductsClient = msProductsClient;
	}

	public boolean localDateFormat(String dateString) {
		
		return dateString.matches(RegexPattern.LOCALDATE);
		
	}
	
	public boolean localDateValid(String dateString) {
		
		try {
			
			LocalDate.parse(dateString);
			
		}catch(Exception ex){
			
			return false;
			
		}
		
		return true;
		
	}
	
	public boolean checkCustomerId(Long customerId) {
		/*
		ResponseEntity<CustomerDto> response = msCustomersClient.getCustomerById(customerId);
		return response.getStatusCode() == HttpStatus.OK;*/
		
		return true;
	}
	
    public boolean checkProductId(Long productId) {
    
		ResponseEntity<ProductDto> response = msProductsClient.getProductById(productId);
		return response.getStatusCode() == HttpStatus.OK;
		
	}
    
    
}
