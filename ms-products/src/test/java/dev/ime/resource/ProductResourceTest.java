package dev.ime.resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import dev.ime.dto.ProductDto;
import dev.ime.entity.Product;
import dev.ime.mapper.ProductMapper;
import dev.ime.service.impl.ProductServiceImpl;
import dev.ime.tool.SomeConstants;

@WebMvcTest(ProductResource.class)
@AutoConfigureMockMvc(addFilters = false)
class ProductResourceTest {
	
	@MockBean
	private ProductServiceImpl productService;
	
	@MockBean
	private ProductMapper productMapper;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private Logger logger;
	
	private final String path = "/api/products";
	private List<ProductDto>productsDtoS;
	private List<Product>products;
	private ProductDto proDto;
	private Product proTest;
	private final Long proId = 7L;
	private final Long catId = 13L;
	private final String proName = "Yukine";
	private final Double unitPrice = 99.9;
	private final Integer unitInStock = 17;
	
	@BeforeEach
	private void createObjects() {
		
		productsDtoS = new ArrayList<>();
		products = new ArrayList<>();
		
		proDto = new ProductDto(proId,proName,unitPrice,unitInStock,false,catId);
		
		proTest = new Product();
		proTest.setId(proId);
		proTest.setName(proName);
		proTest.setUnitPrice(unitPrice);
		proTest.setUnitInStock(unitInStock);
		proTest.setDiscontinued(false);
		
	}
	
	@Test
	void ProductResource_getAll_ReturnListEmpty() throws Exception {
		Mockito.when(productService.getAll()).thenReturn(products);
		
		mockMvc.perform(MockMvcRequestBuilders.get(path))
		.andExpect(MockMvcResultMatchers.status().isNoContent())
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.notNullValue()))
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.empty()))
		;
	}

	@Test
	void ProductResource_getAll_ReturnPagedList() throws Exception {
		products.add(proTest);
		productsDtoS.add(proDto);
		Mockito.when(productService.getAllPaged(Mockito.anyInt(),Mockito.anyInt())).thenReturn(products);
		Mockito.when(productMapper.toListProductDto(Mockito.anyList())).thenReturn(productsDtoS);
		
		mockMvc.perform(MockMvcRequestBuilders.get(path)
				.param("page", "1")
				.param("size", "1")
				)
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.notNullValue()))
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.hasSize(1)))
		.andExpect(MockMvcResultMatchers.jsonPath("$[0].productId", org.hamcrest.Matchers.equalTo(7)))
		.andExpect(MockMvcResultMatchers.jsonPath("$[0].name", org.hamcrest.Matchers.equalTo(proName)))
		;
	}

	@Test
	void ProductResource_getAll_ReturnListPagedDefaultSize() throws Exception {
		products.add(proTest);
		productsDtoS.add(proDto);
		Mockito.when(productService.getAllPaged(Mockito.anyInt(),Mockito.anyInt())).thenReturn(products);
		Mockito.when(productMapper.toListProductDto(Mockito.anyList())).thenReturn(productsDtoS);
		
		mockMvc.perform(MockMvcRequestBuilders.get(path)
				.param("page", "1")
				)
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.notNullValue()))
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.hasSize(1)))
		.andExpect(MockMvcResultMatchers.jsonPath("$[0].productId", org.hamcrest.Matchers.equalTo(7)))
		.andExpect(MockMvcResultMatchers.jsonPath("$[0].name", org.hamcrest.Matchers.equalTo(proName)))
		;
	}
	

	@Test
	void ProductResource_getById_ReturnProductDto() throws Exception {
		
		Mockito.when(productService.getById(Mockito.anyLong())).thenReturn(Optional.of(proTest));
		Mockito.when(productMapper.toProductDto(Mockito.any(Product.class))).thenReturn(proDto);
		
		mockMvc.perform(MockMvcRequestBuilders.get(path + "/{id}", proId))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.notNullValue()))
		.andExpect(MockMvcResultMatchers.jsonPath("$.productId", org.hamcrest.Matchers.equalTo(7)))
		.andExpect(MockMvcResultMatchers.jsonPath("$.name", org.hamcrest.Matchers.equalTo(proName)))
		;
	}
	
	@Test
	void ProductResource_getById_ReturnProductDtoEmpty() throws Exception {
		
		Mockito.when(productService.getById(Mockito.anyLong())).thenReturn(Optional.empty());
		
		mockMvc.perform(MockMvcRequestBuilders.get(path + "/{id}", proId))
		.andExpect(MockMvcResultMatchers.status().isNotFound())
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.notNullValue()))
		.andExpect(MockMvcResultMatchers.jsonPath("$.productId", org.hamcrest.Matchers.equalTo(0)))
		.andExpect(MockMvcResultMatchers.jsonPath("$.name", org.hamcrest.Matchers.equalTo(SomeConstants.DATELESS)))
		;
	}
	

	@Test
	void ProductResource_create_ReturnProductDto() throws Exception{
		
		Mockito.when(productService.create(Mockito.any(ProductDto.class))).thenReturn(Optional.of(proTest));
		Mockito.when(productMapper.toProductDto(Mockito.any(Product.class))).thenReturn(proDto);

		mockMvc.perform(MockMvcRequestBuilders.post(path)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(proDto))
				)
		.andExpect(MockMvcResultMatchers.status().isCreated())
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.notNullValue()))
		.andExpect(MockMvcResultMatchers.jsonPath("$.name", org.hamcrest.Matchers.equalTo(proName)))		
		;
	}
	

	@Test
	void ProductResource_create_ReturnProductDtoEmtpy() throws Exception{
		
		Mockito.when(productService.create(Mockito.any(ProductDto.class))).thenReturn(Optional.empty());

		mockMvc.perform(MockMvcRequestBuilders.post(path)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(proDto))
				)		
		.andExpect(MockMvcResultMatchers.status().isNotFound())
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.notNullValue()))
		.andExpect(MockMvcResultMatchers.jsonPath("$.productId",  org.hamcrest.Matchers.equalTo(0)))
		.andExpect(MockMvcResultMatchers.jsonPath("$.name", org.hamcrest.Matchers.equalTo(SomeConstants.DATELESS)))
		;
	}
	
	@Test
	void ProductResource_update_ReturnProductDto() throws Exception{
		
		Mockito.when(productService.update(Mockito.anyLong(), Mockito.any(ProductDto.class))).thenReturn(Optional.of(proTest));
		Mockito.when(productMapper.toProductDto(Mockito.any(Product.class))).thenReturn(proDto);

		mockMvc.perform(MockMvcRequestBuilders.put(path + "/{id}", proId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(proDto))
				)
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.notNullValue()))
		.andExpect(MockMvcResultMatchers.jsonPath("$.productId", org.hamcrest.Matchers.equalTo(7)))
		.andExpect(MockMvcResultMatchers.jsonPath("$.name", org.hamcrest.Matchers.equalTo(proName)))		
		;
	}
	

	@Test
	void ProductResource_update_ReturnProductDtoEmpty() throws Exception{
		
		Mockito.when(productService.update(Mockito.anyLong(), Mockito.any(ProductDto.class))).thenReturn(Optional.empty());
		
		mockMvc.perform(MockMvcRequestBuilders.put(path + "/{id}", proId)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(proDto))
			)		
		.andExpect(MockMvcResultMatchers.status().isNotFound())
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.notNullValue()))
		.andExpect(MockMvcResultMatchers.jsonPath("$.productId",  org.hamcrest.Matchers.equalTo(0)))
		.andExpect(MockMvcResultMatchers.jsonPath("$.name", org.hamcrest.Matchers.equalTo(SomeConstants.DATELESS)))
		;
	}
	

	@Test
	void ProductResource_delete_ReturnBooleanTrue() throws Exception {
		
		Mockito.when(productService.delete(Mockito.anyLong())).thenReturn(0);
		
		mockMvc.perform(MockMvcRequestBuilders.delete(path + "/{id}", proId))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$").exists())
		.andExpect(MockMvcResultMatchers.jsonPath("$").isBoolean())
		.andExpect(MockMvcResultMatchers.jsonPath("$").value(true))	
		;
	}

	@Test
	void ProductResource_delete_ReturnBooleanFalse() throws Exception {
		
		Mockito.when(productService.delete(Mockito.anyLong())).thenReturn(1);
		
		mockMvc.perform(MockMvcRequestBuilders.delete(path + "/{id}", proId))
		.andExpect(MockMvcResultMatchers.status().isNotFound())
		.andExpect(MockMvcResultMatchers.jsonPath("$").exists())
		.andExpect(MockMvcResultMatchers.jsonPath("$").isBoolean())
		.andExpect(MockMvcResultMatchers.jsonPath("$").value(false))			
		;
	}
	
	@Test
	void ProductResource_changeCategory_ReturnBooleanTrue() throws Exception {
		
		Mockito.when(productService.changeCategory(Mockito.anyLong(), Mockito.anyLong())).thenReturn(true);
		
		mockMvc.perform(MockMvcRequestBuilders.put(path + "/{id}/categories/{categoryId}", proId, catId))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$").exists())
		.andExpect(MockMvcResultMatchers.jsonPath("$").isBoolean())
		.andExpect(MockMvcResultMatchers.jsonPath("$").value(true))	
		;
	}

	@Test
	void ProductResource_changeCategory_ReturnBooleanFalse() throws Exception {
		
		Mockito.when(productService.changeCategory(Mockito.anyLong(), Mockito.anyLong())).thenReturn(false);
		
		mockMvc.perform(MockMvcRequestBuilders.put(path + "/{id}/categories/{categoryId}", proId, catId))
		.andExpect(MockMvcResultMatchers.status().isNotFound())
		.andExpect(MockMvcResultMatchers.jsonPath("$").exists())
		.andExpect(MockMvcResultMatchers.jsonPath("$").isBoolean())
		.andExpect(MockMvcResultMatchers.jsonPath("$").value(false))	
		;
	}
}
