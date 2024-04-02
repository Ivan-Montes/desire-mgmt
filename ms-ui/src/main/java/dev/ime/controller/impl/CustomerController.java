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
import dev.ime.dto.CustomerDto;
import dev.ime.dtomvc.CustomerMvcDto;
import dev.ime.service.impl.CustomerService;
import dev.ime.tool.SomeConstants;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/customers")
public class CustomerController implements GenericMvcController<CustomerMvcDto> {

	private static final String REDIRECT_CUSTOMERS = "redirect:/customers";
	private final CustomerService customerService;
	
	
	public CustomerController(CustomerService customerService) {
		super();
		this.customerService = customerService;
	}


	@Override
	@GetMapping
	public String getAll(Model model, 
			@RequestParam( value="page", required = false) Integer page,
			@RequestParam( value="size", required = false) Integer size) {
		
		List<CustomerDto>list;
		
		if ( ( page != null && page >= 0 ) && ( size != null && size >= 1 ) ) {
			list = customerService.getAllPaged(page, size);
		}
		else if ( page != null && page >= 0 ) {
			list = customerService.getAllPaged(page, SomeConstants.SIZE_REQUEST);
		}
		else  {
			list = customerService.getAll();			
		}
		
		model.addAttribute("customerList", list);
		
		return "sections/customers/index";
	}


	@Override
	@GetMapping("/{id}")
	public String getById(Model model, @PathVariable Long id) {

		model.addAttribute("customer", customerService.getById(id));
		
		return "sections/customers/edit";
	}


	@Override
	@GetMapping("/add")
	public String add(Model model) {
		
		model.addAttribute("newCustomer", new CustomerMvcDto());
		
		return "sections/customers/add";
	}


	@Override
	@PostMapping("/create")
	public String create(Model model, @Valid @ModelAttribute("newCustomer") CustomerMvcDto mvcDto) {
		
		model.addAttribute("saveCustomer", customerService.create(mvcDto));
		
		return REDIRECT_CUSTOMERS;
	}


	@Override
	@PostMapping("/update/{id}")
	public String update(Model model, @PathVariable Long id, @Valid @ModelAttribute("newCustomer") CustomerMvcDto mvcDto) {
		
		model.addAttribute("saveCustomer", customerService.update(id, mvcDto));
		
		return REDIRECT_CUSTOMERS;
	}


	@Override
	@GetMapping("/confirm-delete/{id}")
	public String confirmDelete(Model model, @PathVariable Long id) {
		
		model.addAttribute("customer", customerService.getById(id));
		
		return "sections/customers/delete";
	}


	@Override
	@GetMapping("/delete/{id}")
	public String delete(Model model, @PathVariable Long id) {
		
		customerService.delete(id);
		
		return REDIRECT_CUSTOMERS;
	}
	
}
