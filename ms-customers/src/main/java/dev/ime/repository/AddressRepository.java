package dev.ime.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.ime.entity.Address;

public interface AddressRepository extends JpaRepository <Address, Long>{

}
