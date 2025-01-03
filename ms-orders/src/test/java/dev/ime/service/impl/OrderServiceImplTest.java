package dev.ime.service.impl;


import java.time.LocalDate;
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
import org.springframework.data.domain.Sort;

import dev.ime.dto.OrderDto;
import dev.ime.entity.Order;
import dev.ime.entity.OrderDetail;
import dev.ime.exception.DateBadFormatException;
import dev.ime.exception.EntityAssociatedException;
import dev.ime.exception.ResourceNotFoundException;
import dev.ime.repository.OrderDetailRepository;
import dev.ime.repository.OrderRepository;
import dev.ime.tool.Checker;
import dev.ime.tool.Transformer;


@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

	@Mock
	private OrderRepository orderRepo;
	@Mock
	private OrderDetailRepository orderDetailRepo;
	@Mock
	private Checker checker;
	@Mock
	private Transformer transformer;	
	
	@InjectMocks
	private OrderServiceImpl orderService;

	private List<Order> orders;
	private OrderDto orderDtoTest;
	private Order orderTest;
	private OrderDetail orderDetailTest;
	private final Long orderId = 7L;
	private final Long customerId = 3L;
	private final String orderDate = "2013-07-20";
	private final Long orderDetailId = 12L;
	
	@BeforeEach
	private void createObjects() {
		
		orderDtoTest = new OrderDto(orderId,customerId,orderDate);
		
		orders = new ArrayList<>();
		
		orderTest = new Order();
		orderTest.setId(orderId);
		orderTest.setCustomerId(customerId);
		orderTest.setOrderDate(LocalDate.parse(orderDate));
		
		orderDetailTest = new OrderDetail();
		orderDetailTest.setId(orderDetailId);
		
	}
	
	@Test
	void OrderServiceImpl_getAll_ReturnListOrder() {
		
		orders.add(orderTest);
		Mockito.when(orderRepo.findAll(Mockito.any(Sort.class))).thenReturn(orders);
		
		List<Order>list = orderService.getAll();
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(list).isNotNull(),
				()-> Assertions.assertThat(list).isNotEmpty(),
				()-> Assertions.assertThat(list).hasSize(1),
				()-> Assertions.assertThat(list.get(0).getId()).isEqualTo(orderId),
				()-> Assertions.assertThat(list.get(0).getCustomerId()).isEqualTo(customerId)
				);
		Mockito.verify(orderRepo, Mockito.times(1)).findAll(Mockito.any(Sort.class));
	}
	

	@Test
	void OrderServiceImpl_getAllPaged_ReturnListOrder() {
		
		@SuppressWarnings("unchecked")
		Page<Order> page = Mockito.mock(Page.class);
		orders.add(orderTest);
		Mockito.when(orderRepo.findAll(Mockito.any(PageRequest.class))).thenReturn(page);
		Mockito.when(page.toList()).thenReturn(orders);
		
		List<Order>list = orderService.getAllPaged(1, 2);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(list).isNotNull(),
				()-> Assertions.assertThat(list).isNotEmpty(),
				()-> Assertions.assertThat(list).hasSize(1),
				()-> Assertions.assertThat(list.get(0).getId()).isEqualTo(orderId),
				()-> Assertions.assertThat(list.get(0).getCustomerId()).isEqualTo(customerId)
				);
		Mockito.verify(orderRepo, Mockito.times(1)).findAll(Mockito.any(PageRequest.class));
	}

	@Test
	void OrderServiceImpl_getById_ReturnOptOrder() {
		
		Mockito.when(orderRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(orderTest));
		
		Optional<Order>optOr = orderService.getById(orderId);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optOr).isNotNull(),
				()-> Assertions.assertThat(optOr).isNotEmpty(),
				()-> Assertions.assertThat(optOr.get().getId()).isEqualTo(orderId)
				);
		Mockito.verify(orderRepo, Mockito.times(1)).findById(Mockito.anyLong());
	}

	@Test
	void OrderServiceImpl_getById_ReturnException() {
		
		Mockito.when(orderRepo.findById(Mockito.anyLong())).thenReturn(Optional.empty());
		
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, ()-> orderService.getById(orderId));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(ResourceNotFoundException.class)
				);
		Mockito.verify(orderRepo, Mockito.times(1)).findById(Mockito.anyLong());
	}

	@Test
	void OrderServiceImpl_create_ReturnOptOrder() {
		
		Mockito.when(checker.localDateValid(Mockito.anyString())).thenReturn(Boolean.TRUE);
		Mockito.when(checker.checkCustomerId(Mockito.anyLong())).thenReturn(Boolean.TRUE);
		Mockito.when(orderRepo.save(Mockito.any(Order.class))).thenReturn(orderTest);
		
		Optional<Order>optOr = orderService.create(orderDtoTest);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optOr).isNotNull(),
				()-> Assertions.assertThat(optOr).isNotEmpty(),
				()-> Assertions.assertThat(optOr.get().getId()).isEqualTo(orderId)
				);
		Mockito.verify(checker, Mockito.times(1)).localDateValid(Mockito.anyString());
		Mockito.verify(checker, Mockito.times(1)).checkCustomerId(Mockito.anyLong());
		Mockito.verify(orderRepo, Mockito.times(1)).save(Mockito.any(Order.class));
	}

	@Test
	void OrderServiceImpl_create_ReturnDateBadFormatException() {
		
		Mockito.when(checker.localDateValid(Mockito.anyString())).thenReturn(Boolean.FALSE);

		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(DateBadFormatException.class, ()-> orderService.create(orderDtoTest));
	
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(DateBadFormatException.class)
				);
		Mockito.verify(checker, Mockito.times(1)).localDateValid(Mockito.anyString());
	}
	

	@Test
	void OrderServiceImpl_create_ReturnResourceNotFoundException() {
		
		Mockito.when(checker.localDateValid(Mockito.anyString())).thenReturn(Boolean.TRUE);
		Mockito.when(checker.checkCustomerId(Mockito.anyLong())).thenReturn(Boolean.FALSE);

		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, ()-> orderService.create(orderDtoTest));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(ResourceNotFoundException.class)
				);
		Mockito.verify(checker, Mockito.times(1)).localDateValid(Mockito.anyString());
		Mockito.verify(checker, Mockito.times(1)).checkCustomerId(Mockito.anyLong());
	}
	

	@Test
	void OrderServiceImpl_update_ReturnOptOrder() {
		
		Mockito.when(orderRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(orderTest));
		Mockito.when(checker.localDateValid(Mockito.anyString())).thenReturn(Boolean.TRUE);
		Mockito.when(checker.checkCustomerId(Mockito.anyLong())).thenReturn(Boolean.TRUE);
		Mockito.when(transformer.fromStringToDateTimeOnErrorZeroes(Mockito.anyString())).thenReturn(LocalDate.parse(orderDate));
		Mockito.when(orderRepo.save(Mockito.any(Order.class))).thenReturn(orderTest);
		
		Optional<Order>optOr = orderService.update(orderId, orderDtoTest);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optOr).isNotNull(),
				()-> Assertions.assertThat(optOr).isNotEmpty(),
				()-> Assertions.assertThat(optOr.get().getId()).isEqualTo(orderId)
				);
		Mockito.verify(orderRepo, Mockito.times(1)).findById(Mockito.anyLong());
		Mockito.verify(checker, Mockito.times(1)).localDateValid(Mockito.anyString());
		Mockito.verify(checker, Mockito.times(1)).checkCustomerId(Mockito.anyLong());
		Mockito.verify(transformer, Mockito.times(1)).fromStringToDateTimeOnErrorZeroes(Mockito.anyString());
		Mockito.verify(orderRepo, Mockito.times(1)).save(Mockito.any(Order.class));

	}
	
	@Test
	void OrderServiceImpl_update_ReturnResourceNotFoundException() {
		
		Mockito.when(orderRepo.findById(Mockito.anyLong())).thenReturn(Optional.empty());
		
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, ()-> orderService.update(orderId, orderDtoTest));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(ResourceNotFoundException.class)
				);
		Mockito.verify(orderRepo, Mockito.times(1)).findById(Mockito.anyLong());
	}

	@Test
	void OrderServiceImpl_update_ReturnDateBadFormatException() {
		
		Mockito.when(orderRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(orderTest));
		Mockito.when(checker.localDateValid(Mockito.anyString())).thenReturn(Boolean.FALSE);
		
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(DateBadFormatException.class, ()-> orderService.update(orderId, orderDtoTest));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(DateBadFormatException.class)
				);
		Mockito.verify(orderRepo, Mockito.times(1)).findById(Mockito.anyLong());
		Mockito.verify(checker, Mockito.times(1)).localDateValid(Mockito.anyString());
	}

	@Test
	void OrderServiceImpl_update_ReturnResourceNotFoundExceptionByCustomer() {
		
		Mockito.when(orderRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(orderTest));
		Mockito.when(checker.localDateValid(Mockito.anyString())).thenReturn(Boolean.TRUE);
		Mockito.when(checker.checkCustomerId(Mockito.anyLong())).thenReturn(Boolean.FALSE);
		
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, ()-> orderService.update(orderId, orderDtoTest));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(ResourceNotFoundException.class)
				);
		Mockito.verify(orderRepo, Mockito.times(1)).findById(Mockito.anyLong());
		Mockito.verify(checker, Mockito.times(1)).localDateValid(Mockito.anyString());
		Mockito.verify(checker, Mockito.times(1)).checkCustomerId(Mockito.anyLong());
	}

	@Test
	void OrderServiceImpl_delete_ReturnIntOk() {
		
		Mockito.when(orderRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(orderTest)).thenReturn(Optional.empty());
		Mockito.doNothing().when(orderRepo).deleteById(Mockito.anyLong());
		
		Integer resultValue = orderService.delete(orderId);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(resultValue).isNotNull(),
				()-> Assertions.assertThat(resultValue).isZero()
		);
		Mockito.verify(orderRepo, Mockito.times(2)).findById(Mockito.anyLong());
		Mockito.verify(orderRepo, Mockito.times(1)).deleteById(Mockito.anyLong());
	}
	
	@Test
	void OrderServiceImpl_delete_ReturnIntFail() {
		
		Mockito.when(orderRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(orderTest)).thenReturn(Optional.of(orderTest));
		Mockito.doNothing().when(orderRepo).deleteById(Mockito.anyLong());
		
		Integer resultValue = orderService.delete(orderId);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(resultValue).isNotNull(),
				()-> Assertions.assertThat(resultValue).isEqualTo(1)
		);
		Mockito.verify(orderRepo, Mockito.times(2)).findById(Mockito.anyLong());
		Mockito.verify(orderRepo, Mockito.times(1)).deleteById(Mockito.anyLong());
	}
	
	@Test
	void OrderServiceImpl_delete_ReturnResourceNotFoundException() {
		
		Mockito.when(orderRepo.findById(Mockito.anyLong())).thenReturn(Optional.empty());

		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, ()-> orderService.delete(orderId));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(ResourceNotFoundException.class)
				);
		Mockito.verify(orderRepo, Mockito.times(1)).findById(Mockito.anyLong());
	}

	@Test
	void OrderServiceImpl_delete_ReturnEntityAssociatedException() {
		
		orderTest.getOrderDetails().add(orderDetailTest);
		Mockito.when(orderRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(orderTest));

		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(EntityAssociatedException.class, ()-> orderService.delete(orderId));

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(EntityAssociatedException.class)
				);
		Mockito.verify(orderRepo, Mockito.times(1)).findById(Mockito.anyLong());
	}

	@Test
	void OrderServiceImpl_addOrderDetail_ReturnBooleanTrue() {
		
		Mockito.when(orderRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(orderTest));
		Mockito.when(orderDetailRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(orderDetailTest));
		Mockito.when(orderRepo.save(Mockito.any(Order.class))).thenReturn(orderTest);
		
		Boolean resultValue = orderService.addOrderDetail(orderId, orderDetailId);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(resultValue).isNotNull(),
				()-> Assertions.assertThat(resultValue).isTrue()
				);
		Mockito.verify(orderRepo, Mockito.times(1)).findById(Mockito.anyLong());
		Mockito.verify(orderDetailRepo, Mockito.times(1)).findById(Mockito.anyLong());
		Mockito.verify(orderRepo, Mockito.times(1)).save(Mockito.any(Order.class));
	}

	@Test
	void OrderServiceImpl_addOrderDetail_ReturnBooleanFalse() {
		
		Mockito.when(orderRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(orderTest));
		Mockito.when(orderDetailRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(orderDetailTest));
		Mockito.when(orderRepo.save(Mockito.any(Order.class))).thenReturn(null);
		
		Boolean resultValue = orderService.addOrderDetail(orderId, orderDetailId);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(resultValue).isNotNull(),
				()-> Assertions.assertThat(resultValue).isFalse()
				);
		Mockito.verify(orderRepo, Mockito.times(1)).findById(Mockito.anyLong());
		Mockito.verify(orderDetailRepo, Mockito.times(1)).findById(Mockito.anyLong());
		Mockito.verify(orderRepo, Mockito.times(1)).save(Mockito.any(Order.class));
	}

	@Test
	void OrderServiceImpl_addOrderDetail_ReturnResourceNotFoundExceptionByOrder() {
		
		Mockito.when(orderRepo.findById(Mockito.anyLong())).thenReturn(Optional.empty());
		
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, ()-> orderService.addOrderDetail(orderId, orderDetailId));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(ResourceNotFoundException.class)
				);
		Mockito.verify(orderRepo, Mockito.times(1)).findById(Mockito.anyLong());
	}

	@Test
	void OrderServiceImpl_addOrderDetail_ReturnResourceNotFoundExceptionByOrderDetail() {
		
		Mockito.when(orderRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(orderTest));
		Mockito.when(orderDetailRepo.findById(Mockito.anyLong())).thenReturn(Optional.empty());

		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, ()-> orderService.addOrderDetail(orderId, orderDetailId));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(ResourceNotFoundException.class)
				);
		Mockito.verify(orderRepo, Mockito.times(1)).findById(Mockito.anyLong());
		Mockito.verify(orderDetailRepo, Mockito.times(1)).findById(Mockito.anyLong());
	}
}
