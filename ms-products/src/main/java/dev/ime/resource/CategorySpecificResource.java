package dev.ime.resource;

import org.springframework.http.ResponseEntity;

public interface CategorySpecificResource {

	ResponseEntity<Boolean> addProductToCategory(Long categoryId, Long productId);
}
