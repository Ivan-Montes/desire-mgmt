package dev.ime.dto;

import dev.ime.tool.RegexPattern;
import dev.ime.tool.SomeConstants;
import jakarta.validation.constraints.Pattern;

public record CategoryDto(
		Long categoryId,
		@Pattern( regexp = RegexPattern.NAME_FULL ) String name,
		@Pattern( regexp = RegexPattern.DESCRIPTION_FULL )String description
		) {
	public CategoryDto() {
		this(0L, SomeConstants.DATELESS, SomeConstants.DATELESS);
	}
}
