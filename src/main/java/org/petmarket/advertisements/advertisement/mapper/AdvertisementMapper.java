package org.petmarket.advertisements.advertisement.mapper;

import org.mapstruct.Mapper;
import org.petmarket.advertisements.advertisement.dto.AdvertisementRequestDto;
import org.petmarket.advertisements.advertisement.dto.AdvertisementResponseDto;
import org.petmarket.advertisements.advertisement.entity.Advertisement;

@Mapper(componentModel = "spring")
public interface AdvertisementMapper {

    AdvertisementResponseDto mapEntityToDto(Advertisement entity);

    Advertisement mapDtoRequestToEntity(AdvertisementRequestDto request);
}
