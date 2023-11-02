package org.petmarket.delivery.mapper;

import org.mapstruct.Mapper;
import org.petmarket.delivery.dto.DeliveryRequestDto;
import org.petmarket.delivery.dto.DeliveryResponseDto;
import org.petmarket.delivery.entity.Delivery;

@Mapper(componentModel = "spring")
public interface DeliveryMapper {

    DeliveryResponseDto mapEntityToDto(Delivery entity);

    Delivery mapDtoRequestToEntity(DeliveryRequestDto request);
}
