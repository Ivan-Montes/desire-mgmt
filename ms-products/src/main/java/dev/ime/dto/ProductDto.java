package dev.ime.dto;

import dev.ime.tool.RegexPattern;
import dev.ime.tool.SomeConstants;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record ProductDto(
		Long productId,
		@Pattern( regexp = RegexPattern.NAME_FULL ) String name,
		@NotNull@Min(0)@Max(99999) Double unitPrice,
		@NotNull@Min(0)@Max(99999) Integer unitInStock,
		@NotNull Boolean discontinued,
		Long categoryId
		) {

	public ProductDto() {
		this(0L, SomeConstants.DATELESS, 0D, 0, false, 0L);
	}
}
