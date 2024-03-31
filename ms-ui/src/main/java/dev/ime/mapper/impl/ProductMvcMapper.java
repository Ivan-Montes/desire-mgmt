package dev.ime.mapper.impl;

import org.springframework.stereotype.Component;

import dev.ime.dto.ProductDto;
import dev.ime.dtomvc.ProductMvcDto;
import dev.ime.mapper.GenericMvcMapper;

@Component
public class ProductMvcMapper implements GenericMvcMapper<ProductMvcDto, ProductDto>{

	@Override
	public ProductDto fromMvcDtoToDto(ProductMvcDto mvcDto) {
		
		return new ProductDto(mvcDto.getProductId(), 
							  mvcDto.getName(), 
							  mvcDto.getUnitPrice(), 
							  mvcDto.getUnitInStock(), 
							  mvcDto.getDiscontinued(), 
							  mvcDto.getCategoryId());
		
	}
	
}
