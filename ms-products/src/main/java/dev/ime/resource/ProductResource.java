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

import dev.ime.dto.ProductDto;
import dev.ime.entity.Product;
import dev.ime.mapper.ProductMapper;
import dev.ime.service.impl.ProductServiceImpl;
import dev.ime.tool.SomeConstants;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/products")
public class ProductResource implements GenericResource<ProductDto>, ProductSpecificResource{

	private final ProductServiceImpl productService;
	private final ProductMapper productMapper;

	public ProductResource(ProductServiceImpl productService, ProductMapper productMapper) {
		this.productService = productService;
		this.productMapper = productMapper;
	}
	
	@GetMapping
	@Override
	public ResponseEntity<List<ProductDto>> getAll(
			@RequestParam( value="page", required = false) Integer page,
			@RequestParam( value="size", required = false) Integer size) {
		
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
		
		return list.isEmpty()? new ResponseEntity<>(Collections.emptyList(),HttpStatus.NO_CONTENT)
				:new ResponseEntity<>(productMapper.toListProductDto(list), HttpStatus.OK);
	}


	@GetMapping("/{id}")
	@Override
	public ResponseEntity<ProductDto> getById(@PathVariable Long id) {
		
		Optional<Product> opt =  productService.getById(id);
		
		return opt.isPresent()? new ResponseEntity<>(productMapper.toProductDto(opt.get()),HttpStatus.OK)
				:new ResponseEntity<>(new ProductDto(), HttpStatus.NOT_FOUND);		
	}

	@PostMapping
	@Override
	public ResponseEntity<ProductDto> create(@Valid @RequestBody ProductDto entity) {
		
		Optional<Product> opt =  productService.create(entity);
		
		return opt.isPresent()? new ResponseEntity<>(productMapper.toProductDto(opt.get()),HttpStatus.CREATED)
				:new ResponseEntity<>(new ProductDto(), HttpStatus.NOT_FOUND);	
	}

	@PutMapping("/{id}")
	@Override
	public ResponseEntity<ProductDto> update(@PathVariable Long id, @Valid @RequestBody ProductDto entity) {
		
		Optional<Product> opt =  productService.update(id, entity);
		
		return opt.isPresent()? new ResponseEntity<>(productMapper.toProductDto(opt.get()),HttpStatus.OK)
				:new ResponseEntity<>(new ProductDto(), HttpStatus.NOT_FOUND);	
	}

	@DeleteMapping("/{id}")
	@Override
	public ResponseEntity<Boolean> delete(@PathVariable Long id) {		
		
		return (productService.delete(id) == 0)? new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK)
				:new ResponseEntity<>(Boolean.FALSE, HttpStatus.NOT_FOUND);
	}


	@PutMapping("/{productId}/categories/{categoryId}")
	@Override
	public ResponseEntity<Boolean> changeCategory(@PathVariable Long productId, @PathVariable Long categoryId) {

		return Boolean.TRUE.equals(productService.changeCategory(productId, categoryId))? new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK)
				:new ResponseEntity<>(Boolean.FALSE, HttpStatus.NOT_FOUND);
	}
	
}
