package dev.ime.dto;

import dev.ime.tool.SomeConstants;

public record ProductDto(
		Long productId,
		String name,
		Double unitPrice,
		Integer unitInStock,
		Boolean discontinued,
		Long categoryId
		) {

	public ProductDto() {
		this(0L, SomeConstants.DATELESS, 0D, 0, false, 0L);
	}
}
