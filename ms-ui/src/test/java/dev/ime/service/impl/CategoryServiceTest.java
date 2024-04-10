package dev.ime.service.impl;


import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import dev.ime.client.impl.MsProductsClientImpl;
import dev.ime.dto.CategoryDto;
import dev.ime.dtomvc.CategoryMvcDto;
import dev.ime.exception.EntityAssociatedException;
import dev.ime.exception.ResourceNotFoundException;
import dev.ime.mapper.impl.CategoryMvcMapper;


@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {


	@Mock
	private MsProductsClientImpl msProductsClient;	

	@Mock
	private CategoryMvcMapper categoryMvcMapper;

	@InjectMocks
	private CategoryService categoryService;
	
	@Mock
	private ResponseEntity<List<CategoryDto>> responseListCategoryDto;
	private ResponseEntity<CategoryDto> responseEntityWithCategoryDto;
	private ResponseEntity<CategoryDto> responseEntityWithCategoryDtoException;
	private ResponseEntity<Boolean> responseEntityBooleanTrue;
	
	private List<CategoryDto> categoryDtoList;
	private final Long catId = 61L;
	private final String catName = "Tesoros sagrados";
	private final String catDescription = "Un Tesoro Sagrado o Shinki son espíritus que los dioses utilizan para diversas tareas y propósitos";
	private CategoryDto categoryDtoTest;
	private CategoryMvcDto categoryMvcDto;
	
	@BeforeEach
	private void createObjects() {
		
		categoryDtoList = new ArrayList<>();
		categoryDtoTest = new CategoryDto(catId, catName, catDescription);
		categoryMvcDto = new CategoryMvcDto();
		responseEntityWithCategoryDto = new ResponseEntity<CategoryDto>(categoryDtoTest, HttpStatus.OK);
		responseEntityWithCategoryDtoException = new ResponseEntity<CategoryDto>(new CategoryDto(), HttpStatus.OK);
		responseEntityBooleanTrue = new ResponseEntity<Boolean>(true, HttpStatus.OK);
	}
	
	@Test
	void CategoryService_getAll_ReturnListCategoryDto() {
		
		categoryDtoList.add(categoryDtoTest);
		Mockito.when(msProductsClient.getAllCategory()).thenReturn(responseListCategoryDto);
		Mockito.when(responseListCategoryDto.getBody()).thenReturn(categoryDtoList);
		
		List<CategoryDto>list = categoryService.getAll();
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(list).isNotNull(),
				()-> Assertions.assertThat(list).isNotEmpty(),
				()-> Assertions.assertThat(list).hasSize(1),
				()-> Assertions.assertThat(list.get(0).categoryId()).isEqualTo(catId),
				()-> Assertions.assertThat(list.get(0).name()).isEqualTo(catName)
				);
	}

	@Test
	void CategoryService_getAllPaged_ReturnListCategoryDto() {
		
		categoryDtoList.add(categoryDtoTest);
		Mockito.when(msProductsClient.getAllCategoryPaged(Mockito.anyInt(),Mockito.anyInt())).thenReturn(responseListCategoryDto);
		Mockito.when(responseListCategoryDto.getBody()).thenReturn(categoryDtoList);
		
		List<CategoryDto>list = categoryService.getAllPaged(93,28);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(list).isNotNull(),
				()-> Assertions.assertThat(list).isNotEmpty(),
				()-> Assertions.assertThat(list).hasSize(1),
				()-> Assertions.assertThat(list.get(0).categoryId()).isEqualTo(catId),
				()-> Assertions.assertThat(list.get(0).name()).isEqualTo(catName)
				);
	}

	@Test
	void CategoryService_getById_ReturnCategoryDto() {
		
		Mockito.when(msProductsClient.getCategoryById(Mockito.anyLong())).thenReturn(responseEntityWithCategoryDto);
		
		CategoryDto categoryDto = categoryService.getById(91L);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(categoryDto).isNotNull(),
				()-> Assertions.assertThat(categoryDto.categoryId()).isEqualTo(catId),
				()-> Assertions.assertThat(categoryDto.name()).isEqualTo(catName),
				()-> Assertions.assertThat(categoryDto.description()).isEqualTo(catDescription)
				);		
	}

	@Test
	void CategoryService_getById_ReturnResourceNotFoundException() {
		
		Mockito.when(msProductsClient.getCategoryById(Mockito.anyLong())).thenReturn(responseEntityWithCategoryDtoException);
		
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, () -> categoryService.getById(91L));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(ResourceNotFoundException.class)
				);
	}

	@Test
	void CategoryService_create_ReturnCategoryDto() {
		
		Mockito.when(msProductsClient.create(Mockito.any(CategoryDto.class))).thenReturn(new ResponseEntity<>(categoryDtoTest, HttpStatus.CREATED));
		Mockito.when(categoryMvcMapper.fromMvcDtoToDto(Mockito.any(CategoryMvcDto.class))).thenReturn(categoryDtoTest);
		
		CategoryDto categoryDto = categoryService.create(new CategoryMvcDto());

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(categoryDto).isNotNull(),
				()-> Assertions.assertThat(categoryDto.categoryId()).isEqualTo(catId),
				()-> Assertions.assertThat(categoryDto.name()).isEqualTo(catName),
				()-> Assertions.assertThat(categoryDto.description()).isEqualTo(catDescription)
				);		
	}

	@Test
	void CategoryService_create_ReturnCategoryDtoResourceNotFoundException() {
		
		Mockito.when(msProductsClient.create(Mockito.any(CategoryDto.class))).thenReturn(new ResponseEntity<>(new CategoryDto(), HttpStatus.CREATED));
		Mockito.when(categoryMvcMapper.fromMvcDtoToDto(Mockito.any(CategoryMvcDto.class))).thenReturn(categoryDtoTest);
		
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, () -> categoryService.create(categoryMvcDto));

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(ResourceNotFoundException.class)
				);	
	}

	@Test
	void CategoryService_update_ReturnResourceNotFoundException() {
		
		Mockito.when(msProductsClient.update(Mockito.anyLong(), Mockito.any(CategoryDto.class))).thenReturn(responseEntityWithCategoryDto);
		Mockito.when(categoryMvcMapper.fromMvcDtoToDto(Mockito.any(CategoryMvcDto.class))).thenReturn(categoryDtoTest);
		
		CategoryDto categoryDto = categoryService.update(catId, categoryMvcDto);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(categoryDto).isNotNull(),
				()-> Assertions.assertThat(categoryDto.categoryId()).isEqualTo(catId),
				()-> Assertions.assertThat(categoryDto.name()).isEqualTo(catName),
				()-> Assertions.assertThat(categoryDto.description()).isEqualTo(catDescription)				
				);
	}

	@Test
	void CategoryService_update_ReturnCategoryDto() {
		
		Mockito.when(msProductsClient.update(Mockito.anyLong(), Mockito.any(CategoryDto.class))).thenReturn(responseEntityWithCategoryDtoException);
		Mockito.when(categoryMvcMapper.fromMvcDtoToDto(Mockito.any(CategoryMvcDto.class))).thenReturn(categoryDtoTest);
				
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, () -> categoryService.update(catId, categoryMvcDto));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(ResourceNotFoundException.class)
				);
	}

	@Test
	void CategoryService_delete_ReturnBooleanTrue() {

		Mockito.when(msProductsClient.deleteCategory(Mockito.anyLong())).thenReturn(responseEntityBooleanTrue);
		
		Boolean resultValue = categoryService.delete(88l);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(resultValue).isNotNull(),
				()-> Assertions.assertThat(resultValue).isTrue()
				);
	}
	

	@Test
	void CategoryService_delete_ReturnEntityAssociatedException() {
		
		Mockito.when(msProductsClient.deleteCategory(Mockito.anyLong())).thenReturn(new ResponseEntity<>(null, HttpStatus.OK));
		
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(EntityAssociatedException.class, () -> categoryService.delete(88l));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(EntityAssociatedException.class)
				);
	}

	@Test
	void CategoryService_addProductToCategory_ReturnBooleanTrue() {

		Mockito.when(msProductsClient.addProductToCategory(Mockito.anyLong(),Mockito.anyLong())).thenReturn(responseEntityBooleanTrue);
		
		Boolean resultValue = categoryService.addProductToCategory(38l, 8l);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(resultValue).isNotNull(),
				()-> Assertions.assertThat(resultValue).isTrue()
				);
	}

	@Test
	void CategoryService_addProductToCategory_ReturnResourceNotFoundException() {
		
		Mockito.when(msProductsClient.addProductToCategory(Mockito.anyLong(),Mockito.anyLong())).thenReturn(null);
		
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, () -> categoryService.addProductToCategory(77l,21L));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(ResourceNotFoundException.class)
				);
	}
	
	
	

}
