package org.petmarket.location.mapper;

import org.mapstruct.Mapper;
import org.petmarket.location.dto.CountryResponseDto;
import org.petmarket.location.entity.Country;

@Mapper(componentModel = "spring")
public interface CountryMapper {

    CountryResponseDto mapEntityToDto(Country entity);
}
