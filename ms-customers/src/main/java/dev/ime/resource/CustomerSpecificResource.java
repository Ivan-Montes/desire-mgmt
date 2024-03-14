package dev.ime.resource;

import java.util.List;

import org.springframework.http.ResponseEntity;

import dev.ime.dto.AddressDto;

public interface CustomerSpecificResource {

	ResponseEntity<Boolean>addAddress(Long customerId, Long addressId);
	
	ResponseEntity<List<AddressDto>>getAddressesInCustomer(Long customerId);
}
