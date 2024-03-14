package dev.ime.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.ime.entity.Customer;

public interface CustomerRepository extends JpaRepository <Customer,Long>{

}
