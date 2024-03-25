package dev.ime.dto;

import java.io.Serializable;

import dev.ime.tool.RegexPattern;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

@Data
@Generated
@NoArgsConstructor
public class ProductMvcDto implements Serializable {

	private static final long serialVersionUID = -4899382959456174327L;

	private Long productId;
	
	@Pattern( regexp = RegexPattern.NAME_FULL )
	private String name;
	
	@NotNull@Min(0)@Max(99999) 
	private Double unitPrice;
	
	@NotNull@Min(0)@Max(99999) 
	private Integer unitInStock;
	
	@NotNull 
	private Boolean discontinued;

	@NotNull 
	private Long categoryId;
	
}
