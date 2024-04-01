package dev.ime.service;

import java.util.List;

public interface GenericService<T,U> {

	List<U>getAll();
	List<U>getAllPaged(Integer pageNumber, Integer pageSize);
	U getById(Long id);
	U create(T dto);
	U update(Long id, T dto);
	Boolean delete(Long id);
}
