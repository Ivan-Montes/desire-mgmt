package dev.ime.resource.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.ime.dto.AddressDto;
import dev.ime.dto.CustomerDto;
import dev.ime.entity.Customer;
import dev.ime.mapper.impl.AddressMapper;
import dev.ime.mapper.impl.CustomerMapper;
import dev.ime.resource.CustomerSpecificResource;
import dev.ime.resource.GenericResource;
import dev.ime.service.impl.CustomerServiceImpl;
import dev.ime.tool.SomeConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/customers")
@Tag(name = "Customer", description="Customer Operations")
public class CustomerResource implements GenericResource<CustomerDto>, CustomerSpecificResource{

	private final CustomerServiceImpl customerService;
	private final CustomerMapper customerMapper;
	private final AddressMapper addressMapper;
	
	public CustomerResource(CustomerServiceImpl customerService, CustomerMapper customerMapper,
			AddressMapper addressMapper) {
		this.customerService = customerService;
		this.customerMapper = customerMapper;
		this.addressMapper = addressMapper;
	}

	@GetMapping
	@Operation(summary="Get a List of all Customer", description="Get a List of all Customer, @return an object Response with a List of DTO's")
	@Override
	public ResponseEntity<List<CustomerDto>> getAll(Integer page, Integer size) {
		
		List<Customer> list;
		
		if ( ( page != null && page >= 0 ) && ( size != null && size >= 1 ) ) {
			list = customerService.getAllPaged(page, size);
		}
		else if ( page != null && page >= 0 ) {
			list = customerService.getAllPaged(page, SomeConstants.SIZE_REQUEST);
		}
		else {
			list = customerService.getAll();
		}
		
		return list.isEmpty()? new ResponseEntity<>(Collections.emptyList(), HttpStatus.NO_CONTENT):
								new ResponseEntity<>(customerMapper.toListDto(list), HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	@Operation(summary="Get a Customer according to an Id", description="Get a Customer according to an Id, @return an object Response with the entity required in a DTO")
	@Override
	public ResponseEntity<CustomerDto> getById(@PathVariable Long id) {
		
		Optional<Customer> opt = customerService.getById(id);
		
		return opt.isPresent()? new ResponseEntity<>(customerMapper.toDto(opt.get()), HttpStatus.OK)
								: new ResponseEntity<>(new CustomerDto(), HttpStatus.NOT_FOUND);
	}

	@PostMapping
	@Operation(summary="Create a new Customer", description="Create a new Customer, @return an object Response with the entity in a DTO")
	@Override
	public ResponseEntity<CustomerDto> create(@Valid @RequestBody CustomerDto entity) {
		
		Optional<Customer>opt = customerService.create(entity);
		
		return opt.isPresent()? new ResponseEntity<>(customerMapper.toDto(opt.get()),HttpStatus.CREATED)
							: new ResponseEntity<>(new CustomerDto(), HttpStatus.NOT_FOUND);
		
	}
	
	@PutMapping("/{id}")
	@Operation(summary="Update fields in a Customer", description="Update fields in a Customer, @return an object Response with the entity modified in a DTO")
	@Override
	public ResponseEntity<CustomerDto> update(@PathVariable Long id, @Valid @RequestBody CustomerDto entity) {
		
		Optional<Customer>opt = customerService.update(id, entity);
		
		return opt.isPresent()? new ResponseEntity<>(customerMapper.toDto(opt.get()),HttpStatus.OK)
				: new ResponseEntity<>(new CustomerDto(), HttpStatus.NOT_FOUND);
	}
	
	@DeleteMapping("/{id}")
	@Operation(summary="Delete a Customer by its Id", description="Delete a Customer by its Id, @return an object Response with a message")
	@Override
	public ResponseEntity<Boolean> delete(@PathVariable Long id) {
		
		return customerService.delete(id) == 0 ? new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK)
				:new ResponseEntity<>(Boolean.FALSE, HttpStatus.NOT_FOUND);
	}
	
	@PutMapping("/{customerId}/addresses/{addressId}")
	@Operation(summary="Add an Address in a Customer", description="Add an Address in a Customer, @return an object Response with a message")
	@Override
	public ResponseEntity<Boolean> addAddress(@PathVariable Long customerId, @PathVariable Long addressId) {
				
		return Boolean.TRUE.equals(customerService.addAddress(customerId, addressId))? new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK)
																: new ResponseEntity<>(Boolean.FALSE,HttpStatus.NOT_FOUND);
	}

	@GetMapping("/{customerId}/addresses")
	@Operation(summary="Get addresses of a Customer", description="Get addresses of a Customer, AKA Address objects")
	@Override
	public ResponseEntity<List<AddressDto>> getAddressesInCustomer(@PathVariable Long customerId) {
		
		Optional<Customer> opt = customerService.getById(customerId);

		if (opt.isPresent() && !opt.get().getAddresses().isEmpty() ) {
			return new ResponseEntity<>(addressMapper.toListDto(opt.get().getAddresses()), HttpStatus.OK);
		}
		
		return new ResponseEntity<>(addressMapper.toListDto(Collections.emptyList()), HttpStatus.NO_CONTENT);
	}

	
}
