package dev.ime.resource;

import java.util.Set;

import org.springframework.http.ResponseEntity;

import dev.ime.dto.OrderDetailDto;

public interface OrderSpecificResource {

	ResponseEntity<Boolean> addOrderDetail(Long orderId, Long orderDetailId);
	ResponseEntity<Set<OrderDetailDto>> getLinesByOrderId(Long orderId);
}
