package dev.ime.mapper.impl;


import java.util.List;

import org.springframework.stereotype.Component;

import dev.ime.dto.CustomerDto;
import dev.ime.entity.Customer;
import dev.ime.mapper.GenericMapper;

@Component
public class CustomerMapper implements GenericMapper<Customer, CustomerDto> {

	public CustomerMapper() {
		super();
	}

	@Override
	public Customer fromDto(CustomerDto dto) {
		
		Customer c = new Customer();
		c.setId(dto.customerId());
		c.setCompanyName(dto.companyName());
		c.setContactName(dto.contactName());
		
		return c;
	}

	@Override
	public CustomerDto toDto(Customer e) {
		
		return new CustomerDto(e.getId(),e.getCompanyName(),e.getContactName());
	}

	@Override
	public List<CustomerDto> toListDto(List<Customer> list) {
		
		return list.stream()
				.map(this::toDto)
				.toList();
	}



}
