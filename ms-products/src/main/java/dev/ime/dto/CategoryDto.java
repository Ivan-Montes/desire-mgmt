package dev.ime.dto;

import dev.ime.tool.SomeConstants;

public record CategoryDto(
		Long categoryId,
		String name,
		String description
		) {
	public CategoryDto() {
		this(0L, SomeConstants.DATELESS, SomeConstants.DATELESS);
	}
}
