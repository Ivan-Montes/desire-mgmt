package dev.ime.controller.impl;


import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import dev.ime.controller.GenericMvcController;
import dev.ime.dto.ProductDto;
import dev.ime.dtomvc.ProductMvcDto;
import dev.ime.service.impl.CategoryService;
import dev.ime.service.impl.ProductService;
import dev.ime.tool.SomeConstants;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/products")
public class ProductController implements GenericMvcController<ProductMvcDto>{
	
	private static final String REDIRECT_PRODUCTS = "redirect:/products";
	private final ProductService productService;
	private final CategoryService categoryService;
	
	public ProductController(ProductService productService, CategoryService categoryService) {
		super();
		this.productService = productService;
		this.categoryService = categoryService;
	}

	@Override
	@GetMapping
	public String getAll(Model model, 
			@RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer size) {
		
		List<ProductDto>list;
		
		if ( ( page != null && page >= 0 ) && ( size != null && size >= 1 ) ) {
			list = productService.getAllPaged(page, size);
		}
		else if ( page != null && page >= 0 ) {
			list = productService.getAllPaged(page, SomeConstants.SIZE_REQUEST);
		}
		else  {
			list = productService.getAll();			
		}
		
		model.addAttribute("productList", list);
		
		return "sections/products/index";
	}

	@Override
	@GetMapping("/{id}")
	public String getById(Model model, @PathVariable Long id) {
		
		model.addAttribute("product", productService.getById(id));
		model.addAttribute("categoryList", categoryService.getAll());
		
		return "sections/products/edit";
	}

	@Override
	@GetMapping("/add")
	public String add(Model model) {	
		
		model.addAttribute("newProduct", new ProductMvcDto());
		model.addAttribute("categoryList", categoryService.getAll());
		
		return "sections/products/add";
	}

	@Override
	@PostMapping("/create")
	public String create(Model model, @Valid @ModelAttribute("newProduct") ProductMvcDto mvcDto ) {
		
		model.addAttribute("saveProduct", productService.create( mvcDto ) );
		
		return REDIRECT_PRODUCTS;
	}

	@Override
	@PostMapping("/update/{id}")
	public String update(Model model, @PathVariable Long id, @Valid @ModelAttribute("newProduct") ProductMvcDto mvcDto ) {
		
		model.addAttribute("saveProduct", productService.update( id, mvcDto ) );
		
		return REDIRECT_PRODUCTS;
	}

	@Override
	@GetMapping("/confirm-delete/{id}")
	public String confirmDelete(Model model, @PathVariable Long id) {	
		
		model.addAttribute("product", productService.getById(id));
		
		return "sections/products/delete";
		
	}

	@Override
	@GetMapping("/delete/{id}")
	public String delete(Model model, @PathVariable Long id) {	
		
		productService.delete(id);
		
		return REDIRECT_PRODUCTS;
		
	}	
	
}
