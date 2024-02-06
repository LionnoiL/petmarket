package org.petmarket.cart.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.petmarket.cart.dto.CartItemResponseDto;
import org.petmarket.cart.entity.CartItem;
import org.petmarket.config.MapperConfig;

@Mapper(config = MapperConfig.class)
public interface CartItemMapper {

    @Mapping(target = "advertisementId", source = "advertisement.id")
    CartItemResponseDto mapEntityToDto(CartItem entity);
}
