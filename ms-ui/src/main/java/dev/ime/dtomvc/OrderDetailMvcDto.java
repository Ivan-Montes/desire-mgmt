package dev.ime.dtomvc;

import java.io.Serializable;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Generated
public class OrderDetailMvcDto implements Serializable{
	
	private static final long serialVersionUID = 6380411879839004303L;

	private Long orderDetailId;
	
	@NotNull@Min(0)@Max(9999) 
	private Integer quantity;
	
	@NotNull@Min(0)@Max(9999) 	
	private Double discount;
	
	@NotNull@Min(0)@Max(9999) 
	private Long productId;
	
	@NotNull@Min(0)@Max(9999) 
	private Long orderId;
	
}
