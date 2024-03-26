package dev.ime.service;

public interface OrderDetailSpecificService {

	Boolean setOrder(Long orderDetailId, Long orderId);
	Boolean getAnyByProductId(Long productId);
}
