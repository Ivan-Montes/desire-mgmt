package dev.ime.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;

import dev.ime.client.impl.MsProductsClientImpl;
import dev.ime.dto.CategoryDto;
import dev.ime.dtomvc.CategoryMvcDto;
import dev.ime.exception.EntityAssociatedException;
import dev.ime.exception.ResourceNotFoundException;
import dev.ime.mapper.impl.CategoryMvcMapper;
import dev.ime.service.CategorySpecificService;
import dev.ime.service.GenericService;
import dev.ime.tool.SomeConstants;

@Service
public class CategoryService implements GenericService<CategoryMvcDto, CategoryDto>, CategorySpecificService {
	
	private final MsProductsClientImpl msProductsClient;	
	private final CategoryMvcMapper categoryMvcMapper;
	
	public CategoryService(MsProductsClientImpl msProductsClient, CategoryMvcMapper categoryMvcMapper) {
		super();
		this.msProductsClient = msProductsClient;
		this.categoryMvcMapper = categoryMvcMapper;
	}

	@Override
	public List<CategoryDto> getAll() {
		
		return msProductsClient.getAllCategory().getBody();
	}

	@Override
	public List<CategoryDto> getAllPaged(Integer pageNumber, Integer pageSize) {
		
		return msProductsClient.getAllCategoryPaged(pageNumber, pageSize).getBody();
	}

	@Override
	public CategoryDto getById(Long id) {
		
		return Optional.ofNullable(msProductsClient.getCategoryById(id))
				.filter( r -> r.getStatusCode().equals(HttpStatus.OK))
				.map(ResponseEntity::getBody)
				.filter( c -> c.categoryId() > 0 )
				.orElseThrow( () -> new ResourceNotFoundException( Map.of( SomeConstants.CATEGORYID, String.valueOf(id))));
	}

	@Override
	public CategoryDto create(CategoryMvcDto dto) {
		
		return Optional.ofNullable(msProductsClient.create(categoryMvcMapper.fromMvcDtoToDto(dto)))
				.filter( r -> r.getStatusCode().equals(HttpStatus.CREATED))
				.map(ResponseEntity::getBody)
				.filter( c -> c.categoryId() > 0 )
				.orElseThrow( () -> new ResourceNotFoundException( Map.of( SomeConstants.DATELESS, dto.toString())));
	}

	@Override
	public CategoryDto update(Long id, CategoryMvcDto dto) {
		
		return Optional.ofNullable(msProductsClient.update(id, categoryMvcMapper.fromMvcDtoToDto(dto)))
				.filter( r -> r.getStatusCode().equals(HttpStatus.OK))
				.map(ResponseEntity::getBody)
				.filter( c -> c.categoryId().equals(id))
				.orElseThrow( () -> new ResourceNotFoundException( Map.of( SomeConstants.CATEGORYID, String.valueOf(id))));
	}

	@Override
	public Boolean delete(Long id) {
		
		return Optional.ofNullable(msProductsClient.deleteCategory(id))
				.filter( r -> r.getStatusCode().equals(HttpStatus.OK))
				.map(ResponseEntity::getBody)
				.orElseThrow( () -> new EntityAssociatedException(Map.of( SomeConstants.CATEGORYID, String.valueOf(id))));
	}

	@Override
	public Boolean addProductToCategory(Long categoryId, Long productId) {
		
		return Optional.ofNullable(msProductsClient.addProductToCategory(categoryId, productId))
				.filter( r -> r.getStatusCode().equals(HttpStatus.OK))
				.map(ResponseEntity::getBody)
				.orElseThrow( () -> new ResourceNotFoundException(Map.of( SomeConstants.PRODUCTID, String.valueOf(productId), 
																		  SomeConstants.CATEGORYID, String.valueOf(categoryId))));		
	}

}
