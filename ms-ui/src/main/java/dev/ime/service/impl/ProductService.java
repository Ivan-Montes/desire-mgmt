package dev.ime.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import dev.ime.client.impl.MsProductsClientImpl;
import dev.ime.dto.ProductDto;
import dev.ime.dtomvc.ProductMvcDto;
import dev.ime.exception.EntityAssociatedException;
import dev.ime.exception.ResourceNotFoundException;
import dev.ime.mapper.impl.ProductMvcMapper;
import dev.ime.service.GenericService;
import dev.ime.service.ProductSpecificService;
import dev.ime.tool.SomeConstants;


@Service
public class ProductService implements GenericService<ProductMvcDto, ProductDto>, ProductSpecificService{

	private final MsProductsClientImpl msProductsClient;	
	private final ProductMvcMapper productMvcMapper;
	
	public ProductService(MsProductsClientImpl msProductsClient, ProductMvcMapper productMvcMapper) {
		super();
		this.msProductsClient = msProductsClient;
		this.productMvcMapper = productMvcMapper;
	}

	@Override
	public List<ProductDto> getAll() {
		
		return  msProductsClient.getAllProduct().getBody();
	}

	@Override
	public List<ProductDto> getAllPaged(Integer pageNumber, Integer pageSize) {
		
		return  msProductsClient.getAllProductPaged(pageNumber, pageSize).getBody();
	}

	@Override
	public ProductDto getById(Long id) {		
		
		return  Optional.ofNullable(msProductsClient.getProductById(id))
				.filter( r -> r.getStatusCode().equals(HttpStatus.OK))
				.map(ResponseEntity::getBody)
				.filter( p -> p.productId() > 0 )
				.orElseThrow( ()-> new ResourceNotFoundException(Map.of(SomeConstants.PRODUCTID, String.valueOf(id))));
	}

	@Override
	public ProductDto create(ProductMvcDto dto) {
		
		return Optional.ofNullable(msProductsClient.create(productMvcMapper.fromMvcDtoToDto(dto)))
				.filter( r -> r.getStatusCode().equals(HttpStatus.CREATED))
				.map(ResponseEntity::getBody)
				.filter( p -> p.productId() > 0 )
				.orElseThrow( () -> new ResourceNotFoundException(Map.of( SomeConstants.DATELESS, dto.toString())));
	}

	@Override
	public ProductDto update(Long id, ProductMvcDto dto) {
		
		return Optional.ofNullable(msProductsClient.update(id, productMvcMapper.fromMvcDtoToDto(dto)))
				.filter( r -> r.getStatusCode().equals(HttpStatus.OK))
				.map(ResponseEntity::getBody)
				.filter( p -> p.productId().equals(id) )
				.orElseThrow( () -> new ResourceNotFoundException(Map.of( SomeConstants.PRODUCTID, String.valueOf(id))));
	}

	@Override
	public Boolean delete(Long id) {
		
		return Optional.ofNullable(msProductsClient.deleteProduct(id))
				.filter( r -> r.getStatusCode().equals(HttpStatus.OK))
				.map(ResponseEntity::getBody)
				.orElseThrow( () -> new EntityAssociatedException(Map.of( SomeConstants.PRODUCTID, String.valueOf(id))));
	}

	@Override
	public Boolean changeCategory(Long productId, Long categoryId) {
		
		return Optional.ofNullable(msProductsClient.changeCategory(productId, categoryId))
				.filter( r -> r.getStatusCode().equals(HttpStatus.OK))
				.map(ResponseEntity::getBody)
				.orElseThrow( () -> new ResourceNotFoundException(Map.of( SomeConstants.PRODUCTID, String.valueOf(productId), 
																		  SomeConstants.CATEGORYID, String.valueOf(categoryId))));		
	}

}
