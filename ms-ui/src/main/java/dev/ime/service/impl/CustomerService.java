package dev.ime.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import dev.ime.client.impl.MsCustomersClientImpl;
import dev.ime.dto.CustomerDto;
import dev.ime.dtomvc.CustomerMvcDto;
import dev.ime.exception.EntityAssociatedException;
import dev.ime.exception.ResourceNotFoundException;
import dev.ime.mapper.impl.CustomerMvcMapper;
import dev.ime.service.CustomerSpecificService;
import dev.ime.service.GenericService;
import dev.ime.tool.SomeConstants;

@Service
public class CustomerService implements GenericService<CustomerMvcDto, CustomerDto>, CustomerSpecificService {

	private final MsCustomersClientImpl msCustomersClient;	
	private final CustomerMvcMapper customerMvcMapper;
	
	public CustomerService(MsCustomersClientImpl msCustomersClient, CustomerMvcMapper customerMvcMapper) {
		super();
		this.msCustomersClient = msCustomersClient;
		this.customerMvcMapper = customerMvcMapper;
	}

	@Override
	public List<CustomerDto> getAll() {
		
		return msCustomersClient.getAllCustomer().getBody();
		
	}

	@Override
	public List<CustomerDto> getAllPaged(Integer pageNumber, Integer pageSize) {
		
		return msCustomersClient.getAllCustomerPaged(pageNumber, pageSize).getBody();
		
	}

	@Override
	public CustomerDto getById(Long id) {
		
		return  Optional.ofNullable(msCustomersClient.getCustomerById(id))
				.filter( r -> r.getStatusCode().equals(HttpStatus.OK))
				.map(ResponseEntity::getBody)
				.filter( c -> c.customerId() > 0 )
				.orElseThrow( ()-> new ResourceNotFoundException(Map.of(SomeConstants.CUSTOMERID, String.valueOf(id))));
	}

	@Override
	public CustomerDto create(CustomerMvcDto dto) {

		return Optional.ofNullable(msCustomersClient.create(customerMvcMapper.fromMvcDtoToDto(dto)))
				.filter( r -> r.getStatusCode().equals(HttpStatus.CREATED))
				.map(ResponseEntity::getBody)
				.filter( c -> c.customerId() > 0 )
				.orElseThrow( () -> new ResourceNotFoundException(Map.of( SomeConstants.DATELESS, dto.toString())));
	}

	@Override
	public CustomerDto update(Long id, CustomerMvcDto dto) {

		return Optional.ofNullable(msCustomersClient.update(id, customerMvcMapper.fromMvcDtoToDto(dto)))
				.filter( r -> r.getStatusCode().equals(HttpStatus.OK))
				.map(ResponseEntity::getBody)
				.filter( c -> c.customerId().equals(id) )
				.orElseThrow( () -> new ResourceNotFoundException(Map.of( SomeConstants.CUSTOMERID, dto.toString())));
	}

	@Override
	public Boolean delete(Long id) {
		
		return Optional.ofNullable(msCustomersClient.deleteCustomer(id))
				.filter( r -> r.getStatusCode().equals(HttpStatus.OK))
				.map(ResponseEntity::getBody)
				.orElseThrow( () -> new EntityAssociatedException(Map.of( SomeConstants.CUSTOMERID, String.valueOf(id))));
	}

	@Override
	public Boolean addAddress(Long customerId, Long addressId) {

		return Optional.ofNullable(msCustomersClient.addAddress(customerId, addressId))
				.filter( r -> r.getStatusCode().equals(HttpStatus.OK))
				.map(ResponseEntity::getBody)
				.orElseThrow( () -> new ResourceNotFoundException(Map.of( SomeConstants.CUSTOMERID, String.valueOf(customerId), 
																		  SomeConstants.ADDRESSID, String.valueOf(addressId))));		
	}


}
