package dev.ime.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.ime.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
