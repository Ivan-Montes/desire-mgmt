package dev.ime.mapper.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import dev.ime.dto.AddressDto;
import dev.ime.entity.Address;
import dev.ime.mapper.GenericMapper;

@Component
public class AddressMapper implements GenericMapper<Address, AddressDto>{

	public AddressMapper() {
		super();
	}

	@Override
	public Address fromDto(AddressDto dto) {

		Address a = new Address();
		a.setId(dto.id());
		a.setLocation(dto.location());
		a.setCity(dto.city());
		a.setCountry(dto.country());
		a.setEmail(dto.email());
		a.setCustomer(null);
		
		return null;
	}

	@Override
	public AddressDto toDto(Address e) {
		
		return new AddressDto(e.getId(),e.getLocation(),e.getCity(),e.getCountry(),e.getEmail(),e.getCustomer().getId());
	}

	@Override
	public List<AddressDto> toListDto(List<Address> list) {
		
		return list.stream()
				.map(this::toDto)
				.toList();
	}

}
