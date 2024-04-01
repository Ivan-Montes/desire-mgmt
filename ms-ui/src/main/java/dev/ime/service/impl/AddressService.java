package dev.ime.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import dev.ime.client.impl.MsCustomersClientImpl;
import dev.ime.dto.AddressDto;
import dev.ime.dtomvc.AddressMvcDto;
import dev.ime.exception.EntityAssociatedException;
import dev.ime.exception.ResourceNotFoundException;
import dev.ime.mapper.impl.AddressMvcMapper;
import dev.ime.service.AddressSpecificService;
import dev.ime.service.GenericService;
import dev.ime.tool.SomeConstants;

@Service
public class AddressService implements GenericService<AddressMvcDto, AddressDto>, AddressSpecificService{

	private final MsCustomersClientImpl msCustomersClient;
	private final AddressMvcMapper addressMvcMapper;
	
	public AddressService(MsCustomersClientImpl msCustomersClient, AddressMvcMapper addressMvcMapper) {
		super();
		this.msCustomersClient = msCustomersClient;
		this.addressMvcMapper = addressMvcMapper;
	}

	@Override
	public List<AddressDto> getAll() {
		
		return msCustomersClient.getAllAddress().getBody();
	}

	@Override
	public List<AddressDto> getAllPaged(Integer pageNumber, Integer pageSize) {
		
		return msCustomersClient.getAllAddressPaged(pageNumber, pageSize).getBody();
	}

	@Override
	public AddressDto getById(Long id) {
		
		return  Optional.ofNullable(msCustomersClient.getAddressById(id))
				.filter( r -> r.getStatusCode().equals(HttpStatus.OK))
				.map(ResponseEntity::getBody)
				.filter( a -> a.addressId() > 0 )
				.orElseThrow( ()-> new ResourceNotFoundException(Map.of(SomeConstants.ADDRESSID, String.valueOf(id))));
	}

	@Override
	public AddressDto create(AddressMvcDto dto) {

		return Optional.ofNullable(msCustomersClient.create(addressMvcMapper.fromMvcDtoToDto(dto)))
				.filter( r -> r.getStatusCode().equals(HttpStatus.CREATED))
				.map(ResponseEntity::getBody)
				.filter( a -> a.addressId() > 0 )
				.orElseThrow( () -> new ResourceNotFoundException(Map.of( SomeConstants.DATELESS, dto.toString())));
	}

	@Override
	public AddressDto update(Long id, AddressMvcDto dto) {

		return Optional.ofNullable(msCustomersClient.update(id, addressMvcMapper.fromMvcDtoToDto(dto)))
				.filter( r -> r.getStatusCode().equals(HttpStatus.OK))
				.map(ResponseEntity::getBody)
				.filter( a -> a.addressId() > 0 )
				.orElseThrow( () -> new ResourceNotFoundException(Map.of( SomeConstants.ADDRESSID, dto.toString())));
	}

	@Override
	public Boolean delete(Long id) {

		return Optional.ofNullable(msCustomersClient.deleteAddress(id))
				.filter( r -> r.getStatusCode().equals(HttpStatus.OK))
				.map(ResponseEntity::getBody)
				.orElseThrow( () -> new EntityAssociatedException(Map.of( SomeConstants.ADDRESSID, String.valueOf(id))));
	}	

	@Override
	public Boolean setCustomer(Long addressId, Long customerId) {

		return Optional.ofNullable(msCustomersClient.setCustomer(addressId, customerId))
				.filter( r -> r.getStatusCode().equals(HttpStatus.OK))
				.map(ResponseEntity::getBody)
				.orElseThrow( () -> new ResourceNotFoundException(Map.of( SomeConstants.ADDRESSID, String.valueOf(addressId), 
																		  SomeConstants.CUSTOMERID, String.valueOf(customerId))));	
	}

}
