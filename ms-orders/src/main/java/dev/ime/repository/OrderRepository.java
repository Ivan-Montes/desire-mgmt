package dev.ime.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.ime.entity.Order;

public interface OrderRepository extends JpaRepository < Order, Long >{

}
