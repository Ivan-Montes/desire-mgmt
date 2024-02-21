package dev.ime.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import dev.ime.dto.ProductDto;
import dev.ime.entity.Product;

@Component
public class ProductMapper {
	
	public ProductMapper() {
		super();
	}

	public Product fromDto(ProductDto dto) {
		
		Product p = new Product();
		p.setId(dto.productId());
		p.setName(dto.name());
		p.setUnitInStock(dto.unitInStock());
		p.setUnitPrice(dto.unitPrice());
		p.setDiscontinued(dto.discontinued());
		p.setCategory(null);
		
		return p;
	}
	
	public ProductDto toProductDto(Product p) {
		return new ProductDto(p.getId(),p.getName(),p.getUnitPrice(),p.getUnitInStock(),p.getDiscontinued(), p.getCategory().getId());
	}
	
	public List<ProductDto>toListProductDto(List<Product>products){
		return products.stream()
				.map(this::toProductDto)
				.toList();
	}
}
