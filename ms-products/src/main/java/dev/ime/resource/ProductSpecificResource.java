package dev.ime.resource;

import org.springframework.http.ResponseEntity;

public interface ProductSpecificResource {

	ResponseEntity<Boolean> changeCategory(Long productId, Long categoryId);

}
