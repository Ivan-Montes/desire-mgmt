package dev.ime.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.ime.entity.OrderDetail;

public interface OrderDetailRepository extends JpaRepository < OrderDetail, Long >{

}
