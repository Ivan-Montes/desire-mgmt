package dev.ime.mapper.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import dev.ime.dto.ProductDto;
import dev.ime.entity.Product;
import dev.ime.mapper.GenericMapper;

@Component
public class ProductMapper implements GenericMapper<Product, ProductDto>{
	
	public ProductMapper() {
		super();
	}

	@Override
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

	@Override
	public ProductDto toDto(Product p) {
		return new ProductDto(p.getId(),p.getName(),p.getUnitPrice(),p.getUnitInStock(),p.getDiscontinued(), p.getCategory().getId());
	}

	@Override
	public List<ProductDto>toListDto(List<Product>products){
		return products.stream()
				.map(this::toDto)
				.toList();
	}
}
