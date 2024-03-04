package dev.ime.service.impl;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import dev.ime.dto.ProductDto;
import dev.ime.entity.Category;
import dev.ime.entity.Product;
import dev.ime.exception.AttributeUniqueException;
import dev.ime.exception.ResourceNotFoundException;
import dev.ime.mapper.impl.ProductMapper;
import dev.ime.repository.CategoryRepository;
import dev.ime.repository.ProductRepository;


@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

	@Mock
	private ProductRepository productRepo;
	
	@Mock
	private ProductMapper productMapper;
	
	@Mock
	private CategoryRepository categoryRepo;
	
	@InjectMocks
	private ProductServiceImpl productService;
	
	private List<Product>products;
	private Product proTest;
	private Product proTest2;
	private Long proId = 1L;
	private Long proId2 = 77L;
	private String proName = "Yukine";
	private Double unitPrice;
	private Integer unitInStock = 13;
	private Boolean discontinued = false;
	private Category catTest;	
	private ProductDto productDto;
	
	@BeforeEach
	void createObjects() {
		
		products = new ArrayList<>();
		
		catTest = new Category();
		catTest.setId(77L);
		
		proTest = new Product();
		proTest.setId(proId);
		proTest.setName(proName);
		proTest.setUnitPrice(unitPrice);
		proTest.setUnitInStock(unitInStock);
		proTest.setDiscontinued(discontinued);
		proTest.setCategory(catTest);
		
		proTest2 = new Product();
		proTest2.setId(proId2);
		
		productDto = new ProductMapper().toDto(proTest);
	}

	@Test
	void ProductServiceImpl_getAll_ReturnListProduct() {
		
		products.add(proTest);
		Mockito.when(productRepo.findAll()).thenReturn(products);
		
		List<Product>list = productService.getAll();
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(list).isNotNull(),
				()-> Assertions.assertThat(list).isNotEmpty(),
				()-> Assertions.assertThat(list).hasSize(1),
				()-> Assertions.assertThat(list.get(0).getId()).isEqualTo(proId)
				);
		Mockito.verify(productRepo, Mockito.times(1)).findAll();
	}
	

	@Test
	void ProductServiceImpl_getAllPaged_ReturnListProduct() {
		
		@SuppressWarnings("unchecked")
		Page<Product> page = Mockito.mock(Page.class);
		products.add(proTest);
		Mockito.doReturn(page).when(productRepo).findAll(Mockito.any(PageRequest.class));
		Mockito.when(page.toList()).thenReturn(products);
		
		List<Product>list = productService.getAllPaged(1,1);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(list).isNotNull(),
				()-> Assertions.assertThat(list).isNotEmpty(),
				()-> Assertions.assertThat(list).hasSize(1),
				()-> Assertions.assertThat(list.get(0).getId()).isEqualTo(proId)
				);
		Mockito.verify(productRepo, Mockito.times(1)).findAll(Mockito.any(PageRequest.class));
		Mockito.verify(page, Mockito.times(1)).toList();
	}
	
	@Test
	void ProductServiceImpl_getById_ReturnOptProduct() {
		
		Mockito.when(productRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(proTest));
		
		Optional<Product>optPro = productService.getById(proId);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optPro).isNotNull(),
				()-> Assertions.assertThat(optPro).isNotEmpty(),
				()-> Assertions.assertThat(optPro.get().getId()).isEqualTo(proId),
				()-> Assertions.assertThat(optPro.get().getName()).isEqualTo(proName)
				);
		Mockito.verify(productRepo, Mockito.times(1)).findById(Mockito.anyLong());
	}
	

	@Test
	void ProductServiceImpl_getById_ReturnResourceNotFoundException() {
		
		Mockito.when(productRepo.findById(Mockito.anyLong())).thenReturn(Optional.empty());
		
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, ()-> productService.getById(proId));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(ResourceNotFoundException.class)
				);
		Mockito.verify(productRepo, Mockito.times(1)).findById(Mockito.anyLong());
	}
	
	@Test
	void ProductServiceImpl_create_ReturnAttributeUniqueException() {
		
		products.add(proTest);
		Mockito.when(productRepo.findByName(Mockito.anyString())).thenReturn(products);
		
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(AttributeUniqueException.class, ()-> productService.create(productDto));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(AttributeUniqueException.class)
				);
		Mockito.verify(productRepo, Mockito.times(1)).findByName(Mockito.anyString());
	}
	
	@Test
	void ProductServiceImpl_create_ReturnResourceNotFoundException() {
		
		Mockito.when(productRepo.findByName(Mockito.anyString())).thenReturn(products);
		Mockito.when(productMapper.fromDto(Mockito.any(ProductDto.class))).thenReturn(proTest);
		Mockito.when(categoryRepo.findById(Mockito.anyLong())).thenReturn(Optional.empty());

		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, ()-> productService.create(productDto));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(ResourceNotFoundException.class)
				);
		
		Mockito.verify(productRepo, Mockito.times(1)).findByName(Mockito.anyString());
		Mockito.verify(productMapper, Mockito.times(1)).fromDto(Mockito.any(ProductDto.class));
		Mockito.verify(categoryRepo, Mockito.times(1)).findById(Mockito.anyLong());
	}
	

	@Test
	void ProductServiceImpl_create_ReturnOptProduct() {
		
		Mockito.when(productRepo.findByName(Mockito.anyString())).thenReturn(products);
		Mockito.when(productMapper.fromDto(Mockito.any(ProductDto.class))).thenReturn(proTest);
		Mockito.when(categoryRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(catTest));
		Mockito.when(productRepo.save(Mockito.any(Product.class))).thenReturn(proTest);
		
		Optional<Product>optPro = productService.create(productDto);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optPro).isNotNull(),
				()-> Assertions.assertThat(optPro).isNotEmpty(),
				()-> Assertions.assertThat(optPro.get().getId()).isEqualTo(proId),
				()-> Assertions.assertThat(optPro.get().getName()).isEqualTo(proName)
				);
		Mockito.verify(productRepo, Mockito.times(1)).findByName(Mockito.anyString());
		Mockito.verify(productMapper, Mockito.times(1)).fromDto(Mockito.any(ProductDto.class));
		Mockito.verify(categoryRepo, Mockito.times(1)).findById(Mockito.anyLong());
		Mockito.verify(productRepo, Mockito.times(1)).save(Mockito.any(Product.class));
	}
	
	@Test
	void ProductServiceImpl_update_ReturnResourceNotFoundExceptionProduct() {
		
		Mockito.when(productRepo.findById(Mockito.anyLong())).thenReturn(Optional.empty());
		
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, ()-> productService.update(proId, productDto));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(ResourceNotFoundException.class)
				);
		Mockito.verify(productRepo, Mockito.times(1)).findById(Mockito.anyLong());		
	}	

	@Test
	void ProductServiceImpl_update_ReturnResourceNotFoundExceptionCategory() {
		
		Mockito.when(productRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(proTest));
		Mockito.when(categoryRepo.findById(Mockito.anyLong())).thenReturn(Optional.empty());
		
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, ()-> productService.update(proId, productDto));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(ResourceNotFoundException.class)
				);
		Mockito.verify(productRepo, Mockito.times(1)).findById(Mockito.anyLong());	
		Mockito.verify(categoryRepo, Mockito.times(1)).findById(Mockito.anyLong());		
	}	

	@Test
	void ProductServiceImpl_update_ReturnOptProduct() {
		
		Mockito.when(productRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(proTest));
		Mockito.when(categoryRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(catTest));
		Mockito.when(productRepo.findByName(Mockito.anyString())).thenReturn(products);
		Mockito.when(productRepo.save(Mockito.any(Product.class))).thenReturn(proTest);
		
		Optional<Product>optPro = productService.update(proId, productDto);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optPro).isNotNull(),
				()-> Assertions.assertThat(optPro).isNotEmpty(),
				()-> Assertions.assertThat(optPro.get().getId()).isEqualTo(proId),
				()-> Assertions.assertThat(optPro.get().getName()).isEqualTo(proName)
				);
		Mockito.verify(productRepo, Mockito.times(1)).findById(Mockito.anyLong());	
		Mockito.verify(categoryRepo, Mockito.times(1)).findById(Mockito.anyLong());	
		Mockito.verify(productRepo, Mockito.times(1)).findByName(Mockito.anyString());
		Mockito.verify(productRepo, Mockito.times(1)).save(Mockito.any(Product.class));		
	}
	

	@Test
	void ProductServiceImpl_update_ReturnAttributeUniqueException() {
		
		Mockito.when(productRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(proTest));
		Mockito.when(categoryRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(catTest));
		products.add(proTest2);
		Mockito.when(productRepo.findByName(Mockito.anyString())).thenReturn(products);
		
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(AttributeUniqueException.class, ()-> productService.update(proId, productDto));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(AttributeUniqueException.class)
				);
		
		Mockito.verify(productRepo, Mockito.times(1)).findById(Mockito.anyLong());	
		Mockito.verify(categoryRepo, Mockito.times(1)).findById(Mockito.anyLong());	
		Mockito.verify(productRepo, Mockito.times(1)).findByName(Mockito.anyString());	
	}
	

	@Test
	void ProductServiceImpl_delete_ReturnIntOk() {
		
		Mockito.doNothing().when(productRepo).deleteById(Mockito.anyLong());
		Mockito.when(productRepo.findById(Mockito.anyLong())).thenReturn(Optional.empty());
		
		Integer resultValue = productService.delete(proId);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(resultValue).isNotNull(),
				()-> Assertions.assertThat(resultValue).isZero()
				);
		Mockito.verify(productRepo, Mockito.times(1)).deleteById(Mockito.anyLong());
		Mockito.verify(productRepo, Mockito.times(1)).findById(Mockito.anyLong());
	}
	

	@Test
	void ProductServiceImpl_delete_ReturnIntFail() {
		
		Mockito.doNothing().when(productRepo).deleteById(Mockito.anyLong());
		Mockito.when(productRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(proTest));
		
		Integer resultValue = productService.delete(proId);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(resultValue).isNotNull(),
				()-> Assertions.assertThat(resultValue).isEqualTo(1)
				);
		Mockito.verify(productRepo, Mockito.times(1)).deleteById(Mockito.anyLong());
		Mockito.verify(productRepo, Mockito.times(1)).findById(Mockito.anyLong());
	}
	

	@Test
	void ProductServiceImpl_changeCategory_ReturnResourceNotFoundExceptionProduct() {
		
		Mockito.when(productRepo.findById(Mockito.anyLong())).thenReturn(Optional.empty());
		
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, ()-> productService.changeCategory(proId, proId2));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(ResourceNotFoundException.class)
				);
		Mockito.verify(productRepo, Mockito.times(1)).findById(Mockito.anyLong());
	}
	

	@Test
	void ProductServiceImpl_changeCategory_ReturnResourceNotFoundExceptionCategory() {
		
		Mockito.when(productRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(proTest));
		Mockito.when(categoryRepo.findById(Mockito.anyLong())).thenReturn(Optional.empty());
		
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, ()-> productService.changeCategory(proId, proId2));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(ResourceNotFoundException.class)
				);
		Mockito.verify(productRepo, Mockito.times(1)).findById(Mockito.anyLong());
		Mockito.verify(categoryRepo, Mockito.times(1)).findById(Mockito.anyLong());
	}
	
	@Test
	void ProductServiceImpl_changeCategory_ReturnTrue() {
		
		Mockito.when(productRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(proTest));
		Mockito.when(categoryRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(catTest));
		Mockito.when(productRepo.save(Mockito.any(Product.class))).thenReturn(proTest);
		
		Boolean resultBool =  productService.changeCategory(proId, proId2);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(resultBool).isNotNull(),
				()-> Assertions.assertThat(resultBool).isTrue()
				);	
		Mockito.verify(productRepo, Mockito.times(1)).findById(Mockito.anyLong());
		Mockito.verify(categoryRepo, Mockito.times(1)).findById(Mockito.anyLong());
		Mockito.verify(productRepo, Mockito.times(1)).save(Mockito.any(Product.class));
	}

	@Test
	void ProductServiceImpl_changeCategory_ReturnFalse() {
		
		Mockito.when(productRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(proTest));
		Mockito.when(categoryRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(catTest));
		Mockito.when(productRepo.save(Mockito.any(Product.class))).thenReturn(null);
		
		Boolean resultBool =  productService.changeCategory(proId, proId2);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(resultBool).isNotNull(),
				()-> Assertions.assertThat(resultBool).isFalse()
				);	
		Mockito.verify(productRepo, Mockito.times(1)).findById(Mockito.anyLong());
		Mockito.verify(categoryRepo, Mockito.times(1)).findById(Mockito.anyLong());
		Mockito.verify(productRepo, Mockito.times(1)).save(Mockito.any(Product.class));
	}
	
}
