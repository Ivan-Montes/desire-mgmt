package dev.ime.controller.impl;


import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import dev.ime.dto.CategoryDto;
import dev.ime.exceptionhandler.GlobalExceptionHandler;
import dev.ime.service.impl.CategoryService;

@WebMvcTest(CategoryController.class)
@AutoConfigureMockMvc(addFilters = false)
class CategoryControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private CategoryService categoryService;

	@MockitoBean
	private GlobalExceptionHandler globalExceptionHandler;
	
	@MockitoBean
	private Logger logger;

	private static final String PATH = "/categories";
	private static final String REDIRECT_CATEGORIES = "redirect:" + PATH;

	private final Long categoryId = 61L;
	private final String name = "Tesoros sagrados";
	private final String description = "Un Tesoro Sagrado o Shinki son espíritus que los dioses utilizan para diversas tareas y propósitos";
	private CategoryDto categoryDtoTest;
	
	@BeforeEach
	private void createObjects() {
		
		categoryDtoTest = new CategoryDto(categoryId, name, description);
		
	}
	
	@Test
	void CategoryController_getAll_ReturnStringView() throws Exception {
		
		mockMvc.perform(MockMvcRequestBuilders.get(PATH))
		.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
		.andExpect(MockMvcResultMatchers.view().name("sections/categories/index"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("categoryList"));
		
	}

	@Test
	void CategoryController_getAll_ReturnStringViewWithPageAttr() throws Exception {
		
		mockMvc.perform(MockMvcRequestBuilders.get(PATH)
				.param("page", "9"))
		.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
		.andExpect(MockMvcResultMatchers.view().name("sections/categories/index"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("categoryList"));
		
	}

	@Test
	void CategoryController_getAll_ReturnStringViewWithPageAndSizeAttr() throws Exception {
		
		mockMvc.perform(MockMvcRequestBuilders.get(PATH)
				.param("page", "18")
				.param("size", "7"))
		.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
		.andExpect(MockMvcResultMatchers.view().name("sections/categories/index"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("categoryList"));
		
	}

	@Test
	void CategoryController_getById_ReturnStringView() throws Exception {
		
		Mockito.when(categoryService.getById(Mockito.anyLong())).thenReturn(categoryDtoTest);
		
		mockMvc.perform(MockMvcRequestBuilders.get(PATH + "/{id}", categoryId))
		.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
		.andExpect(MockMvcResultMatchers.view().name("sections/categories/edit"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("category"));
	}

	@Test
	void CategoryController_add_ReturnStringView() throws Exception {
		
		mockMvc.perform(MockMvcRequestBuilders.get(PATH + "/add"))
		.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
		.andExpect(MockMvcResultMatchers.view().name("sections/categories/add"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("newCategory"));		
	}
	

	@Test
	void CategoryController_create_ReturnStringView() throws Exception {
		
		mockMvc.perform(MockMvcRequestBuilders.post(PATH + "/create")
				.param("name", name)
				.param("description", description))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
		.andExpect(MockMvcResultMatchers.view().name(REDIRECT_CATEGORIES))
		.andExpect(MockMvcResultMatchers.redirectedUrl(PATH));
	}

	@Test
	void CategoryController_update_ReturnStringView() throws Exception {
		
		mockMvc.perform(MockMvcRequestBuilders.post(PATH + "/update/{id}", categoryId)
				.param("categoryId", String.valueOf(categoryId))
				.param("name", name)
				.param("description", description))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
		.andExpect(MockMvcResultMatchers.view().name(REDIRECT_CATEGORIES))
		.andExpect(MockMvcResultMatchers.redirectedUrl(PATH));
	}

	@Test
	void CategoryController_confirmDelete_ReturnStringView() throws Exception {

		Mockito.when(categoryService.getById(Mockito.anyLong())).thenReturn(categoryDtoTest);
		
		mockMvc.perform(MockMvcRequestBuilders.get(PATH + "/confirm-delete/{id}", categoryId))
		.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
		.andExpect(MockMvcResultMatchers.view().name("sections/categories/delete"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("category"));
	}
	

	@Test
	void CategoryController_delete_ReturnStringView() throws Exception {
		
		mockMvc.perform(MockMvcRequestBuilders.get(PATH + "/delete/{id}", categoryId))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
		.andExpect(MockMvcResultMatchers.view().name(REDIRECT_CATEGORIES))
		.andExpect(MockMvcResultMatchers.redirectedUrl(PATH));		
	}
	
	
	
}
