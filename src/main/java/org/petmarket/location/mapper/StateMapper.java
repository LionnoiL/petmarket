package org.petmarket.location.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.petmarket.location.dto.StateResponseDto;
import org.petmarket.location.entity.State;

@Mapper(componentModel = "spring")
public interface StateMapper {

    @Mapping(target = "countryId", source = "country.id")
    @Mapping(target = "countryName", source = "country.name")
    StateResponseDto mapEntityToDto(State entity);
}
