package org.petmarket.advertisements.images.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.petmarket.advertisements.images.dto.AdvertisementImageResponseDto;
import org.petmarket.advertisements.images.entity.AdvertisementImage;
import org.petmarket.config.MapperConfig;

@Mapper(config = MapperConfig.class)
public interface AdvertisementImageMapper {

    @Mapping(target = "advertisementId", source = "advertisement.id")
    AdvertisementImageResponseDto toDto(AdvertisementImage image);

}
