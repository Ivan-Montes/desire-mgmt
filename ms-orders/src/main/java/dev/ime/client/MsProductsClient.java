package dev.ime.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import dev.ime.dto.ProductDto;

@FeignClient( name = "ms-products", url = "${feign.ms-products.url}")
public interface MsProductsClient {

	@GetMapping("/api/products/{id}")
	public ResponseEntity<ProductDto> getProductById(@PathVariable Long id);
}
