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
import dev.ime.entity.Address;
import dev.ime.mapper.impl.AddressMapper;
import dev.ime.resource.AddressSpecificResource;
import dev.ime.resource.GenericResource;
import dev.ime.service.impl.AddressServiceImpl;
import dev.ime.tool.SomeConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/addresses")
@Tag(name = "Address", description="Address Operations")
public class AddressResource implements GenericResource<AddressDto>, AddressSpecificResource{

	private final AddressServiceImpl addressService;
	private final AddressMapper addressMapper;
	
	
	public AddressResource(AddressServiceImpl addressService, AddressMapper addressMapper) {
		this.addressService = addressService;
		this.addressMapper = addressMapper;
	}

	@GetMapping
	@Operation(summary="Get a List of all Address", description="Get a List of all Address, @return an object Response with a List of DTO's")
	@Override
	public ResponseEntity<List<AddressDto>> getAll(Integer page, Integer size) {
		
		List<Address> list;
		
		if ( ( page != null && page >= 0 ) && ( size != null && size >= 1 ) ) {
			list = addressService.getAllPaged(page, size);
		}
		else if ( page != null && page >= 0 ) {
			list = addressService.getAllPaged(page, SomeConstants.SIZE_REQUEST);
		}
		else {
			list = addressService.getAll();
		}
		
		return list.isEmpty()? new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK):
								new ResponseEntity<>(addressMapper.toListDto(list), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	@Operation(summary="Get a Address according to an Id", description="Get a Address according to an Id, @return an object Response with the entity required in a DTO")
	@Override
	public ResponseEntity<AddressDto> getById(@PathVariable Long id) {
		Optional<Address> opt = addressService.getById(id);
		
		return opt.isPresent()? new ResponseEntity<>(addressMapper.toDto(opt.get()), HttpStatus.OK)
								: new ResponseEntity<>(new AddressDto(), HttpStatus.OK);
	}

	@PostMapping
	@Operation(summary="Create a new Address", description="Create a new Address, @return an object Response with the entity in a DTO")
	@Override
	public ResponseEntity<AddressDto> create(@Valid @RequestBody AddressDto entity) {
		Optional<Address>opt = addressService.create(entity);
		
		return opt.isPresent()? new ResponseEntity<>(addressMapper.toDto(opt.get()),HttpStatus.CREATED)
							: new ResponseEntity<>(new AddressDto(), HttpStatus.OK);
	}

	@PutMapping("/{id}")
	@Operation(summary="Update fields in a Address", description="Update fields in a Address, @return an object Response with the entity modified in a DTO")
	@Override
	public ResponseEntity<AddressDto> update(@PathVariable Long id, @Valid @RequestBody AddressDto entity) {
		Optional<Address>opt = addressService.update(id, entity);
		
		return opt.isPresent()? new ResponseEntity<>(addressMapper.toDto(opt.get()),HttpStatus.OK)
				: new ResponseEntity<>(new AddressDto(), HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	@Operation(summary="Delete a Address by its Id", description="Delete a Address by its Id, @return an object Response with a message")
	@Override
	public ResponseEntity<Boolean> delete(@PathVariable Long id) {
		return addressService.delete(id) == 0 ? new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK)
				:new ResponseEntity<>(Boolean.FALSE, HttpStatus.OK);
	}
	
	@PutMapping("/{addressId}/customers/{customerId}")
	@Operation(summary="Set in an Address in a Customer", description="Set in an Address in a Customer, @return an object Response with a message")
	@Override
	public ResponseEntity<Boolean> setCustomer(@PathVariable Long addressId, @PathVariable Long customerId) {
		
		return Boolean.TRUE.equals(addressService.setCustomer(addressId, customerId)) ? 
															new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK)
															:new ResponseEntity<>(Boolean.FALSE,HttpStatus.OK);
	}

}
