package dev.ime.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record OrderDetailDto(
		Long orderDetailId,
		@NotNull@Min(0)@Max(9999) Integer quantity,
		@NotNull@Min(0)@Max(9999) Double discount,
		@NotNull@Min(0)@Max(9999) Long productId,
		@NotNull@Min(0)@Max(9999) Long orderId
		) {

	public OrderDetailDto() {
		this(0L, 0, 0D, 0L, 0L);
	}
}
