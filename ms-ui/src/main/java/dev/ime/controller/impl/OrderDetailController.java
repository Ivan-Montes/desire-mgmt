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
import dev.ime.dto.OrderDetailDto;
import dev.ime.dtomvc.OrderDetailMvcDto;
import dev.ime.service.impl.OrderDetailService;
import dev.ime.service.impl.OrderService;
import dev.ime.service.impl.ProductService;
import dev.ime.tool.SomeConstants;
import jakarta.validation.Valid;


@Controller
@RequestMapping("/orderdetails")
public class OrderDetailController implements GenericMvcController<OrderDetailMvcDto> {

	private static final String REDIRECT_ORDERDETAILS = "redirect:/orderdetails";
	private final OrderDetailService orderDetailService;
	private final OrderService orderService;
	private final ProductService productService;
	

	public OrderDetailController(OrderDetailService orderDetailService, OrderService orderService,
			ProductService productService) {
		super();
		this.orderDetailService = orderDetailService;
		this.orderService = orderService;
		this.productService = productService;
	}

	@Override
	@GetMapping
	public String getAll(Model model, 
			@RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer size) {
		
		List<OrderDetailDto>list;
		
		if ( ( page != null && page >= 0 ) && ( size != null && size >= 1 ) ) {
			list = orderDetailService.getAllPaged(page, size);
		}
		else if ( page != null && page >= 0 ) {
			list = orderDetailService.getAllPaged(page, SomeConstants.SIZE_REQUEST);
		}
		else  {
			list = orderDetailService.getAll();			
		}
		
		model.addAttribute("orderDetailList", list);
		
		return "sections/orderdetails/index";
	}

	@Override
	@GetMapping("/{id}")
	public String getById(Model model, @PathVariable Long id) {

		model.addAttribute("orderDetail", orderDetailService.getById(id));
		model.addAttribute("orderList", orderService.getAll());
		model.addAttribute("productList", productService.getAll());
		
		return "sections/orderdetails/edit";
	}

	@Override
	@GetMapping("/add")
	public String add(Model model) {
		
		model.addAttribute("newOrderDetail", new OrderDetailMvcDto());
		model.addAttribute("orderList", orderService.getAll());
		model.addAttribute("productList", productService.getAll());
		
		return "sections/orderdetails/add";
	}

	@Override
	@PostMapping("/create")
	public String create(Model model, @Valid @ModelAttribute("newOrderDetail") OrderDetailMvcDto mvcDto) {
		
		model.addAttribute("saveOrderDetail", orderDetailService.create( mvcDto ) );
		
		return REDIRECT_ORDERDETAILS;
	}

	@Override
	@PostMapping("/update/{id}")
	public String update(Model model, @PathVariable Long id, @Valid @ModelAttribute("newOrderDetail") OrderDetailMvcDto mvcDto) {
		
		model.addAttribute("saveOrderDetail", orderDetailService.update( id, mvcDto ) );
		
		return REDIRECT_ORDERDETAILS;
	}

	@Override
	@GetMapping("/confirm-delete/{id}")
	public String confirmDelete(Model model, @PathVariable Long id) {
		
		OrderDetailDto orderDetailDto = orderDetailService.getById(id);
		model.addAttribute("orderDetail", orderDetailDto);
		model.addAttribute("product", productService.getById(orderDetailDto.productId()));
		model.addAttribute("order", orderService.getById(orderDetailDto.orderId()));
		
		return "sections/orderdetails/delete";
	}

	@Override
	@GetMapping("/delete/{id}")
	public String delete(Model model, @PathVariable Long id) {
		
		orderDetailService.delete(id);
		
		return REDIRECT_ORDERDETAILS;
	}
	
	
}
