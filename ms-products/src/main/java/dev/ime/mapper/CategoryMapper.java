package dev.ime.mapper;


import java.util.List;

import org.springframework.stereotype.Component;

import dev.ime.dto.CategoryDto;
import dev.ime.entity.Category;

@Component
public class CategoryMapper {

	public CategoryMapper() {
		super();
	}
	
	public Category fromDto(CategoryDto dto) {
		
		Category c = new Category();
		c.setId(dto.categoryId());
		c.setName(dto.name());
		c.setDescription(dto.description());
		
		return c;
	}

	public CategoryDto toCategoryDto(Category c) {
		return new CategoryDto(c.getId(),c.getName(),c.getDescription());
	}
	
	public List<CategoryDto>toListCategoryDto(List<Category>categories){
		return categories.stream()
				.map(this::toCategoryDto)
				.toList();
	}
}
