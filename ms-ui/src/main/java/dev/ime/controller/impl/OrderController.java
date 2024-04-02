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
import dev.ime.dto.OrderDto;
import dev.ime.dtomvc.OrderMvcDto;
import dev.ime.service.impl.CustomerService;
import dev.ime.service.impl.OrderService;
import dev.ime.tool.SomeConstants;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/orders")
public class OrderController implements GenericMvcController<OrderMvcDto> {

	private static final String REDIRECT_ORDERS = "redirect:/orders";
	private final OrderService orderService;
	private final CustomerService customerService;
	
	public OrderController(OrderService orderService, CustomerService customerService) {
		super();
		this.orderService = orderService;
		this.customerService = customerService;
	}

	@Override
	@GetMapping
	public String getAll(Model model, 
			@RequestParam( value="page", required = false) Integer page,
			@RequestParam( value="size", required = false) Integer size) {
		
		List<OrderDto>list;
		
		if ( ( page != null && page >= 0 ) && ( size != null && size >= 1 ) ) {
			list = orderService.getAllPaged(page, size);
		}
		else if ( page != null && page >= 0 ) {
			list = orderService.getAllPaged(page, SomeConstants.SIZE_REQUEST);
		}
		else  {
			list = orderService.getAll();			
		}
		
		model.addAttribute("orderList", list);
		
		return "sections/orders/index";
	}

	@Override
	@GetMapping("/{id}")
	public String getById(Model model, @PathVariable Long id) {
		
		model.addAttribute("order", orderService.getById(id));
		model.addAttribute("customerList", customerService.getAll());
		
		return "sections/orders/edit";
	}

	@Override
	@GetMapping("/add")
	public String add(Model model) {
		
		model.addAttribute("newOrder", new OrderMvcDto());
		model.addAttribute("customerList", customerService.getAll());
		
		return "sections/orders/add";
	}

	@Override
	@PostMapping("/create")
	public String create(Model model, @Valid @ModelAttribute("newOrder") OrderMvcDto mvcDto) {

		model.addAttribute("saveOrder", orderService.create( mvcDto ) );
		
		return REDIRECT_ORDERS;
	}

	@Override
	@PostMapping("/update/{id}")
	public String update(Model model, @PathVariable Long id, @Valid @ModelAttribute("newOrder") OrderMvcDto mvcDto) {

		model.addAttribute("saveOrder", orderService.update( id, mvcDto ) );
		
		return REDIRECT_ORDERS;
	}

	@Override
	@GetMapping("/confirm-delete/{id}")
	public String confirmDelete(Model model, @PathVariable Long id) {
		
		OrderDto orderDto = orderService.getById(id);		
		model.addAttribute("order", orderDto);
		model.addAttribute("customer", customerService.getById(orderDto.customerId()));
		
		return "sections/orders/delete";
	}

	@Override
	@GetMapping("/delete/{id}")
	public String delete(Model model, @PathVariable Long id) {
		
		orderService.delete(id);
		
		return REDIRECT_ORDERS;
	}

	
}
