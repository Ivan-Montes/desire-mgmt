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

import dev.ime.dto.CategoryDto;
import dev.ime.entity.Category;
import dev.ime.mapper.impl.CategoryMapper;
import dev.ime.resource.impl.CategoryResource;
import dev.ime.service.impl.CategoryServiceImpl;
import dev.ime.tool.SomeConstants;

@WebMvcTest(CategoryResource.class)
@AutoConfigureMockMvc(addFilters = false)
class CategoryResourceTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private CategoryServiceImpl categoryService;
	
	@MockBean
	private  CategoryMapper categoryMapper;	
	
	@Autowired
    private ObjectMapper objectMapper;

	@MockBean
	private Logger logger;
	
	private final String path = "/api/categories";
	private List<CategoryDto>categoriesDtoS;
	private List<Category>categories;
	private CategoryDto catDto;
	private Category catTest;
	private final Long catId = 7L;
	private final String catName = "Tesoros sagrados";
	private final String catDescription = "Un Tesoro Sagrado o Shinki son espíritus que los dioses utilizan para diversas tareas y propósitos";
	
	@BeforeEach
	private void createObjects() {
		
		categories = new ArrayList<>();
		categoriesDtoS = new ArrayList<>();
		
		catDto = new CategoryDto(catId, catName, catDescription);
		
		catTest = new Category();
		catTest.setId(catId);
	}
	
	@Test
	void CategoryResource_getAll_ReturnListEmpty() throws Exception{
				
		Mockito.when(categoryService.getAll()).thenReturn(categories);
		
		mockMvc.perform(MockMvcRequestBuilders.get(path))
		.andExpect(MockMvcResultMatchers.status().isNoContent())
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.notNullValue()))
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.empty()))
		;
	}

	@Test
	void CategoryResource_getAll_ReturnListPagedDefaultSize() throws Exception{
		
		categories.add(catTest);
		categoriesDtoS.add(catDto);
		Mockito.when(categoryService.getAllPaged(Mockito.anyInt(), Mockito.anyInt())).thenReturn(categories);
		Mockito.when(categoryMapper.toListDto(Mockito.anyList())).thenReturn(categoriesDtoS);
		
		mockMvc.perform(MockMvcRequestBuilders.get(path)
				.param("page", "1"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.notNullValue()))
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.hasSize(1)))
		.andExpect(MockMvcResultMatchers.jsonPath("$[0].categoryId", org.hamcrest.Matchers.equalTo(7)))
		.andExpect(MockMvcResultMatchers.jsonPath("$[0].name", org.hamcrest.Matchers.equalTo(catName)))
		.andExpect(MockMvcResultMatchers.jsonPath("$[0].description", org.hamcrest.Matchers.equalTo(catDescription)))
		;
	}
	

	@Test
	void CategoryResource_getAll_ReturnListPaged() throws Exception{
		
		categories.add(catTest);
		categoriesDtoS.add(catDto);
		Mockito.when(categoryService.getAllPaged(Mockito.anyInt(), Mockito.anyInt())).thenReturn(categories);
		Mockito.when(categoryMapper.toListDto(Mockito.anyList())).thenReturn(categoriesDtoS);
		
		mockMvc.perform(MockMvcRequestBuilders.get(path)
				.param("page", "1")
				.param("size", "1"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.notNullValue()))
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.hasSize(1)))
		.andExpect(MockMvcResultMatchers.jsonPath("$[0].categoryId", org.hamcrest.Matchers.equalTo(7)))
		.andExpect(MockMvcResultMatchers.jsonPath("$[0].name", org.hamcrest.Matchers.equalTo(catName)))
		.andExpect(MockMvcResultMatchers.jsonPath("$[0].description", org.hamcrest.Matchers.equalTo(catDescription)))
		;
	}
	
	@Test
	void CategoryResource_getById_ReturnCategoryDto() throws Exception {
		
		Mockito.when(categoryService.getById(Mockito.anyLong())).thenReturn(Optional.of(catTest));
		Mockito.when(categoryMapper.toDto(Mockito.any(Category.class))).thenReturn(catDto);
		
		mockMvc.perform(MockMvcRequestBuilders.get(path + "/{id}", catId))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.notNullValue()))
		.andExpect(MockMvcResultMatchers.jsonPath("$.categoryId",  org.hamcrest.Matchers.equalTo(7)))
		.andExpect(MockMvcResultMatchers.jsonPath("$.name", org.hamcrest.Matchers.equalTo(catName)))
		;
	}
	
	@Test
	void CategoryResource_getById_ReturnCategoryDtoEmpty() throws Exception{
		
		Mockito.when(categoryService.getById(Mockito.anyLong())).thenReturn(Optional.empty());
		
		mockMvc.perform(MockMvcRequestBuilders.get(path + "/{id}", catId))
		.andExpect(MockMvcResultMatchers.status().isNotFound())
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.notNullValue()))
		.andExpect(MockMvcResultMatchers.jsonPath("$.categoryId",  org.hamcrest.Matchers.equalTo(0)))
		.andExpect(MockMvcResultMatchers.jsonPath("$.name", org.hamcrest.Matchers.equalTo(SomeConstants.DATELESS)))
		;
	}
	

	@Test
	void CategoryResource_create_ReturnCategoryDto() throws Exception{
		
		Mockito.when(categoryService.create(Mockito.any(CategoryDto.class))).thenReturn(Optional.of(catTest));
		Mockito.when(categoryMapper.toDto(Mockito.any(Category.class))).thenReturn(catDto);
		
		mockMvc.perform(MockMvcRequestBuilders.post(path)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(catDto))
				)
		.andExpect(MockMvcResultMatchers.status().isCreated())
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.notNullValue()))
		.andExpect(MockMvcResultMatchers.jsonPath("$.name", org.hamcrest.Matchers.equalTo(catName)))		
		;
	}

	@Test
	void CategoryResource_create_ReturnCategoryDtoEmpty() throws Exception{
		
		Mockito.when(categoryService.create(Mockito.any(CategoryDto.class))).thenReturn(Optional.empty());
		
		mockMvc.perform(MockMvcRequestBuilders.post(path)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(catDto))
				)		
		.andExpect(MockMvcResultMatchers.status().isNotFound())
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.notNullValue()))
		.andExpect(MockMvcResultMatchers.jsonPath("$.categoryId",  org.hamcrest.Matchers.equalTo(0)))
		.andExpect(MockMvcResultMatchers.jsonPath("$.name", org.hamcrest.Matchers.equalTo(SomeConstants.DATELESS)))
		;
	}

	@Test
	void CategoryResource_update_ReturnCategoryDto() throws Exception{
		
		Mockito.when(categoryService.update(Mockito.anyLong(), Mockito.any(CategoryDto.class))).thenReturn(Optional.of(catTest));
		Mockito.when(categoryMapper.toDto(Mockito.any(Category.class))).thenReturn(catDto);
		
		mockMvc.perform(MockMvcRequestBuilders.put(path + "/{id}", catId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(catDto))
				)		
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.notNullValue()))
		.andExpect(MockMvcResultMatchers.jsonPath("$.categoryId",  org.hamcrest.Matchers.equalTo(7)))
		.andExpect(MockMvcResultMatchers.jsonPath("$.name", org.hamcrest.Matchers.equalTo(catName)))		
		;
	}

	@Test
	void CategoryResource_update_ReturnCategoryDtoEmpty() throws Exception{
		
		Mockito.when(categoryService.update(Mockito.anyLong(), Mockito.any(CategoryDto.class))).thenReturn(Optional.empty());
		
		mockMvc.perform(MockMvcRequestBuilders.put(path + "/{id}", catId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(catDto))
				)		
		.andExpect(MockMvcResultMatchers.status().isNotFound())
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.notNullValue()))
		.andExpect(MockMvcResultMatchers.jsonPath("$.categoryId",  org.hamcrest.Matchers.equalTo(0)))
		.andExpect(MockMvcResultMatchers.jsonPath("$.name", org.hamcrest.Matchers.equalTo(SomeConstants.DATELESS)))	
		;
	}
	

	@Test
	void CategoryResource_delete_ReturnTrue() throws Exception{
		
		Mockito.when(categoryService.delete(Mockito.anyLong())).thenReturn(0);
		
		mockMvc.perform(MockMvcRequestBuilders.delete(path + "/{id}", catId))	
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$").exists())
		.andExpect(MockMvcResultMatchers.jsonPath("$").isBoolean())
		.andExpect(MockMvcResultMatchers.jsonPath("$").value(true))		
		;
	}	

	@Test
	void CategoryResource_delete_ReturnFalse() throws Exception{
		
		Mockito.when(categoryService.delete(Mockito.anyLong())).thenReturn(1);
		
		mockMvc.perform(MockMvcRequestBuilders.delete(path + "/{id}", catId))	
		.andExpect(MockMvcResultMatchers.status().isNotFound())
		.andExpect(MockMvcResultMatchers.jsonPath("$").exists())
		.andExpect(MockMvcResultMatchers.jsonPath("$").isBoolean())
		.andExpect(MockMvcResultMatchers.jsonPath("$").value(false))		
		;
	}

	@Test
	void CategoryResource_addProductToCategory_ReturnTrue() throws Exception{
		
		Mockito.when(categoryService.addProductToCategory(Mockito.anyLong(), Mockito.anyLong())).thenReturn(true);
	
		mockMvc.perform(MockMvcRequestBuilders.put(path +"/{categoryId}/products/{productId}", catId, catId))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$").exists())
		.andExpect(MockMvcResultMatchers.jsonPath("$").isBoolean())
		.andExpect(MockMvcResultMatchers.jsonPath("$").value(true))
		;
	}
	

	@Test
	void CategoryResource_addProductToCategory_ReturnFalse() throws Exception{
		
		Mockito.when(categoryService.addProductToCategory(Mockito.anyLong(), Mockito.anyLong())).thenReturn(false);
		
		mockMvc.perform(MockMvcRequestBuilders.put(path +"/{categoryId}/products/{productId}", catId, catId))
		.andExpect(MockMvcResultMatchers.status().isNotFound())
		.andExpect(MockMvcResultMatchers.jsonPath("$").exists())
		.andExpect(MockMvcResultMatchers.jsonPath("$").isBoolean())
		.andExpect(MockMvcResultMatchers.jsonPath("$").value(false))
		;
	}
}
