package dev.ime.dtomvc;

import java.io.Serializable;

import dev.ime.tool.RegexPattern;
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
public class CustomerMvcDto implements Serializable{

	private static final long serialVersionUID = 1564156612976145840L;

	private Long customerId;
	
	@NotNull @NotBlank @Size( min = 1, max = 100 ) @Pattern( regexp = RegexPattern.DESCRIPTION_FULL )
	private String companyName;
	
	@NotNull @NotBlank @Size( min = 1, max = 50 ) @Pattern( regexp = RegexPattern.NAME_FULL )
	private String contactName;
	
}
