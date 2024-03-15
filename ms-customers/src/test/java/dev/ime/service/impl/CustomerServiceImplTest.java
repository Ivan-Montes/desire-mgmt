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

import dev.ime.dto.CustomerDto;
import dev.ime.entity.Address;
import dev.ime.entity.Customer;
import dev.ime.exception.EntityAssociatedException;
import dev.ime.exception.ResourceNotFoundException;
import dev.ime.mapper.impl.CustomerMapper;
import dev.ime.repository.AddressRepository;
import dev.ime.repository.CustomerRepository;


@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

	@Mock
	private CustomerRepository customerRepo;
	@Mock
	private CustomerMapper customerMapper;
	@Mock
	private AddressRepository addressRepo;
	
	@InjectMocks
	private CustomerServiceImpl customerService;
	
	private List<Customer>customers;
	private final Long customerId = 4L;
	private final String companyName = "Konohagakure";
	private final String contactName = "Kakashi";
	private Customer customerTest;
	private CustomerDto customerDtoTest;
	
	@BeforeEach
	private void createObjects() {
		
		customers = new ArrayList<>();
		
		customerTest = new Customer();
		customerTest.setId(customerId);
		customerTest.setCompanyName(companyName);
		customerTest.setContactName(contactName);
	
		customerDtoTest = new CustomerDto(5L, "Amegakure", "Pain");
	}
	
	
	@Test
	void CustomerServiceImpl_getAll_ResturnListCustomer() {
		
		customers.add(customerTest);
		Mockito.when(customerRepo.findAll()).thenReturn(customers);
		
		List<Customer>list = customerService.getAll();
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(list).isNotNull(),
				()-> Assertions.assertThat(list).isNotEmpty(),
				()-> Assertions.assertThat(list).hasSize(1),
				()-> Assertions.assertThat(list.get(0).getId()).isEqualTo(customerId),
				()-> Assertions.assertThat(list.get(0).getCompanyName()).isEqualTo(companyName)
				);
		Mockito.verify(customerRepo, Mockito.times(1)).findAll();
	}

	@Test
	void CustomerServiceImpl_getAllPaged_ResturnListCustomer() {
		
		customers.add(customerTest);
		@SuppressWarnings("unchecked")
		Page<Customer> page = Mockito.mock(Page.class);
		Mockito.when(customerRepo.findAll(Mockito.any(PageRequest.class))).thenReturn(page);
		Mockito.when(page.toList()).thenReturn(customers);
		
		List<Customer>list = customerService.getAllPaged(1, 1);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(list).isNotNull(),
				()-> Assertions.assertThat(list).isNotEmpty(),
				()-> Assertions.assertThat(list).hasSize(1),
				()-> Assertions.assertThat(list.get(0).getId()).isEqualTo(customerId),
				()-> Assertions.assertThat(list.get(0).getCompanyName()).isEqualTo(companyName),
				()-> Assertions.assertThat(list.get(0).getContactName()).isEqualTo(contactName)
				);
		Mockito.verify(customerRepo, Mockito.times(1)).findAll(Mockito.any(PageRequest.class));
	}

	@Test
	void CustomerServiceImpl_getById_ReturnOptCustomer() {
		
		Mockito.when(customerRepo.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(customerTest));
		
		Optional<Customer>optCus = customerService.getById(customerId);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optCus).isNotNull(),
				()-> Assertions.assertThat(optCus).isNotEmpty(),
				()-> Assertions.assertThat(optCus.get().getId()).isEqualTo(customerId),
				()-> Assertions.assertThat(optCus.get().getCompanyName()).isEqualTo(companyName),
				()-> Assertions.assertThat(optCus.get().getContactName()).isEqualTo(contactName)
				);
		Mockito.verify(customerRepo, Mockito.times(1)).findById(Mockito.anyLong());
	}
	

	@Test
	void CustomerServiceImpl_getById_ReturnResourceNotFoundException() {
		
		Mockito.when(customerRepo.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(null));
		
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, ()-> customerService.getById(customerId));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(ResourceNotFoundException.class)
				);
		Mockito.verify(customerRepo, Mockito.times(1)).findById(Mockito.anyLong());
	}
	

	@Test
	void CustomerServiceImpl_create_ReturnOptCustomer() {
		
		Mockito.when(customerMapper.fromDto(Mockito.any(CustomerDto.class))).thenReturn(customerTest);
		Mockito.when(customerRepo.save(Mockito.any(Customer.class))).thenReturn(customerTest);
		
		Optional<Customer>optCus = customerService.create(customerDtoTest);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optCus).isNotNull(),
				()-> Assertions.assertThat(optCus).isNotEmpty(),
				()-> Assertions.assertThat(optCus.get().getId()).isEqualTo(customerId),
				()-> Assertions.assertThat(optCus.get().getCompanyName()).isEqualTo(companyName),
				()-> Assertions.assertThat(optCus.get().getContactName()).isEqualTo(contactName)
				);
		Mockito.verify(customerMapper, Mockito.times(1)).fromDto(Mockito.any(CustomerDto.class));
		Mockito.verify(customerRepo, Mockito.times(1)).save(Mockito.any(Customer.class));
	}

	@Test
	void CustomerServiceImpl_update_ReturnOptCustomer() {
		
		Mockito.when(customerRepo.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(customerTest));
		Mockito.when(customerRepo.save(Mockito.any(Customer.class))).thenReturn(customerTest);
		
		Optional<Customer>optCus = customerService.update(customerId,customerDtoTest);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optCus).isNotNull(),
				()-> Assertions.assertThat(optCus).isNotEmpty(),
				()-> Assertions.assertThat(optCus.get().getId()).isEqualTo(customerId),
				()-> Assertions.assertThat(optCus.get().getCompanyName()).isEqualTo("Amegakure"),
				()-> Assertions.assertThat(optCus.get().getContactName()).isEqualTo("Pain")
				);
		Mockito.verify(customerRepo, Mockito.times(1)).findById(Mockito.anyLong());
		Mockito.verify(customerRepo, Mockito.times(1)).save(Mockito.any(Customer.class));
	}

	@Test
	void CustomerServiceImpl_update_ReturnResourceNotFoundException() {
		
		Mockito.when(customerRepo.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(null));
		
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, ()-> customerService.update(customerId,customerDtoTest));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(ResourceNotFoundException.class)
				);
		Mockito.verify(customerRepo, Mockito.times(1)).findById(Mockito.anyLong());
	}
	
	@Test
	void CustomerServiceImpl_delete_ReturnIntOk() {
		
		Mockito.doReturn(Optional.of(customerTest)).doReturn(Optional.empty()).when(customerRepo).findById(Mockito.anyLong());
		Mockito.doNothing().when(customerRepo).deleteById(Mockito.anyLong());
		
		Integer resultValue = customerService.delete(customerId);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(resultValue).isNotNull(),
				()-> Assertions.assertThat(resultValue).isZero()
				);		
		Mockito.verify(customerRepo, Mockito.times(2)).findById(Mockito.anyLong());
		Mockito.verify(customerRepo, Mockito.times(1)).deleteById(Mockito.anyLong());
		
	}

	@Test
	void CustomerServiceImpl_delete_ReturnIntError() {
		
		Mockito.doReturn(Optional.of(customerTest)).doReturn(Optional.of(customerTest)).when(customerRepo).findById(Mockito.anyLong());
		Mockito.doNothing().when(customerRepo).deleteById(Mockito.anyLong());
		
		Integer resultValue = customerService.delete(customerId);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(resultValue).isNotNull(),
				()-> Assertions.assertThat(resultValue).isEqualTo(1)
				);		
		Mockito.verify(customerRepo, Mockito.times(2)).findById(Mockito.anyLong());
		Mockito.verify(customerRepo, Mockito.times(1)).deleteById(Mockito.anyLong());		
	}

	@Test
	void CustomerServiceImpl_delete_ReturnResourceNotFoundException() {
		
		Mockito.when(customerRepo.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(null));
		
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, ()-> customerService.delete(customerId));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(ResourceNotFoundException.class)
				);		
		Mockito.verify(customerRepo, Mockito.times(1)).findById(Mockito.anyLong());	
	}
	
	@Test
	void CustomerServiceImpl_delete_ReturnEntityAssociatedException() {
		
		customerTest.getAddresses().add(new Address());
		Mockito.when(customerRepo.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(customerTest));
		
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(EntityAssociatedException.class, ()-> customerService.delete(customerId));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(EntityAssociatedException.class)
				);		
		Mockito.verify(customerRepo, Mockito.times(1)).findById(Mockito.anyLong());		
	}
	
	@Test
	void CustomerServiceImpl_addAddress_ReturnBooleanTrue() {
		
		Mockito.when(customerRepo.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(customerTest));
		Mockito.when(addressRepo.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(new Address()));
		Mockito.when(customerRepo.save(Mockito.any(Customer.class))).thenReturn(customerTest);
		
		Boolean resultValue = customerService.addAddress(customerId, customerId);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(resultValue).isNotNull(),
				()-> Assertions.assertThat(resultValue).isTrue()
				);		
		Mockito.verify(customerRepo, Mockito.times(1)).findById(Mockito.anyLong());		
		Mockito.verify(addressRepo, Mockito.times(1)).findById(Mockito.anyLong());		
		Mockito.verify(customerRepo, Mockito.times(1)).save(Mockito.any(Customer.class));
	}

	@Test
	void CustomerServiceImpl_addAddress_ReturnBooleanFalse() {
		Mockito.when(customerRepo.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(customerTest));
		Mockito.when(addressRepo.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(new Address()));
		Mockito.when(customerRepo.save(Mockito.any(Customer.class))).thenReturn(null);
		
		Boolean resultValue = customerService.addAddress(customerId, customerId);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(resultValue).isNotNull(),
				()-> Assertions.assertThat(resultValue).isFalse()
				);		
		Mockito.verify(customerRepo, Mockito.times(1)).findById(Mockito.anyLong());		
		Mockito.verify(addressRepo, Mockito.times(1)).findById(Mockito.anyLong());		
		Mockito.verify(customerRepo, Mockito.times(1)).save(Mockito.any(Customer.class));
	}

	@Test
	void CustomerServiceImpl_addAddress_ReturnResourceNotFoundExceptionByCustomer() {
		
		Mockito.when(customerRepo.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(null));
		
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, ()-> customerService.addAddress(customerId, customerId));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(ResourceNotFoundException.class)
				);
		Mockito.verify(customerRepo, Mockito.times(1)).findById(Mockito.anyLong());
	}
	
	@Test
	void CustomerServiceImpl_addAddress_ReturnResourceNotFoundExceptionByAddress() {
		
		Mockito.when(customerRepo.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(customerTest));
		Mockito.when(addressRepo.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(null));
		
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, ()-> customerService.addAddress(customerId, customerId));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(ResourceNotFoundException.class)
				);
		Mockito.verify(customerRepo, Mockito.times(1)).findById(Mockito.anyLong());	
		Mockito.verify(addressRepo, Mockito.times(1)).findById(Mockito.anyLong());	
	}
	
}
