package dev.ime.service.impl;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import dev.ime.dto.CategoryDto;
import dev.ime.entity.Category;
import dev.ime.entity.Product;
import dev.ime.exception.AttributeUniqueException;
import dev.ime.exception.EntityAssociatedException;
import dev.ime.exception.ResourceNotFoundException;
import dev.ime.mapper.impl.CategoryMapper;
import dev.ime.repository.CategoryRepository;
import dev.ime.repository.ProductRepository;


@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

	@Mock
	private CategoryRepository categoryRepo;
	@Mock
	private CategoryMapper categoryMapper;
	@Mock
	private ProductRepository productRepo;
	@InjectMocks
	private CategoryServiceImpl categoryService;
	
	private List<Category>categories;
	private Category catTest;
	private Category catTest2;
	private final Long catId = 1L;
	private final Long catId2 = 33L;
	private final String catName = "Tesoros sagrados";
	private final String catDescription = "Un Tesoro Sagrado o Shinki son espíritus que los dioses utilizan para diversas tareas y propósitos";
	private CategoryDto categoryDto;
	
	@BeforeEach
	private void createObjects() {
		
		categories = new ArrayList<>();
		
		catTest = new Category();
		catTest.setId(catId);
		catTest.setName(catName);
		catTest.setDescription(catDescription);
		
		categoryDto = new CategoryMapper().toDto(catTest);
		
		catTest2 = new Category();
		catTest2.setId(catId2);
		
	}
	
	@Test
	void CategoryServiceImpl_getAll_ReturnListCategory() {
		
		categories.add(catTest);
		Mockito.when(categoryRepo.findAll(Mockito.any(Sort.class))).thenReturn(categories);
		
		List<Category>list = categoryService.getAll();
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(list).isNotNull(),
				()-> Assertions.assertThat(list).isNotEmpty(),
				()-> Assertions.assertThat(list).hasSize(1),
				()-> Assertions.assertThat(list.get(0).getId()).isEqualTo(catId)
				);
		Mockito.verify(categoryRepo, Mockito.times(1)).findAll(Mockito.any(Sort.class));
	}

	@Test	
	void CategoryServiceImpl_getAllPaged_ReturnListCategory() { 
		
		@SuppressWarnings("unchecked")
		Page<Category> page = Mockito.mock(Page.class);
		PageRequest pageRequest = PageRequest.of(1, 1);
		categories.add(catTest);
		
		Mockito.when(categoryRepo.findAll(pageRequest)).thenReturn(page);		
		Mockito.when(page.toList()).thenReturn(categories);
		
		List<Category>list = categoryService.getAllPaged(1, 1);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(list).isNotNull(),
				()-> Assertions.assertThat(list).isNotEmpty(),
				()-> Assertions.assertThat(list.get(0).getId()).isEqualTo(catId),
				()-> Assertions.assertThat(list.get(0).getName()).isEqualTo(catName)
				);
		Mockito.verify(categoryRepo, Mockito.times(1)).findAll(Mockito.any(PageRequest.class));
	}
	
	@Test
	void CategoryServiceImpl_getById_ReturnOptCategory() {
		
		Mockito.when(categoryRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(catTest));
		
		Optional<Category>optCat = categoryService.getById(catId);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optCat).isNotNull(),
				()-> Assertions.assertThat(optCat).isNotEmpty(),
				()-> Assertions.assertThat(optCat.get().getId()).isEqualTo(catId),
				()-> Assertions.assertThat(optCat.get().getName()).isEqualTo(catName)
				);
		Mockito.verify(categoryRepo, Mockito.times(1)).findById(Mockito.anyLong());
	}
	

	@Test
	void CategoryServiceImpl_getById_ReturnException() {
		
		Mockito.when(categoryRepo.findById(Mockito.anyLong())).thenReturn(Optional.empty());
		
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, ()-> categoryService.getById(catId));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(ResourceNotFoundException.class)
				);
		Mockito.verify(categoryRepo, Mockito.times(1)).findById(Mockito.anyLong());
	}
	
	@Test
	void CategoryServiceImpl_create_ReturnException() {

		categories.add(catTest);
		Mockito.when(categoryRepo.findByName(Mockito.anyString())).thenReturn(categories);		
		
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(AttributeUniqueException.class, ()-> categoryService.create(categoryDto));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(AttributeUniqueException.class)
				);
		Mockito.verify(categoryRepo, Mockito.times(1)).findByName(Mockito.anyString());
	}

	@Test
	void CategoryServiceImpl_create_ReturnOptCategory() {

		Mockito.when(categoryRepo.findByName(Mockito.anyString())).thenReturn(categories);
		Mockito.when(categoryMapper.fromDto(Mockito.any(CategoryDto.class))).thenReturn(catTest);
		Mockito.when(categoryRepo.save(Mockito.any(Category.class))).thenReturn(catTest);
		
		Optional<Category>optCat = categoryService.create(categoryDto);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optCat).isNotNull(),
				()-> Assertions.assertThat(optCat).isNotEmpty(),
				()-> Assertions.assertThat(optCat.get().getId()).isEqualTo(catId),
				()-> Assertions.assertThat(optCat.get().getName()).isEqualTo(catName)
				);
		Mockito.verify(categoryRepo, Mockito.times(1)).findByName(Mockito.anyString());
		Mockito.verify(categoryMapper, Mockito.times(1)).fromDto(Mockito.any(CategoryDto.class));
		Mockito.verify(categoryRepo, Mockito.times(1)).save(Mockito.any(Category.class));
	}
	
	@Test
	void CategoryServiceImpl_update_ReturnResourceNotFoundException() {
		
		Mockito.when(categoryRepo.findById(Mockito.anyLong())).thenReturn(Optional.empty());
		
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, ()-> categoryService.update(catId, categoryDto));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(ResourceNotFoundException.class)
				);
		Mockito.verify(categoryRepo, Mockito.times(1)).findById(Mockito.anyLong());
	}
	
	@Test
	void CategoryServiceImpl_update_ReturnOptCategory() {
	
		Mockito.when(categoryRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(catTest));
		Mockito.when(categoryRepo.findByName(Mockito.anyString())).thenReturn(categories);
		Mockito.when(categoryRepo.save(Mockito.any(Category.class))).thenReturn(catTest);

		Optional<Category>optCat = categoryService.update(catId, categoryDto);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optCat).isNotNull(),
				()-> Assertions.assertThat(optCat).isNotEmpty(),
				()-> Assertions.assertThat(optCat.get().getId()).isEqualTo(catId),
				()-> Assertions.assertThat(optCat.get().getName()).isEqualTo(catName)
				);
		Mockito.verify(categoryRepo, Mockito.times(1)).findById(Mockito.anyLong());
		Mockito.verify(categoryRepo, Mockito.times(1)).findByName(Mockito.anyString());
		Mockito.verify(categoryRepo, Mockito.times(1)).save(Mockito.any(Category.class));
	}
	

	@Test
	void CategoryServiceImpl_update_ReturnAttributeUniqueException() {
		
		categories.add(catTest2);
		Mockito.when(categoryRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(catTest));
		Mockito.when(categoryRepo.findByName(Mockito.anyString())).thenReturn(categories);

		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(AttributeUniqueException.class, ()-> categoryService.update(catId, categoryDto));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(AttributeUniqueException.class)
				);
		Mockito.verify(categoryRepo, Mockito.times(1)).findById(Mockito.anyLong());
		Mockito.verify(categoryRepo, Mockito.times(1)).findByName(Mockito.anyString());
	}
	

	@Test
	void CategoryServiceImpl_delete_ReturnResourceNotFoundException(){
		
		Mockito.when(categoryRepo.findById(Mockito.anyLong())).thenReturn(Optional.empty());
		
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, ()-> categoryService.delete(catId));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(ResourceNotFoundException.class)
				);
		Mockito.verify(categoryRepo, Mockito.times(1)).findById(Mockito.anyLong());
	}
	

	@Test
	void CategoryServiceImpl_delete_ReturnIntOk(){
	
		Mockito.doReturn(Optional.of(catTest)).doReturn(Optional.empty()).when(categoryRepo).findById(catId);
		Mockito.doNothing().when(categoryRepo).deleteById(Mockito.anyLong());
		
		Integer resultValue = categoryService.delete(catId);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(resultValue).isNotNull(),
				()-> Assertions.assertThat(resultValue).isZero()
				);		
		Mockito.verify(categoryRepo, Mockito.times(2)).findById(Mockito.anyLong());
		Mockito.verify(categoryRepo, Mockito.times(1)).deleteById(Mockito.anyLong());
	}
	

	@Test
	void CategoryServiceImpl_delete_ReturnIntFail(){
	
		Mockito.when(categoryRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(catTest));
		Mockito.doNothing().when(categoryRepo).deleteById(Mockito.anyLong());
		
		Integer resultValue = categoryService.delete(catId);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(resultValue).isNotNull(),
				()-> Assertions.assertThat(resultValue).isEqualTo(1)
				);		
		Mockito.verify(categoryRepo, Mockito.times(2)).findById(Mockito.anyLong());
		Mockito.verify(categoryRepo, Mockito.times(1)).deleteById(Mockito.anyLong());
	}
	
	@Test
	void CategoryServiceImpl_delete_ReturnEntityAssociatedException(){
		
		catTest.getProducts().add(new Product());
		Mockito.when(categoryRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(catTest));
		
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(EntityAssociatedException.class, ()-> categoryService.delete(catId));		
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(EntityAssociatedException.class)
				);
		Mockito.verify(categoryRepo, Mockito.times(1)).findById(Mockito.anyLong());
	}

	@Test
	void CategoryServiceImpl_addProductToCategory_ReturnResourceNotFoundExceptionInProduct() {
		
		Mockito.when(productRepo.findById(Mockito.anyLong())).thenReturn(Optional.empty());
		
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, ()-> categoryService.addProductToCategory(catId, catId2));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(ResourceNotFoundException.class)
				);
		Mockito.verify(productRepo, Mockito.times(1)).findById(Mockito.anyLong());
	}
	
	@Test
	void CategoryServiceImpl_addProductToCategory_ReturnResourceNotFoundExceptionInCategory() {
		
		Mockito.when(productRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(new Product()));
		Mockito.when(categoryRepo.findById(Mockito.anyLong())).thenReturn(Optional.empty());
		
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, ()-> categoryService.addProductToCategory(catId, catId2));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(ResourceNotFoundException.class)
				);
		Mockito.verify(productRepo, Mockito.times(1)).findById(Mockito.anyLong());
		Mockito.verify(categoryRepo, Mockito.times(1)).findById(Mockito.anyLong());
	}
	
	@Test
	void CategoryServiceImpl_addProductToCategory_ReturnBooleanTrue() {
		
		Mockito.when(productRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(new Product()));
		Mockito.when(categoryRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(catTest));
		Mockito.when(categoryRepo.save(Mockito.any(Category.class))).thenReturn(catTest);
		
		Boolean resultBool = categoryService.addProductToCategory(catId, catId2);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(resultBool).isNotNull(),
				()-> Assertions.assertThat(resultBool).isTrue()
				);	
		Mockito.verify(productRepo, Mockito.times(1)).findById(Mockito.anyLong());
		Mockito.verify(categoryRepo, Mockito.times(1)).findById(Mockito.anyLong());
		Mockito.verify(categoryRepo, Mockito.times(1)).save(Mockito.any(Category.class));
	}

	@Test
	void CategoryServiceImpl_addProductToCategory_ReturnBooleanFalse() {
		
		Mockito.when(productRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(new Product()));
		Mockito.when(categoryRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(catTest));
		Mockito.when(categoryRepo.save(Mockito.any(Category.class))).thenReturn(null);
		
		Boolean resultBool = categoryService.addProductToCategory(catId, catId2);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(resultBool).isNotNull(),
				()-> Assertions.assertThat(resultBool).isFalse()
				);	
		Mockito.verify(productRepo, Mockito.times(1)).findById(Mockito.anyLong());
		Mockito.verify(categoryRepo, Mockito.times(1)).findById(Mockito.anyLong());
		Mockito.verify(categoryRepo, Mockito.times(1)).save(Mockito.any(Category.class));
	}
	
}
