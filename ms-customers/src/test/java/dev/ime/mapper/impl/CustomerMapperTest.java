package dev.ime.mapper.impl;


import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.ime.dto.CustomerDto;
import dev.ime.entity.Customer;

@ExtendWith(MockitoExtension.class)
class CustomerMapperTest {

	@InjectMocks
	private CustomerMapper customerMapper;

	private List<Customer>customers;
	private List<CustomerDto>customersDtos;
	private Customer customerTest;
	private CustomerDto customerTestDto;
	private final Long customerId = 4L;
	private final String companyName = "Konohagakure";
	private final String contactName = "Kakashi";
	
	@BeforeEach	
	private void createObjects() {
		
		customers = new ArrayList<>();
		customersDtos = new ArrayList<>();
		
		customerTest = new Customer();
		customerTest.setId(customerId);
		customerTest.setCompanyName(companyName);
		customerTest.setContactName(contactName);
		
		customerTestDto = new CustomerDto(customerId, companyName, contactName);		
		
	}
	
	@Test
	void AddressMapper_fromDto_ReturnAddress() {
		
		Customer customer = customerMapper.fromDto(customerTestDto);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(customer).isNotNull(),
				()-> Assertions.assertThat(customer.getId()).isEqualTo(customerId),
				()-> Assertions.assertThat(customer.getCompanyName()).isEqualTo(companyName),
				()-> Assertions.assertThat(customer.getContactName()).isEqualTo(contactName)
				);
	}

	@Test
	void AddressMapper_toListDto_ReturnListCustomerDto() {
		
		customers.add(customerTest);
		customersDtos.add(customerTestDto);
		
		List<CustomerDto>list = customerMapper.toListDto(customers);
	
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(list).isNotNull(),
				()-> Assertions.assertThat(list).hasSize(1),
				()-> Assertions.assertThat(list.get(0).customerId()).isEqualTo(customerId.intValue()),
				()-> Assertions.assertThat(list.get(0).companyName()).isEqualTo(companyName),
				()-> Assertions.assertThat(list.get(0).contactName()).isEqualTo(contactName)
				);
	}
	
}
