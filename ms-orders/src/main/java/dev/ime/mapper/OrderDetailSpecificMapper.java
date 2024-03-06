package dev.ime.mapper;

import java.util.Collection;
import java.util.Set;

import dev.ime.dto.OrderDetailDto;
import dev.ime.entity.OrderDetail;

public interface OrderDetailSpecificMapper {

	Set<OrderDetailDto> toSetDto (Collection<OrderDetail> c);
}
