package dev.ime.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import dev.ime.dto.CategoryDto;
import dev.ime.entity.Category;
import dev.ime.entity.Product;
import dev.ime.exception.*;
import dev.ime.repository.CategoryRepository;
import dev.ime.repository.ProductRepository;
import dev.ime.service.CategorySpecificService;
import dev.ime.service.GenericService;
import dev.ime.tool.SomeConstants;

@Service
public class CategoryServiceImpl implements GenericService<Category, CategoryDto>, CategorySpecificService {

	private final CategoryRepository categoryRepo;
	private final ProductRepository productRepo;
	
	public CategoryServiceImpl(CategoryRepository categoryRepo, ProductRepository productRepo) {
		this.categoryRepo = categoryRepo;
		this.productRepo = productRepo;
	}

	@Override
	public List<Category> getAllPaged(Integer pageNumber, Integer pageSize) {
		return categoryRepo.findAll(PageRequest.of(pageNumber, pageSize)).toList();
	}

	@Override
	public List<Category> getAll() {
		return categoryRepo.findAll(Sort.by("id"));
	}

	@Override
	public Optional<Category> getById(Long id) {
		return Optional.ofNullable( categoryRepo.findById(id).orElseThrow( () -> new ResourceNotFoundException(Map.of(SomeConstants.CATEGORYID, String.valueOf(id) ) ) ) );
	}

	@Override
	public Optional<Category> create(CategoryDto entity) {
		
		if ( !categoryRepo.findByName(entity.name()).isEmpty() ) throw new AttributeUniqueException(Map.of(SomeConstants.NAMEATTR, entity.name()));
		Category category = updateCategoryFields(new Category(), entity);
		
		return Optional.ofNullable(categoryRepo.save(category));
		
	}

	private Category updateCategoryFields(Category category, CategoryDto entity) {
		
		category.setName(entity.name());
		category.setDescription(entity.description());
		
		return category;
	}
	
	@Override
	public Optional<Category> update(Long id, CategoryDto entity) {
		
		Category cat = searchByCategoryId(id);
		List<Category>list = categoryRepo.findByName(entity.name());
		boolean checkList = list.stream().anyMatch( c -> c.getId().equals(id));
		
		if ( list.isEmpty() || checkList ) {
			
			return Optional.ofNullable( categoryRepo.save(updateCategoryFields(cat, entity)) );
			
		}else {
			throw new AttributeUniqueException(Map.of(SomeConstants.NAMEATTR, entity.name()));
		}
	}

	@Override
	public Integer delete(Long id) {
		
		Category cat = searchByCategoryId(id);
				
		if (  cat.getProducts().isEmpty() ) {
			
			categoryRepo.deleteById(id);
			return categoryRepo.findById(id).isEmpty()?0:1;
			
		}else {
				
			throw new EntityAssociatedException( Map.of( SomeConstants.CATEGORYID, String.valueOf(id) ) );
		}		
		
	}

	@Override
	public Boolean addProductToCategory(Long categoryId, Long productId) {

		Product pro = productRepo.findById(productId).orElseThrow( () -> new ResourceNotFoundException( Map.of( SomeConstants.PRODUCTID, String.valueOf(productId) ) ) );
		Category cat = searchByCategoryId(categoryId);
		pro.setCategory(cat);
		cat.getProducts().add(pro);
		Optional<Category> opt =  Optional.ofNullable( categoryRepo.save( cat ));
		
		return opt.isPresent();
	}

	private Category searchByCategoryId(Long categoryId) {
		
		return categoryRepo.findById(categoryId).orElseThrow( () -> new ResourceNotFoundException( Map.of( SomeConstants.CATEGORYID, String.valueOf(categoryId) ) ) );
				
	}

}
