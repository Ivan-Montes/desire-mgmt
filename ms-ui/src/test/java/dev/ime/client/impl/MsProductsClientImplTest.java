package dev.ime.client.impl;


import java.util.Collections;
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

import dev.ime.client.MsProductsClient;
import dev.ime.dto.CategoryDto;
import dev.ime.dto.ProductDto;
import dev.ime.dtomvc.CategoryMvcDto;
import dev.ime.dtomvc.ProductMvcDto;
import dev.ime.mapper.impl.CategoryMvcMapper;
import dev.ime.mapper.impl.ProductMvcMapper;


@ExtendWith(MockitoExtension.class)
class MsProductsClientImplTest {


	@Mock
	private MsProductsClient msProductsClient;

	@InjectMocks
	private MsProductsClientImpl msProductsClientImpl;
	

	private ResponseEntity<Boolean> responseEntityBoolTrue;
	private ResponseEntity<ProductDto> responseEntityWithProductDtoEmpy;
	private ResponseEntity<List<ProductDto>> responseEntityWithListProductDtoEmpy;	
	private ProductMvcMapper productMvcMapper;	
	private ProductDto productDtoTest;

	private ResponseEntity<CategoryDto> responseEntityWithCategoryDtoEmpy;
	private ResponseEntity<List<CategoryDto>> responseEntityWithListCategoryDtoEmpy; 
	private CategoryMvcMapper categoryMvcMapper;
	private CategoryDto categoryDtoTest;
	
	
	@BeforeEach
	private void createObjects() {		

		responseEntityBoolTrue = new ResponseEntity<>(true, HttpStatus.OK);
		responseEntityWithProductDtoEmpy = new ResponseEntity<>(new ProductDto(), HttpStatus.OK);
		responseEntityWithListProductDtoEmpy = new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);		
		productMvcMapper = new ProductMvcMapper();
		productDtoTest = productMvcMapper.fromMvcDtoToDto(new ProductMvcDto());
		
		responseEntityWithCategoryDtoEmpy = new ResponseEntity<>(new CategoryDto(), HttpStatus.OK);
		responseEntityWithListCategoryDtoEmpy = new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
		categoryMvcMapper = new CategoryMvcMapper();
		categoryDtoTest = categoryMvcMapper.fromMvcDtoToDto(new CategoryMvcDto());
		
	}
	
	
	@Test
	void MsProductsClientImpl_getAllProduct_ReturnResponseEntityListProductDto() {
		
		Mockito.when(msProductsClient.getAllProduct()).thenReturn(responseEntityWithListProductDtoEmpy);
		
		ResponseEntity<List<ProductDto>> response = msProductsClientImpl.getAllProduct();
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK)
				);
	}

	@Test
	void MsProductsClientImpl_getAllProduct_ReturnResponseEntityListProductDtoError() {
		
		Mockito.when(msProductsClient.getAllProduct()).thenThrow(new RuntimeException());
		
		ResponseEntity<List<ProductDto>> response = msProductsClientImpl.getAllProduct();
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND)
				);
	}

	@Test
	void MsProductsClientImpl_getAllProductPaged_ReturnResponseEntityListProductDto() {
		
		Mockito.when(msProductsClient.getAllProductPaged(Mockito.anyInt(),Mockito.anyInt())).thenReturn(responseEntityWithListProductDtoEmpy);
		
		ResponseEntity<List<ProductDto>> response = msProductsClientImpl.getAllProductPaged(Mockito.anyInt(),Mockito.anyInt());
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK)
				);
	}

	@Test
	void MsProductsClientImpl_getAllProductPaged_ReturnResponseEntityListProductDtoError() {
		
		Mockito.when(msProductsClient.getAllProductPaged(Mockito.anyInt(),Mockito.anyInt())).thenThrow(new RuntimeException());
		
		ResponseEntity<List<ProductDto>> response = msProductsClientImpl.getAllProductPaged(Mockito.anyInt(),Mockito.anyInt());
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND)
				);
	}

	@Test
	void MsProductsClientImpl_getProductById_ReturnResponseEntityProductDto() {
		
		Mockito.when(msProductsClient.getProductById(Mockito.anyLong())).thenReturn(responseEntityWithProductDtoEmpy);
		
		ResponseEntity<ProductDto> response = msProductsClientImpl.getProductById(1L);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK)
				);
	}

	@Test
	void MsProductsClientImpl_getProductById_ReturnResponseEntityProductDtoError() {
		
		Mockito.when(msProductsClient.getProductById(Mockito.anyLong())).thenThrow(new RuntimeException());
		
		ResponseEntity<ProductDto> response = msProductsClientImpl.getProductById(1L);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND)
				);
	}

	@Test
	void MsProductsClientImpl_create_ReturnResponseEntityProductDto() {
		
		Mockito.when(msProductsClient.create(Mockito.any(ProductDto.class))).thenReturn(responseEntityWithProductDtoEmpy);
		
		ResponseEntity<ProductDto> response = msProductsClientImpl.create(productDtoTest);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK)
				);
	}

	@Test
	void MsProductsClientImpl_create_ReturnResponseEntityProductDtoError() {
		
		Mockito.when(msProductsClient.create(Mockito.any(ProductDto.class))).thenThrow(new RuntimeException());
		
		ResponseEntity<ProductDto> response = msProductsClientImpl.create(productDtoTest);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND)
				);
	}

	@Test
	void MsProductsClientImpl_update_ReturnResponseEntityProductDto() {
		
		Mockito.when(msProductsClient.update(Mockito.anyLong(), Mockito.any(ProductDto.class))).thenReturn(responseEntityWithProductDtoEmpy);
		
		ResponseEntity<ProductDto> response = msProductsClientImpl.update(Mockito.anyLong(), productDtoTest);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK)
				);
	}

	@Test
	void MsProductsClientImpl_update_ReturnResponseEntityProductDtoError() {
		
		Mockito.when(msProductsClient.update(Mockito.anyLong(), Mockito.any(ProductDto.class))).thenThrow(new RuntimeException());
		
		ResponseEntity<ProductDto> response = msProductsClientImpl.update(Mockito.anyLong(), productDtoTest);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND)
				);
	}

	@Test
	void MsProductsClientImpl_deleteProduct_ReturnResponseEntityBooleanTrue() {

		Mockito.when(msProductsClient.deleteProduct(Mockito.anyLong())).thenReturn(responseEntityBoolTrue);
		
		ResponseEntity<Boolean> response = msProductsClientImpl.deleteProduct(3L);

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
				()-> Assertions.assertThat(response.getBody().booleanValue())
				);	
	}

	@Test
	void MsProductsClientImpl_deleteProduct_ReturnResponseEntityBooleanTrueError() {

		Mockito.when(msProductsClient.deleteProduct(Mockito.anyLong())).thenThrow(new RuntimeException());
		
		ResponseEntity<Boolean> response = msProductsClientImpl.deleteProduct(3L);

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND),
				()-> Assertions.assertThat(response.getBody().booleanValue())
				);	
	}

	@Test
	void MsProductsClientImpl_changeCategory_ReturnResponseEntityBooleanTrue() {

		Mockito.when(msProductsClient.changeCategory(Mockito.anyLong(),Mockito.anyLong())).thenReturn(responseEntityBoolTrue);
		
		ResponseEntity<Boolean> response = msProductsClientImpl.changeCategory(3L,4L);

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
				()-> Assertions.assertThat(response.getBody().booleanValue())
				);	
	}

	@Test
	void MsProductsClientImpl_changeCategory_ReturnResponseEntityBooleanTrueError() {

		Mockito.when(msProductsClient.changeCategory(Mockito.anyLong(),Mockito.anyLong())).thenThrow(new RuntimeException());
		
		ResponseEntity<Boolean> response = msProductsClientImpl.changeCategory(3L,4L);

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND),
				()-> Assertions.assertThat(response.getBody().booleanValue())
				);	
	}


	@Test
	void MsProductsClientImpl_getAllProduct_ReturnResponseEntityListCategorydto() {
		
		Mockito.when(msProductsClient.getAllCategory()).thenReturn(responseEntityWithListCategoryDtoEmpy);
		
		ResponseEntity<List<CategoryDto>> response = msProductsClientImpl.getAllCategory();
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK)
				);
	}

	@Test
	void MsProductsClientImpl_getAllCategory_ReturnResponseEntityListCategoryDtoError() {
		
		Mockito.when(msProductsClient.getAllCategory()).thenThrow(new RuntimeException());
		
		ResponseEntity<List<CategoryDto>> response = msProductsClientImpl.getAllCategory();
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND)
				);
	}

	@Test
	void MsProductsClientImpl_getAllCategoryPaged_ReturnResponseEntityListCategoryDto() {
		
		Mockito.when(msProductsClient.getAllCategoryPaged(Mockito.anyInt(),Mockito.anyInt())).thenReturn(responseEntityWithListCategoryDtoEmpy);
		
		ResponseEntity<List<CategoryDto>> response = msProductsClientImpl.getAllCategoryPaged(Mockito.anyInt(),Mockito.anyInt());
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK)
				);
	}

	@Test
	void MsProductsClientImpl_getAllCategoryPaged_ReturnResponseEntityListCategoryDtoError() {
		
		Mockito.when(msProductsClient.getAllCategoryPaged(Mockito.anyInt(),Mockito.anyInt())).thenThrow(new RuntimeException());
		
		ResponseEntity<List<CategoryDto>> response = msProductsClientImpl.getAllCategoryPaged(Mockito.anyInt(),Mockito.anyInt());
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND)
				);
	}

	@Test
	void MsProductsClientImpl_getCategoryById_ReturnResponseEntityCategoryDto() {
		
		Mockito.when(msProductsClient.getCategoryById(Mockito.anyLong())).thenReturn(responseEntityWithCategoryDtoEmpy);
		
		ResponseEntity<CategoryDto> response = msProductsClientImpl.getCategoryById(2L);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK)
				);
	}

	@Test
	void MsProductsClientImpl_getCategoryById_ReturnResponseEntityCategoryDtoError() {
		
		Mockito.when(msProductsClient.getCategoryById(Mockito.anyLong())).thenThrow(new RuntimeException());
		
		ResponseEntity<CategoryDto> response = msProductsClientImpl.getCategoryById(2L);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND)
				);
	}

	@Test
	void MsProductsClientImpl_create_ReturnResponseEntityCategoryDto() {
		
		Mockito.when(msProductsClient.create(Mockito.any(CategoryDto.class))).thenReturn(responseEntityWithCategoryDtoEmpy);
		
		ResponseEntity<CategoryDto> response = msProductsClientImpl.create(categoryDtoTest);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK)
				);
	}

	@Test
	void MsProductsClientImpl_create_ReturnResponseEntityCategoryDtoError() {
		
		Mockito.when(msProductsClient.create(Mockito.any(CategoryDto.class))).thenThrow(new RuntimeException());
		
		ResponseEntity<CategoryDto> response = msProductsClientImpl.create(categoryDtoTest);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND)
				);
	}

	@Test
	void MsProductsClientImpl_update_ReturnResponseEntityCategoryDto() {
		
		Mockito.when(msProductsClient.update(Mockito.anyLong(), Mockito.any(CategoryDto.class))).thenReturn(responseEntityWithCategoryDtoEmpy);
		
		ResponseEntity<CategoryDto> response = msProductsClientImpl.update(Mockito.anyLong(), categoryDtoTest);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK)
				);
	}

	@Test
	void MsProductsClientImpl_update_ReturnResponseEntityCategoryDtoError() {
		
		Mockito.when(msProductsClient.update(Mockito.anyLong(), Mockito.any(CategoryDto.class))).thenThrow(new RuntimeException());
		
		ResponseEntity<CategoryDto> response = msProductsClientImpl.update(Mockito.anyLong(), categoryDtoTest);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND)
				);
	}

	@Test
	void MsProductsClientImpl_deleteCategory_ReturnResponseEntityBooleanTrue() {

		Mockito.when(msProductsClient.deleteCategory(Mockito.anyLong())).thenReturn(responseEntityBoolTrue);
		
		ResponseEntity<Boolean> response = msProductsClientImpl.deleteCategory(64L);

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
				()-> Assertions.assertThat(response.getBody().booleanValue())
				);	
	}

	@Test
	void MsProductsClientImpl_deleteCategory_ReturnResponseEntityBooleanTrueError() {

		Mockito.when(msProductsClient.deleteCategory(Mockito.anyLong())).thenThrow(new RuntimeException());
		
		ResponseEntity<Boolean> response = msProductsClientImpl.deleteCategory(13L);

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND),
				()-> Assertions.assertThat(response.getBody().booleanValue())
				);	
	}

	@Test
	void MsProductsClientImpl_addProductToCategory_ReturnResponseEntityBooleanTrue() {
		
		Mockito.when(msProductsClient.addProductToCategory(Mockito.anyLong(), Mockito.anyLong())).thenReturn(responseEntityBoolTrue);

		ResponseEntity<Boolean> response = msProductsClientImpl.addProductToCategory(90l,64L);

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
				()-> Assertions.assertThat(response.getBody().booleanValue())
				);	
	}

	@Test
	void MsProductsClientImpl_addProductToCategory_ReturnResponseEntityBooleanTrueError() {
		
		Mockito.when(msProductsClient.addProductToCategory(Mockito.anyLong(), Mockito.anyLong())).thenThrow(new RuntimeException());

		ResponseEntity<Boolean> response = msProductsClientImpl.addProductToCategory(90l,64L);

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(response).isNotNull(),
				()-> Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND),
				()-> Assertions.assertThat(response.getBody().booleanValue())
				);	
	}
	
	
}
