package org.petmarket.location.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.petmarket.location.dto.LocationResponseDto;
import org.petmarket.location.dto.LocationShortResponseDto;
import org.petmarket.location.entity.Location;

@Mapper(componentModel = "spring")
public interface LocationMapper {

    @Mapping(target = "cityId", source = "city.id")
    @Mapping(target = "cityName", source = "city.name")
    @Mapping(target = "stateName", source = "city.state.name")
    LocationResponseDto mapEntityToDto(Location entity);

    @Mapping(target = "cityId", source = "city.id")
    @Mapping(target = "cityName", source = "city.name")
    @Mapping(target = "cityTypeShortName", source = "city.cityTypeShortName")
    LocationShortResponseDto mapEntityToShortDto(Location entity);
}
