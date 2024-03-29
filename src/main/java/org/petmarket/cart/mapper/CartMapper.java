package org.petmarket.cart.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.petmarket.cart.dto.CartResponseDto;
import org.petmarket.cart.entity.Cart;
import org.petmarket.config.MapperConfig;

@Mapper(config = MapperConfig.class, uses = {CartItemMapper.class})
public interface CartMapper {

    @Mapping(target = "totalSum", expression = "java(entity.getTotalSum())")
    @Mapping(target = "totalNumberPositions", expression = "java(entity.getItems().size())")
    @Mapping(target = "totalQuantity", expression = "java(entity.getTotalQuantity())")
    CartResponseDto mapEntityToDto(Cart entity);
}
