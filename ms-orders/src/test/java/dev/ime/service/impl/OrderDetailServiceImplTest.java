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

import dev.ime.dto.OrderDetailDto;
import dev.ime.entity.Order;
import dev.ime.entity.OrderDetail;
import dev.ime.exception.ResourceNotFoundException;
import dev.ime.mapper.impl.OrderDetailMapper;
import dev.ime.repository.OrderDetailRepository;
import dev.ime.repository.OrderRepository;
import dev.ime.tool.Checker;



@ExtendWith(MockitoExtension.class)
class OrderDetailServiceImplTest {

	@Mock
	private OrderDetailRepository orderDetailRepo;
	@Mock
	private OrderRepository orderRepo;
	@Mock
	private OrderDetailMapper orderDetailMapper;
	@Mock
	private Checker checker;
	@InjectMocks
	private OrderDetailServiceImpl orderDetailService;
	
	private List<OrderDetail>orderDetails;
	private OrderDetail orderDetailTest;
	private OrderDetailDto orderDetailDto;
	private Order orderTest;
	private final Long orderDetailId = 2L;
	private final Long orderId = 7L;
	private final Long productId = 3L;
	private final Integer quantity = 13;
	private final Double discount = 23.9D;
	
	@BeforeEach
	private void createObjects() {
		
		orderDetails = new ArrayList<>();
		
		orderDetailDto = new OrderDetailDto(orderDetailId,quantity,discount,productId,orderId);
		
		orderTest = new Order();
		orderTest.setId(orderId);
		
		orderDetailTest = new OrderDetail();
		orderDetailTest.setId(orderDetailId);
		orderDetailTest.setQuantity(quantity);
		orderDetailTest.setDiscount(discount);
		orderDetailTest.setProductId(productId);
		orderDetailTest.setOrder(orderTest);
		
	}
	
	@Test
	void OrderDetailServiceImpl_ReturnListOrderDetail() {
		
		orderDetails.add(orderDetailTest);
		Mockito.when(orderDetailRepo.findAll()).thenReturn(orderDetails);
		
		List<OrderDetail>list = orderDetailService.getAll();
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(list).isNotNull(),
				()-> Assertions.assertThat(list).isNotEmpty(),
				()-> Assertions.assertThat(list).hasSize(1),
				()-> Assertions.assertThat(list.get(0).getId()).isEqualTo(orderDetailId)					
				);
		Mockito.verify(orderDetailRepo, Mockito.times(1)).findAll();
		}

	@Test
	void OrderDetailServiceImpl_getAllPaged_ReturnListOrderDetail() {
		
		orderDetails.add(orderDetailTest);
		@SuppressWarnings("unchecked")
		Page<OrderDetail> page = Mockito.mock(Page.class);
		Mockito.when(orderDetailRepo.findAll(Mockito.any(PageRequest.class))).thenReturn(page);
		Mockito.when(page.toList()).thenReturn(orderDetails);
		
		List<OrderDetail>list = orderDetailService.getAllPaged(1,1);

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(list).isNotNull(),
				()-> Assertions.assertThat(list).isNotEmpty(),
				()-> Assertions.assertThat(list).hasSize(1),
				()-> Assertions.assertThat(list.get(0).getId()).isEqualTo(orderDetailId)					
				);
		Mockito.verify(orderDetailRepo, Mockito.times(1)).findAll(Mockito.any(PageRequest.class));
		Mockito.verify(page, Mockito.times(1)).toList();
	}

	@Test
	void OrderDetailServiceImpl_getById_ReturnOptOrderDetail() {
		
		Mockito.when(orderDetailRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(orderDetailTest));
		
		Optional<OrderDetail>optOrdDet = orderDetailService.getById(orderDetailId);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optOrdDet).isNotNull(),
				()-> Assertions.assertThat(optOrdDet).isNotEmpty(),
				()-> Assertions.assertThat(optOrdDet.get().getId()).isEqualTo(orderDetailId)
				);
		Mockito.verify(orderDetailRepo, Mockito.times(1)).findById(Mockito.anyLong());
	}

	@Test
	void OrderSDetailerviceImpl_getById_ReturnException() {
		
		Mockito.when(orderDetailRepo.findById(Mockito.anyLong())).thenReturn(Optional.empty());
		
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, ()-> orderDetailService.getById(orderDetailId));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(ResourceNotFoundException.class)
				);
		Mockito.verify(orderDetailRepo, Mockito.times(1)).findById(Mockito.anyLong());
	}

	@Test
	void OrderSDetailerviceImpl_create_ReturnOptOrderDetail() {
		
		Mockito.when(orderRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(orderTest));
		Mockito.when(checker.checkProductId(Mockito.anyLong())).thenReturn(true);
		Mockito.when(orderDetailMapper.fromDto(Mockito.any(OrderDetailDto.class))).thenReturn(orderDetailTest);
		Mockito.when(orderDetailRepo.save(Mockito.any(OrderDetail.class))).thenReturn(orderDetailTest);
		
		Optional<OrderDetail>optOrdDet = orderDetailService.create(orderDetailDto);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optOrdDet).isNotNull(),
				()-> Assertions.assertThat(optOrdDet).isNotEmpty(),
				()-> Assertions.assertThat(optOrdDet.get().getId()).isEqualTo(orderDetailId)
				);
		Mockito.verify(orderRepo, Mockito.times(1)).findById(Mockito.anyLong());
		Mockito.verify(checker, Mockito.times(1)).checkProductId(Mockito.anyLong());
		Mockito.verify(orderDetailMapper, Mockito.times(1)).fromDto(Mockito.any(OrderDetailDto.class));
		Mockito.verify(orderDetailRepo, Mockito.times(1)).save(Mockito.any(OrderDetail.class));
	}

	@Test
	void OrderSDetailerviceImpl_create_ReturnResourceNotFoundExceptionForOrder() {
		
		Mockito.when(orderRepo.findById(Mockito.anyLong())).thenReturn(Optional.empty());

		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, ()-> orderDetailService.create(orderDetailDto));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(ResourceNotFoundException.class)
				);		
		Mockito.verify(orderRepo, Mockito.times(1)).findById(Mockito.anyLong());
	}

	@Test
	void OrderSDetailerviceImpl_create_ReturnResourceNotFoundExceptionForProduct() {
		
		Mockito.when(orderRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(orderTest));
		Mockito.when(checker.checkProductId(Mockito.anyLong())).thenReturn(false);

		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, ()-> orderDetailService.create(orderDetailDto));

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(ResourceNotFoundException.class)
				);	
		Mockito.verify(orderRepo, Mockito.times(1)).findById(Mockito.anyLong());
		Mockito.verify(checker, Mockito.times(1)).checkProductId(Mockito.anyLong());
	}

	@Test
	void OrderSDetailerviceImpl_update_ReturnOptOrderDetail() {
		
		Mockito.when(orderDetailRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(orderDetailTest));
		Mockito.when(orderRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(orderTest));
		Mockito.when(checker.checkProductId(Mockito.anyLong())).thenReturn(true);
		Mockito.when(orderDetailRepo.save(Mockito.any(OrderDetail.class))).thenReturn(orderDetailTest);

		Optional<OrderDetail>optOrdDet = orderDetailService.update(orderDetailId, orderDetailDto);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optOrdDet).isNotNull(),
				()-> Assertions.assertThat(optOrdDet).isNotEmpty(),
				()-> Assertions.assertThat(optOrdDet.get().getId()).isEqualTo(orderDetailId)
				);
		Mockito.verify(orderDetailRepo, Mockito.times(1)).findById(Mockito.anyLong());
		Mockito.verify(orderRepo, Mockito.times(1)).findById(Mockito.anyLong());
		Mockito.verify(checker, Mockito.times(1)).checkProductId(Mockito.anyLong());
		Mockito.verify(orderDetailRepo, Mockito.times(1)).save(Mockito.any(OrderDetail.class));
	}

	@Test
	void OrderSDetailerviceImpl_update_ReturnResourceNotFoundExceptionFoOrderDetail() {
		
		Mockito.when(orderDetailRepo.findById(Mockito.anyLong())).thenReturn(Optional.empty());

		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, ()-> orderDetailService.update(orderDetailId, orderDetailDto));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(ResourceNotFoundException.class)
				);		
		Mockito.verify(orderDetailRepo, Mockito.times(1)).findById(Mockito.anyLong());
	}

	@Test
	void OrderSDetailerviceImpl_update_ReturnResourceNotFoundExceptionFoOrder() {
		
		Mockito.when(orderDetailRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(orderDetailTest));
		Mockito.when(orderRepo.findById(Mockito.anyLong())).thenReturn(Optional.empty());

		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, ()-> orderDetailService.update(orderDetailId, orderDetailDto));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(ResourceNotFoundException.class)
				);		
		Mockito.verify(orderDetailRepo, Mockito.times(1)).findById(Mockito.anyLong());
		Mockito.verify(orderRepo, Mockito.times(1)).findById(Mockito.anyLong());
	}

	@Test
	void OrderSDetailerviceImpl_update_ReturnResourceNotFoundExceptionFoOrderProduct() {
		
		Mockito.when(orderDetailRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(orderDetailTest));
		Mockito.when(orderRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(orderTest));
		Mockito.when(checker.checkProductId(Mockito.anyLong())).thenReturn(false);

		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, ()-> orderDetailService.update(orderDetailId, orderDetailDto));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(ResourceNotFoundException.class)
				);		
		Mockito.verify(orderDetailRepo, Mockito.times(1)).findById(Mockito.anyLong());
		Mockito.verify(orderRepo, Mockito.times(1)).findById(Mockito.anyLong());
		Mockito.verify(checker, Mockito.times(1)).checkProductId(Mockito.anyLong());
	}
	
	@Test
	void OrderSDetailerviceImpl_delete_ReturnIntOk() {
		
		Mockito.doNothing().when(orderDetailRepo).deleteById(Mockito.anyLong());
		Mockito.when(orderDetailRepo.findById(Mockito.anyLong())).thenReturn(Optional.empty());

		Integer resultValue = orderDetailService.delete(orderId);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(resultValue).isNotNull(),
				()-> Assertions.assertThat(resultValue).isZero()
		);
		Mockito.verify(orderDetailRepo, Mockito.times(1)).deleteById(Mockito.anyLong());
		Mockito.verify(orderDetailRepo, Mockito.times(1)).findById(Mockito.anyLong());
	}

	@Test
	void OrderSDetailerviceImpl_delete_ReturnIntFail() {
		
		Mockito.doNothing().when(orderDetailRepo).deleteById(Mockito.anyLong());
		Mockito.when(orderDetailRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(orderDetailTest));

		Integer resultValue = orderDetailService.delete(orderId);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(resultValue).isNotNull(),
				()-> Assertions.assertThat(resultValue).isEqualTo(1)
		);
		Mockito.verify(orderDetailRepo, Mockito.times(1)).deleteById(Mockito.anyLong());
		Mockito.verify(orderDetailRepo, Mockito.times(1)).findById(Mockito.anyLong());
	}
	
	@Test
	void OrderSDetailerviceImpl_setOrder_ReturnTrue() {
		
		Mockito.when(orderDetailRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(orderDetailTest));
		Mockito.when(orderRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(orderTest));
		Mockito.when(orderDetailRepo.save(Mockito.any(OrderDetail.class))).thenReturn(orderDetailTest);
		
		Boolean resultValue = orderDetailService.setOrder(orderDetailId, orderId);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(resultValue).isNotNull(),
				()-> Assertions.assertThat(resultValue).isTrue()
				);		
		Mockito.verify(orderDetailRepo, Mockito.times(1)).findById(Mockito.anyLong());
		Mockito.verify(orderRepo, Mockito.times(1)).findById(Mockito.anyLong());
		Mockito.verify(orderDetailRepo, Mockito.times(1)).save(Mockito.any(OrderDetail.class));
	}
	
	@Test
	void OrderSDetailerviceImpl_setOrder_ReturnFalse() {
		
		Mockito.when(orderDetailRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(orderDetailTest));
		Mockito.when(orderRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(orderTest));
		Mockito.when(orderDetailRepo.save(Mockito.any(OrderDetail.class))).thenReturn(null);
		
		Boolean resultValue = orderDetailService.setOrder(orderDetailId, orderId);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(resultValue).isNotNull(),
				()-> Assertions.assertThat(resultValue).isFalse()
				);	
		Mockito.verify(orderDetailRepo, Mockito.times(1)).findById(Mockito.anyLong());
		Mockito.verify(orderRepo, Mockito.times(1)).findById(Mockito.anyLong());
		Mockito.verify(orderDetailRepo, Mockito.times(1)).save(Mockito.any(OrderDetail.class));
	}
	
	@Test
	void OrderSDetailerviceImpl_setOrder_ReturnResourceNotFoundExceptionForOrderDetail() {
		
		Mockito.when(orderDetailRepo.findById(Mockito.anyLong())).thenReturn(Optional.empty());

		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, ()-> orderDetailService.setOrder(orderDetailId, orderId));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(ResourceNotFoundException.class)
				);
	}
	
	@Test
	void OrderSDetailerviceImpl_setOrder_ReturnResourceNotFoundExceptionForOrder() {
		
		Mockito.when(orderDetailRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(orderDetailTest));
		Mockito.when(orderRepo.findById(Mockito.anyLong())).thenReturn(Optional.empty());

		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, ()-> orderDetailService.setOrder(orderDetailId, orderId));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(ResourceNotFoundException.class)
				);
	}
	
	@Test
	void OrderSDetailerviceImpl_getAnyByProductId_ReturnTrue() {
		
		orderDetails.add(orderDetailTest);
		Mockito.when(orderDetailRepo.findByProductId(Mockito.anyLong())).thenReturn(orderDetails);
		
		Boolean resultValue = orderDetailService.getAnyByProductId(orderDetailId);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(resultValue).isNotNull(),
				()-> Assertions.assertThat(resultValue).isTrue()
				);	
		Mockito.verify(orderDetailRepo, Mockito.times(1)).findByProductId(Mockito.anyLong());
	}
	
	@Test
	void OrderSDetailerviceImpl_getAnyByProductId_ReturnFalse() {
		
		Mockito.when(orderDetailRepo.findByProductId(Mockito.anyLong())).thenReturn(orderDetails);
		
		Boolean resultValue = orderDetailService.getAnyByProductId(orderDetailId);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(resultValue).isNotNull(),
				()-> Assertions.assertThat(resultValue).isFalse()
				);	
		Mockito.verify(orderDetailRepo, Mockito.times(1)).findByProductId(Mockito.anyLong());
	}
}
