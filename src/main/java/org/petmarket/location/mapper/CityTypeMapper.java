package org.petmarket.location.mapper;

import org.mapstruct.Mapper;
import org.petmarket.location.dto.CityTypeResponseDto;
import org.petmarket.location.entity.CityType;

@Mapper(componentModel = "spring")
public interface CityTypeMapper {

    CityTypeResponseDto mapEntityToDto(CityType entity);
}
