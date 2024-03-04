package dev.ime.mapper.impl;


import java.util.List;

import org.springframework.stereotype.Component;

import dev.ime.dto.CategoryDto;
import dev.ime.entity.Category;
import dev.ime.mapper.GenericMapper;

@Component
public class CategoryMapper implements GenericMapper<Category, CategoryDto>{

	public CategoryMapper() {
		super();
	}
	
	@Override
	public Category fromDto(CategoryDto dto) {
		
		Category c = new Category();
		c.setId(dto.categoryId());
		c.setName(dto.name());
		c.setDescription(dto.description());
		
		return c;
	}
	
	@Override
	public CategoryDto toDto(Category e) {
		return new CategoryDto(e.getId(),e.getName(),e.getDescription());
	}

	@Override
	public List<CategoryDto> toListDto(List<Category> list) {
		return list.stream()
				.map(this::toDto)
				.toList();
	}
}
