package dev.ime.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient( name = "ms-orders")
public interface MsOrdersClient {

	@PutMapping("/api/orderdetails/products/{productId}")
	ResponseEntity<Boolean> getAnyByProductId(@PathVariable Long productId);
	
	
}
