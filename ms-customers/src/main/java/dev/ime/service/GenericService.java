package dev.ime.service;

import java.util.List;
import java.util.Optional;


public interface GenericService<T,U> {

	List<T>getAll();
	List<T>getAllPaged(Integer pageNumber, Integer pageSize);
	Optional<T>getById(Long id);
	Optional<T>create(U entity);
	Optional<T>update(Long id, U entity);
	Integer delete(Long id);
}
