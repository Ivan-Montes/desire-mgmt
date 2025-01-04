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
import dev.ime.dto.CategoryDto;
import dev.ime.dtomvc.CategoryMvcDto;
import dev.ime.service.impl.CategoryService;
import dev.ime.tool.SomeConstants;
import jakarta.validation.Valid;


@Controller
@RequestMapping("/categories")
public class CategoryController implements GenericMvcController<CategoryMvcDto>{

	private static final String REDIRECT_CATEGORIES = "redirect:/categories";
	private final CategoryService categoryService;
	
	
	public CategoryController(CategoryService categoryService) {
		super();
		this.categoryService = categoryService;
	}

	@Override
	@GetMapping
	public String getAll(Model model, 
			@RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer size) {
		
		List<CategoryDto>list;
		
		if ( ( page != null && page >= 0 ) && ( size != null && size >= 1 ) ) {
			list = categoryService.getAllPaged(page, size);
		}
		else if ( page != null && page >= 0 ) {
			list = categoryService.getAllPaged(page, SomeConstants.SIZE_REQUEST);
		}
		else  {
			list = categoryService.getAll();			
		}
		
		model.addAttribute("categoryList", list);
		
		return "sections/categories/index";
	}
	

	@Override
	@GetMapping("/{id}")
	public String getById(Model model, @PathVariable Long id) {
		
		model.addAttribute("category", categoryService.getById(id));
		
		return "sections/categories/edit";
	}

	@Override
	@GetMapping("/add")
	public String add(Model model) {
		
		model.addAttribute("newCategory", new CategoryMvcDto());
		
		return "sections/categories/add";
	}

	@Override
	@PostMapping("/create")
	public String create(Model model, @Valid @ModelAttribute("newCategory") CategoryMvcDto mvcDto) {
		
		model.addAttribute("saveCategory", categoryService.create( mvcDto ) );
		
		return REDIRECT_CATEGORIES;
	}

	@Override
	@PostMapping("/update/{id}")
	public String update(Model model, @PathVariable Long id, @Valid @ModelAttribute("newCategory") CategoryMvcDto mvcDto) {
		
		model.addAttribute("saveCategory", categoryService.update( id, mvcDto ) );
		
		return REDIRECT_CATEGORIES;
	}

	@Override
	@GetMapping("/confirm-delete/{id}")
	public String confirmDelete(Model model, @PathVariable Long id) {
		
		model.addAttribute("category", categoryService.getById(id));
		
		return "sections/categories/delete";
	}

	@Override
	@GetMapping("/delete/{id}")
	public String delete(Model model, @PathVariable Long id) {
		
		categoryService.delete(id);
		
		return REDIRECT_CATEGORIES;
	}
	
}
