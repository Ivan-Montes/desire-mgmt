package dev.ime.resource;

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
import dev.ime.mapper.CategoryMapper;
import dev.ime.service.impl.CategoryServiceImpl;
import dev.ime.tool.SomeConstants;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/categories")
public class CategoryResource implements GenericResource<CategoryDto>, CategorySpecificResource{

	private final CategoryServiceImpl categoryService;
	private final CategoryMapper categoryMapper;	
	
	public CategoryResource(CategoryServiceImpl categoryService, CategoryMapper categoryMapper) {
		this.categoryService = categoryService;
		this.categoryMapper = categoryMapper;
	}

	@GetMapping
	@Override
	public ResponseEntity<List<CategoryDto>> getAll(
			 @RequestParam( value="page", required = false) Integer page, 
			@RequestParam( value="size", required = false) Integer size) {
		
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
		
		return list.isEmpty()? new ResponseEntity<>(Collections.emptyList(), HttpStatus.NO_CONTENT)
				:new ResponseEntity<>(categoryMapper.toListCategoryDto(list), HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	@Override
	public ResponseEntity<CategoryDto> getById(@PathVariable Long id) {
		
		Optional<Category> opt = categoryService.getById(id);
		
		return opt.isPresent()? new ResponseEntity<>(categoryMapper.toCategoryDto(opt.get()),HttpStatus.OK)
				:new ResponseEntity<>(new CategoryDto(), HttpStatus.NOT_FOUND);	
	}
	
	@PostMapping
	@Override
	public ResponseEntity<CategoryDto> create(@Valid @RequestBody CategoryDto entity) {

		Optional<Category> opt = categoryService.create(entity);
		
		return opt.isPresent()? new ResponseEntity<>(categoryMapper.toCategoryDto(opt.get()),HttpStatus.CREATED)
				:new ResponseEntity<>(new CategoryDto(), HttpStatus.NOT_FOUND);	
	}
	
	@PutMapping("/{id}")
	@Override
	public ResponseEntity<CategoryDto> update(@PathVariable Long id, @Valid @RequestBody CategoryDto entity) {

		Optional<Category> opt = categoryService.update(id, entity);
		
		return opt.isPresent()? new ResponseEntity<>(categoryMapper.toCategoryDto(opt.get()),HttpStatus.OK)
				:new ResponseEntity<>(new CategoryDto(), HttpStatus.NOT_FOUND);	
	}

	@DeleteMapping("/{id}")
	@Override
	public ResponseEntity<Boolean> delete(@PathVariable Long id) {
		
		return (categoryService.delete(id) == 0)? new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK)
				:new ResponseEntity<>(Boolean.FALSE, HttpStatus.NOT_FOUND);
	}

	@PutMapping("/{categoryId}/products/{productId}")
	@Override
	public ResponseEntity<Boolean> addProductToCategory(@PathVariable Long categoryId, @PathVariable Long productId) {
		return Boolean.TRUE.equals(categoryService.addProductToCategory(categoryId, productId))? new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK)
				:new ResponseEntity<>(Boolean.FALSE, HttpStatus.NOT_FOUND);
	}
	
}
