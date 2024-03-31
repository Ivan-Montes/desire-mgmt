package dev.ime.mapper.impl;

import org.springframework.stereotype.Component;

import dev.ime.dto.AddressDto;
import dev.ime.dtomvc.AddressMvcDto;
import dev.ime.mapper.GenericMvcMapper;

@Component
public class AddressMvcMapper implements GenericMvcMapper<AddressMvcDto, AddressDto>{

	@Override
	public AddressDto fromMvcDtoToDto(AddressMvcDto mvcDto) {
		
		return new AddressDto(mvcDto.getAddressId(),
							  mvcDto.getLocation(),
							  mvcDto.getCity(),
							  mvcDto.getCountry(),
							  mvcDto.getEmail(),
							  mvcDto.getCustomerId());
	}

}
