package dev.ime.mapper;


import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dev.ime.dto.ProductDto;
import dev.ime.entity.Category;
import dev.ime.entity.Product;
import dev.ime.mapper.impl.ProductMapper;

class ProductMapperTest {

	private ProductMapper productMapper;
	private List<Product>products;
	private Product proTest;
	private Long proId = 77L;
	private String proName = "Yukine";
	private Double unitPrice;
	private Integer unitInStock = 13;
	private Boolean discontinued = false;
	private Category catTest;
	private Long catId = 43L;
	private ProductDto proDto;
	
	@BeforeEach
	private void createObjects() {
		
		productMapper = new ProductMapper();
		
		products = new ArrayList<>();
		
		catTest = new Category();
		catTest.setId(catId);
		
		proTest = new Product();
		proTest.setId(proId);
		proTest.setName(proName);
		proTest.setUnitPrice(unitPrice);
		proTest.setUnitInStock(unitInStock);
		proTest.setDiscontinued(discontinued);
		proTest.setCategory(catTest);
		
		proDto = new ProductDto(proId, proName, unitPrice, unitInStock, false, catId);
	}
	
	@Test
	void ProductMapper_fromDto_ReturnProduct() {
		
		Product product = productMapper.fromDto(proDto);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(product).isNotNull(),
				()-> Assertions.assertThat(product.getId()).isEqualTo(proId),
				()-> Assertions.assertThat(product.getName()).isEqualTo(proName)
				);
	}
	
	@Test
	void ProductMapper_toListProductDto_ReturnListProductDto() {
		
		products.add(proTest);
		List<ProductDto> productDtoS = productMapper.toListDto(products);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(productDtoS).isNotNull(),
				()-> Assertions.assertThat(productDtoS).isNotEmpty(),
				()-> Assertions.assertThat(productDtoS).hasSize(1),
				()-> Assertions.assertThat(productDtoS.get(0).productId()).isEqualTo(proId),
				()-> Assertions.assertThat(productDtoS.get(0).name()).isEqualTo(proName)		
		);
		
	}
}
