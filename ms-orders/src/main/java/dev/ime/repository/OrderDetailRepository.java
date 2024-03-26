package dev.ime.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.ime.entity.OrderDetail;

public interface OrderDetailRepository extends JpaRepository < OrderDetail, Long >{

	List<OrderDetail> findByProductId(Long productId);
}
