package dev.ime.mapper;


public interface GenericMvcMapper<T,U> {

	U fromMvcDtoToDto(T mvcDto);
	
}
