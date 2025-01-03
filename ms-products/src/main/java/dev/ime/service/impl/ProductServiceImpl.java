package dev.ime.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import dev.ime.dto.ProductDto;
import dev.ime.entity.Category;
import dev.ime.entity.Product;
import dev.ime.exception.AttributeUniqueException;
import dev.ime.exception.ResourceNotFoundException;
import dev.ime.repository.CategoryRepository;
import dev.ime.repository.ProductRepository;
import dev.ime.service.GenericService;
import dev.ime.service.ProductSpecificService;
import dev.ime.tool.Checker;
import dev.ime.tool.SomeConstants;

@Service
public class ProductServiceImpl implements GenericService<Product, ProductDto>, ProductSpecificService{

	private final ProductRepository productRepo;
	private final CategoryRepository categoryRepo;
	private final Checker checker;
	
	public ProductServiceImpl(ProductRepository productRepo, CategoryRepository categoryRepo, Checker checker) {
		this.productRepo = productRepo;
		this.categoryRepo = categoryRepo;
		this.checker = checker;
	}

	@Override
	public List<Product> getAllPaged(Integer pageNumber, Integer pageSize) {
		return productRepo.findAll(PageRequest.of(pageNumber, pageSize)).toList();
	}

	@Override
	public List<Product> getAll() {		
		return productRepo.findAll(Sort.by("id"));
	}

	@Override
	public Optional<Product> getById(Long id) {
		return Optional.ofNullable( productRepo.findById(id).orElseThrow( () -> new ResourceNotFoundException( Map.of( SomeConstants.PRODUCTID, String.valueOf(id) ) ) ) );
	}

	@Override
	public Optional<Product> create(ProductDto entity) {
		
		if ( !productRepo.findByName(entity.name()).isEmpty() ) throw new AttributeUniqueException(Map.of(SomeConstants.NAMEATTR, entity.name()));
		Category categoryFound =  categoryRepo.findById(entity.categoryId()).orElseThrow( () -> new ResourceNotFoundException( Map.of( SomeConstants.CATEGORYID, String.valueOf(entity.categoryId()) ) ) );
		Product p = updateProductFields(new Product(), entity, categoryFound);
		
		return Optional.ofNullable(productRepo.save(p));
	}

	private Product updateProductFields(Product product, ProductDto entity, Category category) {
		
		product.setName(entity.name());
		product.setUnitInStock(entity.unitInStock());
		product.setUnitPrice(entity.unitPrice());
		product.setDiscontinued(entity.discontinued());			
		product.setCategory(category);
		
		return product;
		
	}
	
	@Override
	public Optional<Product> update(Long id, ProductDto entity) {
		
		Product product = searchProductById(id);
		Category categoryFound = searchCategoryById(entity.categoryId());		
		List<Product>list = productRepo.findByName(entity.name());
		boolean checkList = list.stream().anyMatch( p -> p.getId().equals(id));
		
		if ( list.isEmpty() || checkList ) {			
			
			return Optional.ofNullable(productRepo.save(updateProductFields(product, entity, categoryFound)));
			
		}else{
			throw new AttributeUniqueException(Map.of(SomeConstants.NAMEATTR, entity.name()));
		}
		
	}

	private Product searchProductById(Long id) {
		
		return productRepo.findById(id).orElseThrow( () -> new ResourceNotFoundException( Map.of( SomeConstants.PRODUCTID, String.valueOf(id) ) ) );
		
	}

	@Override
	public Integer delete(Long id) {
		
		if ( checker.checkGetAnyByProductId(id) ) {
			return 1;
		}
		productRepo.deleteById(id);		
		Optional<Product>opt = productRepo.findById(id);
		
		return opt.isEmpty()?0:1;
	}

	@Override
	public Boolean changeCategory(Long productId, Long categoryId) {
		
		Product pro = searchProductById(productId);
		Category cat = searchCategoryById(categoryId);		
		pro.setCategory(cat);
		cat.getProducts().add(pro);
		Optional<Product> opt  = Optional.ofNullable(productRepo.save(pro));
		
		return opt.isPresent();
	}

	private Category searchCategoryById(Long categoryId) {
		
		return categoryRepo.findById(categoryId).orElseThrow( () -> new ResourceNotFoundException( Map.of( SomeConstants.CATEGORYID, String.valueOf(categoryId) ) ) );
		
	}


}
