package dev.ime.mapper;


import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dev.ime.dto.CategoryDto;
import dev.ime.entity.Category;

class CategoryMapperTest {
	
	private CategoryMapper categoryMapper;
	private List<Category>categories;
	private Category catTest;
	private final Long catId = 77L;
	private final String catName = "Tesoros sagrados";
	private final String catDescription = "Un Tesoro Sagrado o Shinki son espíritus que los dioses utilizan para diversas tareas y propósitos";
	private CategoryDto catDto;
	
	@BeforeEach
	private void createObjects() {
		
		categoryMapper = new CategoryMapper();
		
		categories = new ArrayList<>();
		
		catTest = new Category();
		catTest.setId(catId);
		catTest.setName(catName);
		catTest.setDescription(catDescription);
		
		catDto = new CategoryDto(catId, catName, catDescription);
	}
	
	
	@Test
	void CategoryMapper_fromDto_ReturnCategory() {
		
		Category category = categoryMapper.fromDto(catDto);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(category).isNotNull(),
				()-> Assertions.assertThat(category.getId()).isEqualTo(catId),
				()-> Assertions.assertThat(category.getName()).isEqualTo(catName)
				);
	}
	
	@Test
	void CategoryMapper_toListCategoryDto_ReturnListCategoryDto() {
		
		categories.add(catTest);
		List<CategoryDto> categoryDtoS = categoryMapper.toListCategoryDto(categories);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(categoryDtoS).isNotNull(),
				()-> Assertions.assertThat(categoryDtoS).isNotEmpty(),
				()-> Assertions.assertThat(categoryDtoS).hasSize(1),
				()-> Assertions.assertThat(categoryDtoS.get(0).categoryId()).isEqualTo(catId),
				()-> Assertions.assertThat(categoryDtoS.get(0).name()).isEqualTo(catName)		
		);
		
	}

}
