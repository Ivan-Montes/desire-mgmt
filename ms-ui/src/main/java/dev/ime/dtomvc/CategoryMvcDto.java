package dev.ime.dtomvc;

import java.io.Serializable;

import dev.ime.tool.RegexPattern;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Generated
public class CategoryMvcDto implements Serializable{

	private static final long serialVersionUID = 6114451105654335626L;
	
	private Long categoryId;
	
	@Pattern( regexp = RegexPattern.NAME_FULL )
	private String name;
	
	@Pattern( regexp = RegexPattern.DESCRIPTION_FULL )
	private String description;
	
}
