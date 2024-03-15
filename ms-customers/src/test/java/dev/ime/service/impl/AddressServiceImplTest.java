package dev.ime.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import dev.ime.dto.AddressDto;
import dev.ime.entity.Address;
import dev.ime.entity.Customer;
import dev.ime.exception.ResourceNotFoundException;
import dev.ime.mapper.impl.AddressMapper;
import dev.ime.repository.AddressRepository;
import dev.ime.repository.CustomerRepository;

@ExtendWith(MockitoExtension.class)
class AddressServiceImplTest {

	@Mock
	private AddressRepository addressRepo;
	@Mock
	private AddressMapper addressMapper;
	@Mock
	private CustomerRepository customerRepo;
	
	@InjectMocks
	private AddressServiceImpl addressService;
	
	private List<Address>addresses;
	private final Long addressId = 4L;
	private final String location = "Cattedrale di Santa Maria del Fiore";
	private final String city = "Florence";
	private final String country = "Italy";
	private final String email = "florence@florence.it";
	private Address addressTest;
	private AddressDto addressDto;

	private final Long customerId = 4L;
	private final String companyName = "Konohagakure";
	private final String contactName = "Kakashi";
	private Customer customerTest;
	
	@BeforeEach
	private void createObjects() {
		
		addresses = new ArrayList<>();
		
		addressTest = new Address();
		addressTest.setId(addressId);
		addressTest.setLocation(location);
		addressTest.setCity(city);
		addressTest.setCountry(country);
		addressTest.setEmail(email);
		
		addressDto = new AddressDto(5L,"San Marco 1","Venecia","Veneto","ven@ven.it",3L);
		
		customerTest = new Customer();
		customerTest.setId(customerId);
		customerTest.setCompanyName(companyName);
		customerTest.setContactName(contactName);
	}
	
	@Test
	void AddressServiceImpl_getAll_ReturnListAddress() {
		
		addresses.add(addressTest);
		Mockito.when(addressRepo.findAll()).thenReturn(addresses);
		
		List<Address>list = addressService.getAll();
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(list).isNotNull(),
				()-> Assertions.assertThat(list).isNotEmpty(),
				()-> Assertions.assertThat(list).hasSize(1),
				()-> Assertions.assertThat(list.get(0).getId()).isEqualTo(addressId),
				()-> Assertions.assertThat(list.get(0).getLocation()).isEqualTo(location),
				()-> Assertions.assertThat(list.get(0).getCity()).isEqualTo(city),
				()-> Assertions.assertThat(list.get(0).getCountry()).isEqualTo(country),
				()-> Assertions.assertThat(list.get(0).getEmail()).isEqualTo(email)
				);
		Mockito.verify(addressRepo, Mockito.times(1)).findAll();		
	}

	@Test
	void AddressServiceImpl_getAllPaged_ReturnListAddress() {
		
		addresses.add(addressTest);
		@SuppressWarnings("unchecked")
		Page<Address> page = Mockito.mock(Page.class);
		Mockito.when(addressRepo.findAll(Mockito.any(PageRequest.class))).thenReturn(page);
		Mockito.when(page.toList()).thenReturn(addresses);
		
		List<Address>list = addressService.getAllPaged(1,2);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(list).isNotNull(),
				()-> Assertions.assertThat(list).isNotEmpty(),
				()-> Assertions.assertThat(list).hasSize(1),
				()-> Assertions.assertThat(list.get(0).getId()).isEqualTo(addressId),
				()-> Assertions.assertThat(list.get(0).getLocation()).isEqualTo(location),
				()-> Assertions.assertThat(list.get(0).getCity()).isEqualTo(city),
				()-> Assertions.assertThat(list.get(0).getCountry()).isEqualTo(country),
				()-> Assertions.assertThat(list.get(0).getEmail()).isEqualTo(email)
				);
		Mockito.verify(addressRepo, Mockito.times(1)).findAll(Mockito.any(PageRequest.class));
	}

	@Test
	void AddressServiceImpl_getById_ReturnOptAddress() {
		
		Mockito.when(addressRepo.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(addressTest));
		
		Optional<Address>optAddr = addressService.getById(addressId);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optAddr).isNotNull(),
				()-> Assertions.assertThat(optAddr).isNotEmpty(),
				()-> Assertions.assertThat(optAddr.get().getId()).isEqualTo(addressId),
				()-> Assertions.assertThat(optAddr.get().getLocation()).isEqualTo(location),
				()-> Assertions.assertThat(optAddr.get().getCity()).isEqualTo(city),
				()-> Assertions.assertThat(optAddr.get().getCountry()).isEqualTo(country),
				()-> Assertions.assertThat(optAddr.get().getEmail()).isEqualTo(email)
				);
		Mockito.verify(addressRepo, Mockito.times(1)).findById(Mockito.anyLong());
	}
	
	@Test
	void AddressServiceImpl_getById_ReturnExceptionResourceNotFoundException() {
		
		Mockito.when(addressRepo.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(null));

		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, ()-> addressService.getById(addressId));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(ResourceNotFoundException.class)
				);
		Mockito.verify(addressRepo, Mockito.times(1)).findById(Mockito.anyLong());
	}

	@Test
	void AddressServiceImpl_create_ReturnOptAddress() {
		
		Mockito.when(customerRepo.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(customerTest));
		Mockito.when(addressMapper.fromDto(Mockito.any(AddressDto.class))).thenReturn(addressTest);
		Mockito.when(addressRepo.save(Mockito.any(Address.class))).thenReturn(addressTest);
		
		Optional<Address>optAddr = addressService.create(addressDto);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optAddr).isNotNull(),
				()-> Assertions.assertThat(optAddr).isNotEmpty(),
				()-> Assertions.assertThat(optAddr.get().getId()).isEqualTo(addressId),
				()-> Assertions.assertThat(optAddr.get().getLocation()).isEqualTo(location),
				()-> Assertions.assertThat(optAddr.get().getCity()).isEqualTo(city),
				()-> Assertions.assertThat(optAddr.get().getCountry()).isEqualTo(country),
				()-> Assertions.assertThat(optAddr.get().getEmail()).isEqualTo(email)
				);
		Mockito.verify(customerRepo, Mockito.times(1)).findById(Mockito.anyLong());
		Mockito.verify(addressMapper, Mockito.times(1)).fromDto(Mockito.any(AddressDto.class));
		Mockito.verify(addressRepo, Mockito.times(1)).save(Mockito.any(Address.class));
	}
	
	@Test
	void AddressServiceImpl_create_ReturnResourceNotFoundException() {
		
		Mockito.when(customerRepo.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(null));

		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, ()-> addressService.create(addressDto));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(ResourceNotFoundException.class)				
				);
		Mockito.verify(customerRepo, Mockito.times(1)).findById(Mockito.anyLong());
	}

	@Test
	void AddressServiceImpl_update_ReturnOptAddress() {
		
		Mockito.when(addressRepo.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(addressTest));
		Mockito.when(customerRepo.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(customerTest));
		Mockito.when(addressRepo.save(Mockito.any(Address.class))).thenReturn(addressTest);

		Optional<Address>optAddr = addressService.update(addressId, addressDto);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optAddr).isNotNull(),
				()-> Assertions.assertThat(optAddr).isNotEmpty(),
				()-> Assertions.assertThat(optAddr.get().getId()).isEqualTo(addressId),
				()-> Assertions.assertThat(optAddr.get().getLocation()).isEqualTo(addressDto.location()),
				()-> Assertions.assertThat(optAddr.get().getCity()).isEqualTo(addressDto.city()),
				()-> Assertions.assertThat(optAddr.get().getCountry()).isEqualTo(addressDto.country()),
				()-> Assertions.assertThat(optAddr.get().getEmail()).isEqualTo(addressDto.email())
				);
		Mockito.verify(addressRepo, Mockito.times(1)).findById(Mockito.anyLong());
		Mockito.verify(customerRepo, Mockito.times(1)).findById(Mockito.anyLong());
		Mockito.verify(addressRepo, Mockito.times(1)).save(Mockito.any(Address.class));
	}

	@Test
	void AddressServiceImpl_update_ReturnResourceNotFoundExceptionByAddress() {
		
		Mockito.when(addressRepo.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(null));
		
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, ()-> addressService.update(addressId, addressDto));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(ResourceNotFoundException.class)				
				);
		Mockito.verify(addressRepo, Mockito.times(1)).findById(Mockito.anyLong());
	}

	@Test
	void AddressServiceImpl_update_ReturnResourceNotFoundExceptionByCustomer() {
		
		Mockito.when(addressRepo.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(addressTest));
		Mockito.when(customerRepo.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(null));
		
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, ()-> addressService.update(addressId, addressDto));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(ResourceNotFoundException.class)				
				);
		Mockito.verify(addressRepo, Mockito.times(1)).findById(Mockito.anyLong());
		Mockito.verify(customerRepo, Mockito.times(1)).findById(Mockito.anyLong());		
	}

	@Test
	void AddressServiceImpl_delete_ReturnIntOk() {
		
		Mockito.doReturn(Optional.ofNullable(addressTest)).doReturn(Optional.empty()).when(addressRepo).findById(Mockito.anyLong());
		Mockito.doNothing().when(addressRepo).deleteById(Mockito.anyLong());
		
		Integer resultValue = addressService.delete(addressId);
		 
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(resultValue).isNotNull(),
				()-> Assertions.assertThat(resultValue).isZero()
				);
		Mockito.verify(addressRepo, Mockito.times(2)).findById(Mockito.anyLong());
		Mockito.verify(addressRepo, Mockito.times(1)).deleteById(Mockito.anyLong());
	}

	@Test
	void AddressServiceImpl_delete_ReturnIntFail() {
		Mockito.doReturn(Optional.ofNullable(addressTest)).doReturn(Optional.ofNullable(addressTest)).when(addressRepo).findById(Mockito.anyLong());
		Mockito.doNothing().when(addressRepo).deleteById(Mockito.anyLong());
		
		Integer resultValue = addressService.delete(addressId);
		 
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(resultValue).isNotNull(),
				()-> Assertions.assertThat(resultValue).isEqualTo(1)
				);
		Mockito.verify(addressRepo, Mockito.times(2)).findById(Mockito.anyLong());
		Mockito.verify(addressRepo, Mockito.times(1)).deleteById(Mockito.anyLong());
	}

	@Test
	void AddressServiceImpl_delete_ReturnResourceNotFoundException() {
		
		Mockito.when(addressRepo.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(null));		

		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, ()-> addressService.delete(addressId));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(ResourceNotFoundException.class)				
				);
		Mockito.verify(addressRepo, Mockito.times(1)).findById(Mockito.anyLong());

	}

	@Test
	void AddressServiceImpl_setCustomer_ReturnBooleanTrue() {
		
		Mockito.when(addressRepo.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(addressTest));
		Mockito.when(customerRepo.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(customerTest));
		Mockito.when(addressRepo.save(Mockito.any(Address.class))).thenReturn(addressTest);

		Boolean resultValue = addressService.setCustomer(addressId, customerId);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(resultValue).isNotNull(),
				()-> Assertions.assertThat(resultValue).isTrue()
				);
		Mockito.verify(addressRepo, Mockito.times(1)).findById(Mockito.anyLong());
		Mockito.verify(customerRepo, Mockito.times(1)).findById(Mockito.anyLong());
		Mockito.verify(addressRepo, Mockito.times(1)).save(Mockito.any(Address.class));
	}

	@Test
	void AddressServiceImpl_setCustomer_ReturnBooleanFalse() {

		Mockito.when(addressRepo.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(addressTest));
		Mockito.when(customerRepo.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(customerTest));
		Mockito.when(addressRepo.save(Mockito.any(Address.class))).thenReturn(null);

		Boolean resultValue = addressService.setCustomer(addressId, customerId);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(resultValue).isNotNull(),
				()-> Assertions.assertThat(resultValue).isFalse()
				);
		Mockito.verify(addressRepo, Mockito.times(1)).findById(Mockito.anyLong());
		Mockito.verify(customerRepo, Mockito.times(1)).findById(Mockito.anyLong());
		Mockito.verify(addressRepo, Mockito.times(1)).save(Mockito.any(Address.class));
	}

	@Test
	void AddressServiceImpl_setCustomer_ReturnExceptionResourceNotFoundExceptionbyAddress() {
		
		Mockito.when(addressRepo.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(null));

		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, ()-> addressService.setCustomer(addressId, customerId));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(ResourceNotFoundException.class)
				);
		Mockito.verify(addressRepo, Mockito.times(1)).findById(Mockito.anyLong());
	}

	@Test
	void AddressServiceImpl_setCustomer_ReturnExceptionResourceNotFoundExceptionByCustomer() {


		Mockito.when(addressRepo.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(addressTest));
		Mockito.when(customerRepo.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(null));

		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, ()-> addressService.setCustomer(addressId, customerId));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(ResourceNotFoundException.class)
				);
		Mockito.verify(addressRepo, Mockito.times(1)).findById(Mockito.anyLong());
	}
}
