package dev.ime.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import dev.ime.dto.AddressDto;
import dev.ime.dto.CustomerDto;
import jakarta.validation.Valid;

@FeignClient( name = "ms-customers" )
public interface MsCustomersClient {

	@GetMapping("/api/customers")
	ResponseEntity<List<CustomerDto>> getAllCustomer();

	@GetMapping("/api/customers")    
	ResponseEntity<List<CustomerDto>> getAllCustomerPaged(
			@RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer size);
	
	@GetMapping("/api/customers/{id}")
	ResponseEntity<CustomerDto> getCustomerById(@PathVariable Long id);
	
	@PostMapping("/api/customers")
	ResponseEntity<CustomerDto> create(@Valid @RequestBody CustomerDto dto);

	@PutMapping("/api/customers/{id}")
	ResponseEntity<CustomerDto> update(@PathVariable Long id, @Valid @RequestBody CustomerDto dto);

	@DeleteMapping("/api/customers/{id}")
	ResponseEntity<Boolean> deleteCustomer(@PathVariable Long id);

	@PutMapping("/api/customers/{customerId}/addresses/{addressId}")
	ResponseEntity<Boolean> addAddress(@PathVariable Long customerId, @PathVariable Long addressId);

	@GetMapping("/api/customers/{customerId}/addresses")
	ResponseEntity<List<AddressDto>> getAddressesInCustomer(@PathVariable Long customerId);
	
	
	
	@GetMapping("/api/addresses")
	ResponseEntity<List<AddressDto>> getAllAddress();

	@GetMapping("/api/addresses")    
	ResponseEntity<List<AddressDto>> getAllAddressPaged(
			@RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer size);
	
	@GetMapping("/api/addresses/{id}")
	ResponseEntity<AddressDto> getAddressById(@PathVariable Long id);

	@PostMapping("/api/addresses")
	ResponseEntity<AddressDto> create(@Valid @RequestBody AddressDto dto);

	@PutMapping("/api/addresses/{id}")
	ResponseEntity<AddressDto> update(@PathVariable Long id, @Valid @RequestBody AddressDto dto);

	@DeleteMapping("/api/addresses/{id}")
	ResponseEntity<Boolean> deleteAddress(@PathVariable Long id);

	@PutMapping("/api/addresses/{addressId}/customers/{customerId}")
	ResponseEntity<Boolean> setCustomer(@PathVariable Long addressId, @PathVariable Long customerId);
	
}
