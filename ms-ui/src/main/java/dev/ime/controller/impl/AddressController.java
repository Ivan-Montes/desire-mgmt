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
import dev.ime.dto.AddressDto;
import dev.ime.dtomvc.AddressMvcDto;
import dev.ime.service.impl.AddressService;
import dev.ime.service.impl.CustomerService;
import dev.ime.tool.SomeConstants;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/addresses")
public class AddressController implements GenericMvcController<AddressMvcDto>{

	private static final String REDIRECT_ADDRESSES = "redirect:/addresses";
	private final AddressService addressService;
	private final CustomerService customerService;	

	public AddressController(AddressService addressService, CustomerService customerService) {
		super();
		this.addressService = addressService;
		this.customerService = customerService;
	}

	@Override
	@GetMapping
	public String getAll(Model model, 
			@RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer size) {
		
		List<AddressDto>list;
		
		if ( ( page != null && page >= 0 ) && ( size != null && size >= 1 ) ) {
			list = addressService.getAllPaged(page, size);
		}
		else if ( page != null && page >= 0 ) {
			list = addressService.getAllPaged(page, SomeConstants.SIZE_REQUEST);
		}
		else  {
			list = addressService.getAll();			
		}
		
		model.addAttribute("addressList", list);
		
		return "sections/addresses/index";
	}


	@Override
	@GetMapping("/{id}")
	public String getById(Model model, @PathVariable Long id) {
		
		model.addAttribute("address", addressService.getById(id));
		model.addAttribute("customerList", customerService.getAll());
		
		return "sections/addresses/edit";
	}

	@Override
	@GetMapping("/add")
	public String add(Model model) {
		
		model.addAttribute("newAddress", new AddressMvcDto());
		model.addAttribute("customerList", customerService.getAll());
		
		return "sections/addresses/add";
	}

	@Override
	@PostMapping("/create")
	public String create(Model model, @Valid @ModelAttribute("newAddress") AddressMvcDto mvcDto) {
		
		model.addAttribute("saveAddress", addressService.create(mvcDto));
		
		return REDIRECT_ADDRESSES;
	}

	@Override
	@PostMapping("/update/{id}")
	public String update(Model model, @PathVariable Long id, @Valid @ModelAttribute("newAddress") AddressMvcDto mvcDto) {

		model.addAttribute("saveAddress", addressService.update(id, mvcDto));
		
		return REDIRECT_ADDRESSES;
	}

	@Override
	@GetMapping("/confirm-delete/{id}")
	public String confirmDelete(Model model, @PathVariable Long id) {

		AddressDto addressDto =  addressService.getById(id);
		model.addAttribute("address", addressDto);
		model.addAttribute("customer", customerService.getById(addressDto.customerId()));
		
		return "sections/addresses/delete";
	}

	@Override
	@GetMapping("/delete/{id}")
	public String delete(Model model, @PathVariable Long id) {
		
		addressService.delete(id);
		
		return REDIRECT_ADDRESSES;
	}
	
}
