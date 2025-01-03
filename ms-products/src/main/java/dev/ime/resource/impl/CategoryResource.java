package dev.ime.resource.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.ime.dto.CategoryDto;
import dev.ime.entity.Category;
import dev.ime.mapper.impl.CategoryMapper;
import dev.ime.resource.CategorySpecificResource;
import dev.ime.resource.GenericResource;
import dev.ime.service.impl.CategoryServiceImpl;
import dev.ime.tool.SomeConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "Category", description="Category Operations")
public class CategoryResource implements GenericResource<CategoryDto>, CategorySpecificResource{

	private final CategoryServiceImpl categoryService;
	private final CategoryMapper categoryMapper;	
	
	public CategoryResource(CategoryServiceImpl categoryService, CategoryMapper categoryMapper) {
		this.categoryService = categoryService;
		this.categoryMapper = categoryMapper;
	}

	@GetMapping
	@Override
	@Operation(summary="Get a List of all categories", description="Get a List of all categories, @return an object Response with a List of DTO's")
	public ResponseEntity<List<CategoryDto>> getAll(
			@RequestParam(required = false) Integer page, 
			@RequestParam(required = false) Integer size) {
		
		List<Category>list;
		
		if ( ( page != null && page >= 0 ) && ( size != null && size >= 1 ) ) {
			list = categoryService.getAllPaged(page, size);
		}
		else if ( page != null && page >= 0 ) {
			list = categoryService.getAllPaged(page, SomeConstants.SIZE_REQUEST);
		}
		else  {
			list = categoryService.getAll();			
		}	
		
		return list.isEmpty()? new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK)
				:new ResponseEntity<>(categoryMapper.toListDto(list), HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	@Override
	@Operation(summary="Get a Category according to an Id", description="Get a Category according to an Id, @return an object Response with the entity required in a DTO")
	public ResponseEntity<CategoryDto> getById(@PathVariable Long id) {
		
		Optional<Category> opt = categoryService.getById(id);
		
		return opt.isPresent()? new ResponseEntity<>(categoryMapper.toDto(opt.get()),HttpStatus.OK)
				:new ResponseEntity<>(new CategoryDto(), HttpStatus.OK);	
	}
	
	@PostMapping
	@Override
	@Operation(summary="Create a new Category", description="Create a new Category, @return an object Response with the entity in a DTO")
	public ResponseEntity<CategoryDto> create(@Valid @RequestBody CategoryDto entity) {

		Optional<Category> opt = categoryService.create(entity);
		
		return opt.isPresent()? new ResponseEntity<>(categoryMapper.toDto(opt.get()),HttpStatus.CREATED)
				:new ResponseEntity<>(new CategoryDto(), HttpStatus.OK);	
	}
	
	@PutMapping("/{id}")
	@Override
	@Operation(summary="Update fields in a Category", description="Update fields in a Category, @return an object Response with the entity modified in a DTO")
	public ResponseEntity<CategoryDto> update(@PathVariable Long id, @Valid @RequestBody CategoryDto entity) {

		Optional<Category> opt = categoryService.update(id, entity);
		
		return opt.isPresent()? new ResponseEntity<>(categoryMapper.toDto(opt.get()),HttpStatus.OK)
				:new ResponseEntity<>(new CategoryDto(), HttpStatus.OK);	
	}

	@DeleteMapping("/{id}")
	@Override
	@Operation(summary="Delete a Category by its Id", description="Delete a Category by its Id, @return an object Response with a message")
	public ResponseEntity<Boolean> delete(@PathVariable Long id) {
		
		return (categoryService.delete(id) == 0)? new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK)
				:new ResponseEntity<>(Boolean.FALSE, HttpStatus.OK);
	}

	@PutMapping("/{categoryId}/products/{productId}")
	@Override
	@Operation(summary="Add a Category in a Product", description="Add a Product in a Category, @return an object Response with a message")
	public ResponseEntity<Boolean> addProductToCategory(@PathVariable Long categoryId, @PathVariable Long productId) {
		return Boolean.TRUE.equals(categoryService.addProductToCategory(categoryId, productId))? new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK)
				:new ResponseEntity<>(Boolean.FALSE, HttpStatus.OK);
	}
	
}
