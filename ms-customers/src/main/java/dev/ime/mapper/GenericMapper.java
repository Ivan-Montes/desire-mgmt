package dev.ime.mapper;

import java.util.List;

public interface GenericMapper<T,U> {

	T fromDto(U dto);
	U toDto(T e);
	List<U>toListDto(List<T>list);
	
}
