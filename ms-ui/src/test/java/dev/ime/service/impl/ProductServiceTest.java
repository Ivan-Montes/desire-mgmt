package dev.ime.service.impl;


import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import dev.ime.client.impl.MsProductsClientImpl;
import dev.ime.dto.ProductDto;
import dev.ime.dtomvc.ProductMvcDto;
import dev.ime.exception.EntityAssociatedException;
import dev.ime.exception.ResourceNotFoundException;
import dev.ime.mapper.impl.ProductMvcMapper;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

	@Mock
	private MsProductsClientImpl msProductsClient;
	
	@Mock	
	private ProductMvcMapper productMvcMapper;
	
	@InjectMocks
	private ProductService productService;
	
	private List<ProductDto>productList;
	private ProductDto productDtoTest;
	private ProductMvcDto productMvcDtoVoid;
	
	private Long productId = 77L;
	private String productName = "Yukine";
	private Double unitPrice;
	private Integer unitInStock = 13;
	private Boolean discontinued = false;
	private Long categoryId = 63L;
	
	@Mock
	private ResponseEntity<List<ProductDto>> responseListProductDto;
	private ResponseEntity<ProductDto> responseEntityWithProductDto;
	private ResponseEntity<ProductDto> responseEntityWithProductDtoException;
	private ResponseEntity<Boolean> responseEntityBooleanTrue;
	
	
	@BeforeEach
	void createObjects() {
		
		productList = new ArrayList<>();
		productDtoTest = new ProductDto(productId,productName,unitPrice,unitInStock,discontinued,categoryId);
		productMvcDtoVoid = new ProductMvcDto();
		
		responseEntityWithProductDto = new ResponseEntity<>(productDtoTest, HttpStatus.OK);
		responseEntityWithProductDtoException = new ResponseEntity<>(new ProductDto(), HttpStatus.OK);
		responseEntityBooleanTrue = new ResponseEntity<>(true, HttpStatus.OK);
		
	}
	
	@Test
	void  ProductService_getAll_ReturnListProductDto() {
		
		productList.add(productDtoTest);
		Mockito.when(msProductsClient.getAllProduct()).thenReturn(responseListProductDto);
		Mockito.when(responseListProductDto.getBody()).thenReturn(productList);
		
		List<ProductDto> list = productService.getAll();
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(list).isNotNull(),
				()-> Assertions.assertThat(list).isNotEmpty(),
				()-> Assertions.assertThat(list).hasSize(1),
				()-> Assertions.assertThat(list.get(0).productId()).isEqualTo(productId),
				()-> Assertions.assertThat(list.get(0).name()).isEqualTo(productName),
				()-> Assertions.assertThat(list.get(0).unitPrice()).isEqualTo(unitPrice),
				()-> Assertions.assertThat(list.get(0).unitInStock()).isEqualTo(unitInStock),
				()-> Assertions.assertThat(list.get(0).discontinued()).isEqualTo(discontinued),
				()-> Assertions.assertThat(list.get(0).categoryId()).isEqualTo(categoryId)
				);		
	}

	@Test
	void  ProductService_getAllPaged_ReturnListProductDto() {
		
		productList.add(productDtoTest);
		Mockito.when(msProductsClient.getAllProductPaged(Mockito.anyInt(), Mockito.anyInt())).thenReturn(responseListProductDto);
		Mockito.when(responseListProductDto.getBody()).thenReturn(productList);
		
		List<ProductDto> list = productService.getAllPaged(67,31);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(list).isNotNull(),
				()-> Assertions.assertThat(list).isNotEmpty(),
				()-> Assertions.assertThat(list).hasSize(1),
				()-> Assertions.assertThat(list.get(0).productId()).isEqualTo(productId),
				()-> Assertions.assertThat(list.get(0).name()).isEqualTo(productName),
				()-> Assertions.assertThat(list.get(0).unitPrice()).isEqualTo(unitPrice),
				()-> Assertions.assertThat(list.get(0).unitInStock()).isEqualTo(unitInStock),
				()-> Assertions.assertThat(list.get(0).discontinued()).isEqualTo(discontinued),
				()-> Assertions.assertThat(list.get(0).categoryId()).isEqualTo(categoryId)
				);		
	}

	@Test
	void  ProductService_getById_ReturnProductDto() {
		
		Mockito.when(msProductsClient.getProductById(Mockito.anyLong())).thenReturn(responseEntityWithProductDto);
		
		ProductDto productDto = productService.getById(81l);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(productDto).isNotNull(),
				()-> Assertions.assertThat(productDto.productId()).isEqualTo(productId),
				()-> Assertions.assertThat(productDto.name()).isEqualTo(productName),
				()-> Assertions.assertThat(productDto.unitPrice()).isEqualTo(unitPrice),
				()-> Assertions.assertThat(productDto.unitInStock()).isEqualTo(unitInStock),
				()-> Assertions.assertThat(productDto.discontinued()).isEqualTo(discontinued),
				()-> Assertions.assertThat(productDto.categoryId()).isEqualTo(categoryId)
				);
	}

	@Test
	void  ProductService_getById_ReturnResourceNotFoundException() {
		
		Mockito.when(msProductsClient.getProductById(Mockito.anyLong())).thenReturn(responseEntityWithProductDtoException);
		
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, ()-> productService.getById(81l));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(ResourceNotFoundException.class)
				);
	}

	@Test
	void  ProductService_create_ReturnProductDto() {
		
		Mockito.when(msProductsClient.create(Mockito.any(ProductDto.class))).thenReturn(new ResponseEntity<>(productDtoTest, HttpStatus.CREATED));
		Mockito.when(productMvcMapper.fromMvcDtoToDto(Mockito.any(ProductMvcDto.class))).thenReturn(productDtoTest);
		
		ProductDto productDto = productService.create(productMvcDtoVoid);

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(productDto).isNotNull(),
				()-> Assertions.assertThat(productDto.productId()).isEqualTo(productId),
				()-> Assertions.assertThat(productDto.name()).isEqualTo(productName),
				()-> Assertions.assertThat(productDto.unitPrice()).isEqualTo(unitPrice),
				()-> Assertions.assertThat(productDto.unitInStock()).isEqualTo(unitInStock),
				()-> Assertions.assertThat(productDto.discontinued()).isEqualTo(discontinued),
				()-> Assertions.assertThat(productDto.categoryId()).isEqualTo(categoryId)
				);
	}

	@Test
	void  ProductService_create_ReturnResourceNotFoundException() {

		Mockito.when(msProductsClient.create(Mockito.any(ProductDto.class))).thenReturn(new ResponseEntity<>(new ProductDto(), HttpStatus.CREATED));
		Mockito.when(productMvcMapper.fromMvcDtoToDto(Mockito.any(ProductMvcDto.class))).thenReturn(productDtoTest);

		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, ()-> productService.create(productMvcDtoVoid));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(ResourceNotFoundException.class)
				);
	}

	@Test
	void  ProductService_update_ReturnProductDto() {
		
		Mockito.when(msProductsClient.update(Mockito.anyLong(), Mockito.any(ProductDto.class))).thenReturn(responseEntityWithProductDto);
		Mockito.when(productMvcMapper.fromMvcDtoToDto(Mockito.any(ProductMvcDto.class))).thenReturn(productDtoTest);
		
		ProductDto productDto = productService.update(productId, new ProductMvcDto());
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(productDto).isNotNull(),
				()-> Assertions.assertThat(productDto.productId()).isEqualTo(productId),
				()-> Assertions.assertThat(productDto.name()).isEqualTo(productName),
				()-> Assertions.assertThat(productDto.unitPrice()).isEqualTo(unitPrice),
				()-> Assertions.assertThat(productDto.unitInStock()).isEqualTo(unitInStock),
				()-> Assertions.assertThat(productDto.discontinued()).isEqualTo(discontinued),
				()-> Assertions.assertThat(productDto.categoryId()).isEqualTo(categoryId)
				);
	}

	@Test
	void  ProductService_update_ReturnResourceNotFoundException() {
		
		Mockito.when(msProductsClient.update(Mockito.anyLong(), Mockito.any(ProductDto.class))).thenReturn(responseEntityWithProductDtoException);
		Mockito.when(productMvcMapper.fromMvcDtoToDto(Mockito.any(ProductMvcDto.class))).thenReturn(productDtoTest);

		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, ()-> productService.update(productId, productMvcDtoVoid));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(ResourceNotFoundException.class)
				);
	}

	@Test
	void  ProductService_delete_ReturnBooleanTrue() {
		
		Mockito.when(msProductsClient.deleteProduct(Mockito.anyLong())).thenReturn(responseEntityBooleanTrue);
		
		Boolean resultValue = productService.delete(06L);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(resultValue).isNotNull(),
				()-> Assertions.assertThat(resultValue).isTrue()
				);
	}

	@Test
	void  ProductService_delete_ReturnEntityAssociatedException() {
		
		Mockito.when(msProductsClient.deleteProduct(Mockito.anyLong())).thenReturn(new ResponseEntity<>(null, HttpStatus.OK));
		
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(EntityAssociatedException.class, ()-> productService.delete(06L));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(EntityAssociatedException.class)
				);		
	}

	@Test
	void  ProductService_changeCategory_ReturnBooleanTrue() {
		
		Mockito.when(msProductsClient.changeCategory(Mockito.anyLong(), Mockito.anyLong())).thenReturn(responseEntityBooleanTrue);
		
		Boolean resultValue = productService.changeCategory(06L, 78L);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(resultValue).isNotNull(),
				()-> Assertions.assertThat(resultValue).isTrue()
				);		
	}

	@Test
	void  ProductService_changeCategory_ReturnResourceNotFoundException() {
		
		Mockito.when(msProductsClient.changeCategory(Mockito.anyLong(), Mockito.anyLong())).thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, ()-> productService.changeCategory(06L, 78L));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(ResourceNotFoundException.class)
				);
	}
	
}
