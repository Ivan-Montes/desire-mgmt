package dev.ime.controller;

import org.springframework.ui.Model;

public interface GenericMvcController<T> {

	String getAll(Model model, Integer page, Integer size);
	String getById(Model model, Long id);
	String add(Model model);
	String create(Model model, T mvcDto);
	String update(Model model, Long id, T mvcDto);
	String confirmDelete(Model model, Long id);
	String delete(Model model, Long id);
	
}
