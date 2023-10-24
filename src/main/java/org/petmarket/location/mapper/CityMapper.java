package org.petmarket.location.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.petmarket.location.dto.CityRequestDto;
import org.petmarket.location.dto.CityResponseDto;
import org.petmarket.location.entity.City;

@Mapper(componentModel = "spring")
public interface CityMapper {

    @Mapping(target = "stateId", source = "state.id")
    CityResponseDto mapEntityToDto(City entity);

    City mapDtoRequestToEntity(CityRequestDto request);
}
