package dev.ime.dtomvc;

import java.io.Serializable;

import dev.ime.tool.RegexPattern;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Generated
public class OrderMvcDto implements Serializable {

	private static final long serialVersionUID = 7298680897176913951L;

	private Long orderId;
	
	@NotNull@Min(0)@Max(9999) 
	private Long customerId;
	
	@Pattern( regexp = RegexPattern.LOCALDATE ) 
	private String orderDate;
	
}
