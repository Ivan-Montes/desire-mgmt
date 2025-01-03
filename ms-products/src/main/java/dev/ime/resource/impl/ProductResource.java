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

import dev.ime.dto.ProductDto;
import dev.ime.entity.Product;
import dev.ime.mapper.impl.ProductMapper;
import dev.ime.resource.GenericResource;
import dev.ime.resource.ProductSpecificResource;
import dev.ime.service.impl.ProductServiceImpl;
import dev.ime.tool.SomeConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/products")
@Tag(name = "Product", description="Product Operations")
public class ProductResource implements GenericResource<ProductDto>, ProductSpecificResource{

	private final ProductServiceImpl productService;
	private final ProductMapper productMapper;

	public ProductResource(ProductServiceImpl productService, ProductMapper productMapper) {
		this.productService = productService;
		this.productMapper = productMapper;
	}
	
	@GetMapping
	@Override
	@Operation(summary="Get a List of all products", description="Get a List of all products, @return an object Response with a List of DTO's")
	public ResponseEntity<List<ProductDto>> getAll(
			@RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer size) {
		
		List<Product>list;
		
		if ( ( page != null && page >= 0 ) && ( size != null && size >= 1 ) ) {
			list = productService.getAllPaged(page, size);
		}
		else if ( page != null && page >= 0 ) {
			list = productService.getAllPaged(page, SomeConstants.SIZE_REQUEST);
		}
		else  {
			list = productService.getAll();			
		}
		
		return list.isEmpty()? new ResponseEntity<>(Collections.emptyList(),HttpStatus.OK)
				:new ResponseEntity<>(productMapper.toListDto(list), HttpStatus.OK);
	}


	@GetMapping("/{id}")
	@Override
	@Operation(summary="Get a Product according to an Id", description="Get a Product according to an Id, @return an object Response with the entity required in a DTO")
	public ResponseEntity<ProductDto> getById(@PathVariable Long id) {
		
		Optional<Product> opt =  productService.getById(id);
		
		return opt.isPresent()? new ResponseEntity<>(productMapper.toDto(opt.get()),HttpStatus.OK)
				:new ResponseEntity<>(new ProductDto(), HttpStatus.OK);		
	}

	@PostMapping
	@Override
	@Operation(summary="Create a new Product", description="Create a new Product, @return an object Response with the entity in a DTO")
	public ResponseEntity<ProductDto> create(@Valid @RequestBody ProductDto entity) {
		
		Optional<Product> opt =  productService.create(entity);
		
		return opt.isPresent()? new ResponseEntity<>(productMapper.toDto(opt.get()),HttpStatus.CREATED)
				:new ResponseEntity<>(new ProductDto(), HttpStatus.OK);	
	}

	@PutMapping("/{id}")
	@Override
	@Operation(summary="Update fields in a Product", description="Update fields in a Product, @return an object Response with the entity modified in a DTO")
	public ResponseEntity<ProductDto> update(@PathVariable Long id, @Valid @RequestBody ProductDto entity) {
		
		Optional<Product> opt =  productService.update(id, entity);
		
		return opt.isPresent()? new ResponseEntity<>(productMapper.toDto(opt.get()),HttpStatus.OK)
				:new ResponseEntity<>(new ProductDto(), HttpStatus.OK);	
	}

	@DeleteMapping("/{id}")
	@Override
	@Operation(summary="Delete a Product by its Id", description="Product a Category by its Id, @return an object Response with a message")
	public ResponseEntity<Boolean> delete(@PathVariable Long id) {		
		
		return (productService.delete(id) == 0)? new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK)
				:new ResponseEntity<>(Boolean.FALSE, HttpStatus.OK);
	}


	@PutMapping("/{productId}/categories/{categoryId}")
	@Override
	@Operation(summary="Change Product of Category", description="Change Category of a Product, @return an object Response with a message")
	public ResponseEntity<Boolean> changeCategory(@PathVariable Long productId, @PathVariable Long categoryId) {

		return Boolean.TRUE.equals(productService.changeCategory(productId, categoryId))? new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK)
				:new ResponseEntity<>(Boolean.FALSE, HttpStatus.OK);
	}
	
}
