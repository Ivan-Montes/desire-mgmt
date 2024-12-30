package dev.ime.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import dev.ime.dto.CategoryDto;
import dev.ime.dto.ProductDto;
import jakarta.validation.Valid;

@FeignClient( name = "ms-products")
public interface MsProductsClient {

	@GetMapping("/api/products")    
	ResponseEntity<List<ProductDto>> getAllProduct();
	
	@GetMapping("/api/products")    
	ResponseEntity<List<ProductDto>> getAllProductPaged(
			@RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer size);

	@GetMapping("/api/products/{id}")  
	ResponseEntity<ProductDto> getProductById(@PathVariable Long id);

	@PostMapping("/api/products")
	ResponseEntity<ProductDto> create(@Valid @RequestBody ProductDto dto);

	@PutMapping("/api/products/{id}")
	ResponseEntity<ProductDto> update(@PathVariable Long id, @Valid @RequestBody ProductDto dto);	

	@DeleteMapping("/api/products/{id}")
	ResponseEntity<Boolean> deleteProduct(@PathVariable Long id);
	
	@PutMapping("/api/{productId}/categories/{categoryId}")
	ResponseEntity<Boolean> changeCategory(@PathVariable Long productId, @PathVariable Long categoryId);

	
	
	@GetMapping("/api/categories")    
	ResponseEntity<List<CategoryDto>> getAllCategory();

	@GetMapping("/api/categories")    
	ResponseEntity<List<CategoryDto>> getAllCategoryPaged(
			@RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer size);
	
	@GetMapping("/api/categories/{id}")  
	ResponseEntity<CategoryDto> getCategoryById(@PathVariable Long id);

	@PostMapping("/api/categories")
	ResponseEntity<CategoryDto> create(@Valid @RequestBody CategoryDto dto);
	
	@PutMapping("/api/categories/{id}")
	ResponseEntity<CategoryDto> update(@PathVariable Long id, @Valid @RequestBody CategoryDto dto);

	@DeleteMapping("/api/categories/{id}")
	ResponseEntity<Boolean> deleteCategory(@PathVariable Long id);
	
	@PutMapping("/api/categories/{categoryId}/products/{productId}")
	ResponseEntity<Boolean> addProductToCategory(@PathVariable Long categoryId, @PathVariable Long productId);
}
