package dev.ime.dtomvc;

import java.io.Serializable;

import dev.ime.tool.RegexPattern;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
@Generated
public class AddressMvcDto implements Serializable{
	
	private static final long serialVersionUID = -3623017695937676127L;

	private Long addressId;		
	
	@NotNull @NotBlank @Size( min = 1, max = 100 ) @Pattern( regexp = RegexPattern.LOCATION_FULL )
	private String location;
	
	@NotNull @NotBlank @Size( min = 1, max = 50 ) @Pattern( regexp = RegexPattern.NAME_FULL )
	private String city;
	
	@NotNull @NotBlank @Size( min = 1, max = 50 ) @Pattern( regexp = RegexPattern.NAME_FULL )
	private String country;
	
	@NotNull@NotBlank@Size( min = 6, max = 100 )@Email
	private String email;
	
	@NotNull@Min(0)@Max(9999) 
	private Long customerId;
	
}
