package dev.ime.controller.impl;


import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import dev.ime.dto.ProductDto;
import dev.ime.exceptionhandler.GlobalExceptionHandler;
import dev.ime.service.impl.CategoryService;
import dev.ime.service.impl.ProductService;


@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProductControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private ProductService productService;
	
	@MockBean
	private CategoryService categoryService;

	@MockBean
	private GlobalExceptionHandler globalExceptionHandler;
	
	@MockBean
	private Logger logger;
	
	private final String PATH = "/products";
	private final String REDIRECT_PRODUCTS = "redirect:" + PATH;
	private Long productId = 77L;
	private String name = "Yukine";
	private Double unitPrice = 9.99;
	private Integer unitInStock = 13;
	private Boolean discontinued = false;
	private Long categoryId = 63L;
	private ProductDto productDtoTest;
	
	@BeforeEach
	void createObjects() {
		
		productDtoTest = new ProductDto(productId, name, unitPrice, unitInStock, discontinued, categoryId);
		
	}

	@Test
	void ProductController_getAll_ReturnStringView() throws Exception {		

		mockMvc.perform(MockMvcRequestBuilders.get(PATH))
		.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
		.andExpect(MockMvcResultMatchers.view().name("sections/products/index"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("productList"));
	}
	
	@Test
	void ProductController_getAll_ReturnStringViewWithPageAttr() throws Exception {		

		mockMvc.perform(MockMvcRequestBuilders.get(PATH)
		.param("page", "9"))
		.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
		.andExpect(MockMvcResultMatchers.view().name("sections/products/index"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("productList"));
	}

	@Test
	void ProductController_getAll_ReturnStringViewWithPageAndSizeAttr() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.get(PATH)
		.param("page", "18")
		.param("size", "7"))
		.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
		.andExpect(MockMvcResultMatchers.view().name("sections/products/index"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("productList"));
	}

	@Test
	void CategoryController_getById_ReturnStringView() throws Exception {
		
		Mockito.when(productService.getById(Mockito.anyLong())).thenReturn(productDtoTest);
		
		mockMvc.perform(MockMvcRequestBuilders.get(PATH + "/{id}", productId))
		.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
		.andExpect(MockMvcResultMatchers.view().name("sections/products/edit"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("product"));
	}

	@Test
	void CategoryController_add_ReturnStringView() throws Exception {
		
		mockMvc.perform(MockMvcRequestBuilders.get(PATH + "/add"))
		.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
		.andExpect(MockMvcResultMatchers.view().name("sections/products/add"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("newProduct"));		
	}	

	@Test
	void ProductController_create_ReturnStringView() throws Exception {
		
		mockMvc.perform(MockMvcRequestBuilders.post(PATH + "/create")
				.param("name", name)
				.param("unitPrice", String.valueOf(unitPrice))
				.param("unitInStock", String.valueOf(unitInStock))
				.param("discontinued", discontinued.toString())
				.param("categoryId", String.valueOf(categoryId)))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
		.andExpect(MockMvcResultMatchers.view().name(REDIRECT_PRODUCTS))
		.andExpect(MockMvcResultMatchers.redirectedUrl(PATH));
	}

	@Test
	void ProductController_update_ReturnStringView() throws Exception {
		
		mockMvc.perform(MockMvcRequestBuilders.post(PATH + "/update/{id}", productId)
				.param("productId", String.valueOf(productId))
				.param("name", name)
				.param("unitPrice", String.valueOf(unitPrice))
				.param("unitInStock", String.valueOf(unitInStock))
				.param("discontinued", discontinued.toString())
				.param("categoryId", String.valueOf(categoryId)))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
		.andExpect(MockMvcResultMatchers.view().name(REDIRECT_PRODUCTS))
		.andExpect(MockMvcResultMatchers.redirectedUrl(PATH));
	}

	@Test
	void ProductController_confirmDelete_ReturnStringView() throws Exception {

		Mockito.when(productService.getById(Mockito.anyLong())).thenReturn(productDtoTest);
		
		mockMvc.perform(MockMvcRequestBuilders.get(PATH + "/confirm-delete/{id}", productId))
		.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
		.andExpect(MockMvcResultMatchers.view().name("sections/products/delete"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("product"));
	}	

	@Test
	void ProductController_delete_ReturnStringView() throws Exception {
		
		mockMvc.perform(MockMvcRequestBuilders.get(PATH + "/delete/{id}", productId))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
		.andExpect(MockMvcResultMatchers.view().name(REDIRECT_PRODUCTS))
		.andExpect(MockMvcResultMatchers.redirectedUrl(PATH));		
	}	
	
}
