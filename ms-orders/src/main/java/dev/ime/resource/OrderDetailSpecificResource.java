package dev.ime.resource;

import org.springframework.http.ResponseEntity;

public interface OrderDetailSpecificResource {

	ResponseEntity<Boolean> setOrder(Long orderDetailId, Long orderId);
	ResponseEntity<Boolean> getAnyByProductId(Long productId);
}
