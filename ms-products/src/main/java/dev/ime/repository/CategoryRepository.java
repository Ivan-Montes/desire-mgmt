package dev.ime.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.ime.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

	List<Category>findByName(String name);
}
