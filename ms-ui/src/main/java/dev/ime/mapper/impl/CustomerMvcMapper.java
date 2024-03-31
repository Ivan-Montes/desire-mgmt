package dev.ime.mapper.impl;

import org.springframework.stereotype.Component;

import dev.ime.dto.CustomerDto;
import dev.ime.dtomvc.CustomerMvcDto;
import dev.ime.mapper.GenericMvcMapper;

@Component
public class CustomerMvcMapper implements GenericMvcMapper<CustomerMvcDto, CustomerDto>{

	@Override
	public CustomerDto fromMvcDtoToDto(CustomerMvcDto mvcDto) {
		
		return new CustomerDto(mvcDto.getCustomerId(),
							   mvcDto.getCompanyName(),
							   mvcDto.getContactName());
	}

}
