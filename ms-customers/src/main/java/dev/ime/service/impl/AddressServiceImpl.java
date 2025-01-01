package dev.ime.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import dev.ime.dto.AddressDto;
import dev.ime.entity.Address;
import dev.ime.entity.Customer;
import dev.ime.exception.ResourceNotFoundException;
import dev.ime.repository.AddressRepository;
import dev.ime.repository.CustomerRepository;
import dev.ime.service.AddressSpecificService;
import dev.ime.service.GenericService;
import dev.ime.tool.SomeConstants;

@Service
public class AddressServiceImpl implements GenericService<Address,AddressDto>, AddressSpecificService{
	
	private final AddressRepository addressRepo;
	private final CustomerRepository customerRepo;
	
	
	public AddressServiceImpl(AddressRepository addressRepo, CustomerRepository customerRepo) {
		this.addressRepo = addressRepo;
		this.customerRepo = customerRepo;
	}

	@Override
	public List<Address> getAll() {
		return addressRepo.findAll(Sort.by("id"));
	}

	@Override
	public List<Address> getAllPaged(Integer pageNumber, Integer pageSize) {
		return addressRepo.findAll(PageRequest.of(pageNumber, pageSize)).toList();
	}

	@Override
	public Optional<Address> getById(Long id) {
		return Optional.ofNullable( addressRepo.findById(id).orElseThrow( ()-> new ResourceNotFoundException( Map.of(SomeConstants.ADDRESSID, String.valueOf(id) ) ) ) );
	}

	@Override
	public Optional<Address> create(AddressDto entity) {
		
		Customer customerFound = searchCustomerById(entity.customerId());
		Address a = updateFields(new Address(), entity, customerFound);		
		
		return Optional.ofNullable( addressRepo.save( a ));		
	}

	private Address updateFields(Address address, AddressDto entity, Customer customer ) {
		
		address.setLocation(entity.location());
		address.setCity(entity.city());
		address.setCountry(entity.country());
		address.setEmail(entity.email());
		address.setCustomer(customer);
		
		return address;		
	}
	
	@Override
	public Optional<Address> update(Long id, AddressDto entity) {
		
		Address a = addressRepo.findById(id).orElseThrow( ()-> new ResourceNotFoundException( Map.of(SomeConstants.ADDRESSID, String.valueOf(id) ) ) );
		Customer customerFound = searchCustomerById(entity.customerId());
				
		return Optional.ofNullable( addressRepo.save( updateFields(a, entity, customerFound) ));
	}

	private Customer searchCustomerById(Long customerId) {
		
		return customerRepo.findById(customerId).orElseThrow( ()-> new ResourceNotFoundException( Map.of( SomeConstants.CUSTOMERID, String.valueOf(customerId)) ) );
		
	}

	@Override
	public Integer delete(Long id) {
		
		Address a = addressRepo.findById(id).orElseThrow( ()-> new ResourceNotFoundException( Map.of(SomeConstants.ADDRESSID, String.valueOf(id) ) ) );
		addressRepo.deleteById(a.getId());
		
		return addressRepo.findById(a.getId()).isEmpty()? 0:1;
	}

	@Override
	public Boolean setCustomer(Long addressId, Long customerId) {
		
		Address a = addressRepo.findById(addressId).orElseThrow( ()-> new ResourceNotFoundException( Map.of(SomeConstants.ADDRESSID, String.valueOf(addressId) ) ) );
		Customer customerFound = searchCustomerById(customerId);

		a.setCustomer(customerFound);
		customerFound.getAddresses().add(a);
		
		return Optional.ofNullable(addressRepo.save(a)).isPresent();
	}

}
