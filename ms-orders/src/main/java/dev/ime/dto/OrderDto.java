package dev.ime.dto;

import dev.ime.tool.RegexPattern;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record OrderDto(
		Long orderId,
		@NotNull@Min(0)@Max(9999) Long customerId,
		@Pattern( regexp = RegexPattern.LOCALDATE ) String orderDate		
		) {

	public OrderDto() {
		this(0L, 0L, "0000-00-00");
	}
}
