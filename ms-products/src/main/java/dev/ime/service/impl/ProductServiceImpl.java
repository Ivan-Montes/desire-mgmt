package dev.ime.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import dev.ime.dto.ProductDto;
import dev.ime.entity.Category;
import dev.ime.entity.Product;
import dev.ime.exception.AttributeUniqueException;
import dev.ime.exception.ResourceNotFoundException;
import dev.ime.mapper.ProductMapper;
import dev.ime.repository.CategoryRepository;
import dev.ime.repository.ProductRepository;
import dev.ime.service.GenericService;
import dev.ime.service.ProductSpecificService;
import dev.ime.tool.SomeConstants;

@Service
public class ProductServiceImpl implements GenericService<Product, ProductDto>, ProductSpecificService{

	private final ProductRepository productRepo;
	private final ProductMapper productMapper;
	private final CategoryRepository categoryRepo;
	
	public ProductServiceImpl(ProductRepository productRepo, ProductMapper productMapper, CategoryRepository categoryRepo) {
		this.productRepo = productRepo;
		this.productMapper = productMapper;
		this.categoryRepo = categoryRepo;
	}

	@Override
	public List<Product> getAllPaged(Integer pageNumber, Integer pageSize) {
		return productRepo.findAll(PageRequest.of(pageNumber, pageSize)).toList();
	}

	@Override
	public List<Product> getAll() {		
		return productRepo.findAll();
	}

	@Override
	public Optional<Product> getById(Long id) {
		return Optional.ofNullable( productRepo.findById(id).orElseThrow( () -> new ResourceNotFoundException( Map.of( SomeConstants.PRODUCTID, String.valueOf(id) ) ) ) );
	}

	@Override
	public Optional<Product> create(ProductDto entity) {
		
		if ( !productRepo.findByName(entity.name()).isEmpty() ) throw new AttributeUniqueException(Map.of(SomeConstants.NAMEATTR, entity.name()));
		Product p = productMapper.fromDto(entity);		
		Category cat =  categoryRepo.findById(entity.categoryId()).orElseThrow( () -> new ResourceNotFoundException( Map.of( SomeConstants.CATEGORYID, String.valueOf(entity.categoryId()) ) ) );
		p.setCategory(cat);
		
		return Optional.ofNullable(productRepo.save(p));
	}

	@Override
	public Optional<Product> update(Long id, ProductDto entity) {
		
		Product pro = productRepo.findById(id).orElseThrow( () -> new ResourceNotFoundException( Map.of( SomeConstants.PRODUCTID, String.valueOf(id) ) ) );
		Category cat =  categoryRepo.findById(entity.categoryId()).orElseThrow( () -> new ResourceNotFoundException( Map.of( SomeConstants.CATEGORYID, String.valueOf(entity.categoryId()) ) ) );
		List<Product>list = productRepo.findByName(entity.name());
		boolean checkList = list.stream().anyMatch( p -> p.getId().equals(id));
		
		if ( list.isEmpty() || checkList ) {
			
			pro.setName(entity.name());
			pro.setUnitInStock(entity.unitInStock());
			pro.setUnitPrice(entity.unitPrice());
			pro.setDiscontinued(entity.discontinued());			
			pro.setCategory(cat);
			
			return Optional.ofNullable(productRepo.save(pro));
			
		}else{
			throw new AttributeUniqueException(Map.of(SomeConstants.NAMEATTR, entity.name()));
		}
		
	}

	@Override
	public Integer delete(Long id) {
		
		productRepo.deleteById(id);		
		Optional<Product>opt = productRepo.findById(id);
		
		return opt.isEmpty()?0:1;
	}

	@Override
	public Boolean changeCategory(Long productId, Long categoryId) {
		
		Product pro = productRepo.findById(productId).orElseThrow( () -> new ResourceNotFoundException( Map.of( SomeConstants.PRODUCTID, String.valueOf(productId) ) ) );
		Category cat = categoryRepo.findById(categoryId).orElseThrow( () -> new ResourceNotFoundException( Map.of( SomeConstants.CATEGORYID, String.valueOf(categoryId) ) ) );		
		pro.setCategory(cat);
		cat.getProducts().add(pro);
		Optional<Product> opt  = Optional.ofNullable(productRepo.save(pro));
		
		return opt.isPresent();
	}


}
