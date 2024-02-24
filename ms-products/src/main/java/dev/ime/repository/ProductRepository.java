package dev.ime.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.ime.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

	List<Product>findByName(String name);
}
