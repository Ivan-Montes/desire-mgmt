package dev.ime.dto;

import dev.ime.tool.RegexPattern;
import dev.ime.tool.SomeConstants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AddressDto(
		Long addressId,		
		@NotNull @NotBlank @Size( min = 1, max = 100 ) @Pattern( regexp = RegexPattern.LOCATION_FULL )
		String location,
		@NotNull @NotBlank @Size( min = 1, max = 50 ) @Pattern( regexp = RegexPattern.NAME_FULL )
		String city,
		@NotNull @NotBlank @Size( min = 1, max = 50 ) @Pattern( regexp = RegexPattern.NAME_FULL )
		String country,
		@NotNull@NotBlank@Size( min = 6, max = 100 )@Email
		String email,
		@NotNull@Min(0)@Max(9999) 
		Long customerId
		) {

	public AddressDto() {
		this(0L, SomeConstants.DATELESS, SomeConstants.DATELESS, SomeConstants.DATELESS, "mail@mail.tk", 0L);
	}
}
