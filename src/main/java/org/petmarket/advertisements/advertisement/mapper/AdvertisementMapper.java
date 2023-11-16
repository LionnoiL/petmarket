package org.petmarket.advertisements.advertisement.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.petmarket.advertisements.advertisement.dto.AdvertisementRequestDto;
import org.petmarket.advertisements.advertisement.dto.AdvertisementResponseDto;
import org.petmarket.advertisements.advertisement.entity.Advertisement;

@Mapper(componentModel = "spring")
public interface AdvertisementMapper {

    @Mapping(target = "location", ignore = true)
    @Mapping(target = "breed", ignore = true)
    AdvertisementResponseDto mapEntityToDto(Advertisement entity);

    Advertisement mapDtoRequestToEntity(AdvertisementRequestDto request);
}
