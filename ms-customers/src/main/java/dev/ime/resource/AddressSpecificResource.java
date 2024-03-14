package dev.ime.resource;

import org.springframework.http.ResponseEntity;

public interface AddressSpecificResource {

	ResponseEntity<Boolean>setCustomer(Long addressId, Long customerId);
}
