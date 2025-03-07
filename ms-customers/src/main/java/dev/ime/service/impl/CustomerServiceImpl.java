package dev.ime.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import dev.ime.dto.CustomerDto;
import dev.ime.entity.Address;
import dev.ime.entity.Customer;
import dev.ime.exception.EntityAssociatedException;
import dev.ime.exception.ResourceNotFoundException;
import dev.ime.repository.AddressRepository;
import dev.ime.repository.CustomerRepository;
import dev.ime.service.CustomerSpecificService;
import dev.ime.service.GenericService;
import dev.ime.tool.SomeConstants;

@Service
public class CustomerServiceImpl implements GenericService<Customer, CustomerDto>, CustomerSpecificService{

	private final CustomerRepository customerRepo;
	private final AddressRepository addressRepo;
	
	public CustomerServiceImpl(CustomerRepository customerRepo, AddressRepository addressRepo) {
		this.customerRepo = customerRepo;
		this.addressRepo = addressRepo;
	}

	@Override
	public List<Customer> getAll() {
		return customerRepo.findAll(Sort.by("id"));
	}

	@Override
	public List<Customer> getAllPaged(Integer pageNumber, Integer pageSize) {
		return customerRepo.findAll(PageRequest.of(pageNumber,pageSize)).toList();
	}

	@Override
	public Optional<Customer> getById(Long id) {
		return Optional.ofNullable( customerRepo.findById(id).orElseThrow( ()-> new ResourceNotFoundException( Map.of( SomeConstants.CUSTOMERID, String.valueOf(id) ) ) ) );
	}

	@Override
	public Optional<Customer> create(CustomerDto entity) {	
		
		Customer customer = new Customer();
		customer.setCompanyName(entity.companyName());
		customer.setContactName(entity.contactName());
		
		return Optional.ofNullable( customerRepo.save(customer));
		
	}

	@Override
	public Optional<Customer> update(Long id, CustomerDto entity) {
		
		Customer c = searchCustomerById(id);
		
		c.setCompanyName(entity.companyName());
		c.setContactName(entity.contactName());
		
		return Optional.ofNullable(customerRepo.save(c));
		
	}

	@Override
	public Integer delete(Long id) {
		
		Customer c = searchCustomerById(id);
		
		if ( c.getAddresses().isEmpty() ) {
			
			customerRepo.deleteById(id);
			return customerRepo.findById(id).isEmpty()? 0:1;
			
		} else {
			
			throw new EntityAssociatedException( Map.of( SomeConstants.CUSTOMERID, String.valueOf(id) ) );

		}
		
	}

	@Override
	public Boolean addAddress(Long customerId, Long addressId) {
		
		Customer c = searchCustomerById(customerId);
		Address a = addressRepo.findById(addressId).orElseThrow( ()-> new ResourceNotFoundException( Map.of( SomeConstants.ADDRESSID, String.valueOf(addressId) ) ) );
		
		c.getAddresses().add(a);
		a.setCustomer(c);
		
		return Optional.ofNullable( customerRepo.save(c) ).isPresent();
	}

	private Customer searchCustomerById(Long customerId) {
		
		return customerRepo.findById(customerId).orElseThrow( ()-> new ResourceNotFoundException( Map.of( SomeConstants.CUSTOMERID, String.valueOf(customerId) ) ) );
		
	}

}
