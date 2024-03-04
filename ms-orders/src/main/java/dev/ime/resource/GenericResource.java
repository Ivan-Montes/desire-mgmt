package dev.ime.resource;

import java.util.List;

import org.springframework.http.ResponseEntity;

public interface GenericResource<T> {

	ResponseEntity<List<T>> getAll(Integer page, Integer size);
	ResponseEntity<T>getById(Long id);
	ResponseEntity<T>create(T entity);
	ResponseEntity<T>update(Long id, T entity);
	ResponseEntity<Boolean>delete(Long id);
}
