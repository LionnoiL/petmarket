package org.petmarket.advertisements.advertisement.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.petmarket.advertisements.advertisement.dto.AdvertisementDetailsResponseDto;
import org.petmarket.advertisements.advertisement.dto.AdvertisementRequestDto;
import org.petmarket.advertisements.advertisement.dto.AdvertisementShortResponseDto;
import org.petmarket.advertisements.advertisement.entity.Advertisement;
import org.petmarket.advertisements.images.mapper.AdvertisementImageMapper;
import org.petmarket.config.MapperConfig;
import org.petmarket.location.mapper.LocationMapper;
import org.petmarket.users.mapper.UserMapper;

@Mapper(config = MapperConfig.class, uses = {UserMapper.class, LocationMapper.class, AdvertisementImageMapper.class})
public interface AdvertisementMapper {

    @Mapping(target = "breed", ignore = true)
    AdvertisementDetailsResponseDto mapEntityToDto(Advertisement entity);

    AdvertisementShortResponseDto mapEntityToShortDto(Advertisement entity);

    Advertisement mapDtoRequestToEntity(AdvertisementRequestDto request);
}
