package dev.ime.mapper.impl;

import org.springframework.stereotype.Component;

import dev.ime.dto.CategoryDto;
import dev.ime.dtomvc.CategoryMvcDto;
import dev.ime.mapper.GenericMvcMapper;

@Component
public class CategoryMvcMapper implements GenericMvcMapper<CategoryMvcDto, CategoryDto>{

	@Override
	public CategoryDto fromMvcDtoToDto(CategoryMvcDto mvcDto) {		
		
		return new CategoryDto(mvcDto.getCategoryId(), 
							   mvcDto.getName(), 
							   mvcDto.getDescription());
		
	}

}
