package dev.ime.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import dev.ime.dto.CustomerDto;

@FeignClient( name = "ms-customers", url = "${ms-customers.url}")
public interface MsCustomersClient {

	@GetMapping("/api/customers/{id}")
	ResponseEntity<CustomerDto>getCustomerById(@PathVariable Long id);
}
