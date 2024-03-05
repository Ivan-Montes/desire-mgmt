package dev.ime.resource;

import org.springframework.http.ResponseEntity;

public interface OrderSpecificResource {

	ResponseEntity<Boolean> addOrderDetail(Long orderId, Long orderDetailId);
	
}
