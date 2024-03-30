package dev.ime.dto;

import dev.ime.tool.RegexPattern;
import dev.ime.tool.SomeConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CustomerDto(
		Long customerId,
		@NotNull @NotBlank @Size( min = 1, max = 100 ) @Pattern( regexp = RegexPattern.DESCRIPTION_FULL )
		String companyName,
		@NotNull @NotBlank @Size( min = 1, max = 50 ) @Pattern( regexp = RegexPattern.NAME_FULL )
		String contactName
		) {
	public CustomerDto() {
		this(0L,SomeConstants.DATELESS,SomeConstants.DATELESS);
	}
}
