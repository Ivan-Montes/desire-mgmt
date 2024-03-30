package dev.ime.mapper.impl;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.ime.dto.AddressDto;
import dev.ime.entity.Address;
import dev.ime.entity.Customer;


@ExtendWith(MockitoExtension.class)
class AddressMapperTest {

	@InjectMocks
	private AddressMapper addressMapper;
	
	private List<Address>addresses;
	
	private Address addressTest;
	private final Long addressId = 4L;
	private final String location = "Cattedrale di Santa Maria del Fiore";
	private final String city = "Florence";
	private final String country = "Italy";
	private final String email = "florence@florence.it";

	private AddressDto addressTestDto;
	private final Long addressIdDto = 5L;
	private final String locationDto = "San Marco 1";
	private final String cityDto = "Venecia";
	private final String countryDto = "Veneto";
	private final String emailDto = "ven@ven.it";
	private final Long customerId = 47L;
	
	@BeforeEach	
	private void createObjects() {
		
		addresses = new ArrayList<>();
		
		addressTest = new Address();
		addressTest.setId(addressId);
		addressTest.setLocation(location);
		addressTest.setCity(city);
		addressTest.setCountry(country);
		addressTest.setEmail(email);
		addressTest.setCustomer(new Customer());
		
		addressTestDto = new AddressDto(addressIdDto,locationDto,cityDto,countryDto,emailDto,customerId);
	
	}
	
	@Test
	void AddressMapper_fromDto_ReturnAddress() {
		
		Address address = addressMapper.fromDto(addressTestDto);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(address).isNotNull(),
				()-> Assertions.assertThat(address.getId()).isEqualTo(addressIdDto),
				()-> Assertions.assertThat(address.getLocation()).isEqualTo(locationDto),
				()-> Assertions.assertThat(address.getCity()).isEqualTo(cityDto),
				()-> Assertions.assertThat(address.getCountry()).isEqualTo(countryDto),
				()-> Assertions.assertThat(address.getEmail()).isEqualTo(emailDto)
				);
	}

	@Test
	void AddressMapper_toListDto_ReturnListAddressDto() {
		
		addresses.add(addressTest);
		
		List<AddressDto>list = addressMapper.toListDto(addresses);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(list).isNotNull(),
				()-> Assertions.assertThat(list).hasSize(1),
				()-> Assertions.assertThat(list.get(0).addressId()).isEqualTo(addressId),
				()-> Assertions.assertThat(list.get(0).location()).isEqualTo(location),
				()-> Assertions.assertThat(list.get(0).city()).isEqualTo(city),
				()-> Assertions.assertThat(list.get(0).country()).isEqualTo(country),
				()-> Assertions.assertThat(list.get(0).email()).isEqualTo(email)
				);
	}
}
