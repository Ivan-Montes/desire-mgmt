package dev.ime.tool;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import dev.ime.client.impl.MsProductsClientImpl;
import dev.ime.client.impl.MsCustomersClientImpl;
import dev.ime.dto.CustomerDto;
import dev.ime.dto.ProductDto;

@Component
public class Checker {

	private final MsProductsClientImpl msProductsClient;
	private final MsCustomersClientImpl msCustomersClient;
	
	public Checker(MsProductsClientImpl msProductsClient, MsCustomersClientImpl msCustomersClient) {
		super();
		this.msProductsClient = msProductsClient;
		this.msCustomersClient = msCustomersClient;
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

		Optional<CustomerDto> optionalDto = Optional.ofNullable(msCustomersClient.getCustomerById(customerId))
			    .filter(response -> response.getStatusCode() == HttpStatus.OK)
			    .map(ResponseEntity::getBody);

		return optionalDto.map(dto -> dto.customerId() > 0).orElse(false);
	
	}
	
    public boolean checkProductId(Long productId) {
    
		Optional<ProductDto> optionalDto = Optional.ofNullable(msProductsClient.getProductById(productId))
			    .filter(response -> response.getStatusCode() == HttpStatus.OK)
			    .map(ResponseEntity::getBody);

		return optionalDto.map(dto -> dto.productId() > 0).orElse(false);
		
	}
    
    
}
