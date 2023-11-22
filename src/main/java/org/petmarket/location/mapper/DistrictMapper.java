package org.petmarket.location.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.petmarket.location.dto.DistrictResponseDto;
import org.petmarket.location.entity.District;

@Mapper(componentModel = "spring")
public interface DistrictMapper {

    @Mapping(target = "stateId", source = "state.id")
    @Mapping(target = "stateName", source = "state.name")
    @Mapping(target = "countryId", source = "state.country.id")
    @Mapping(target = "countryName", source = "state.country.name")
    DistrictResponseDto mapEntityToDto(District entity);
}
