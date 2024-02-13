package org.petmarket.media.storage.mapper;

import org.mapstruct.Mapper;
import org.petmarket.config.MapperConfig;
import org.petmarket.media.storage.dto.MediaResponseDto;
import org.petmarket.media.storage.entity.Media;

@Mapper(config = MapperConfig.class)
public interface MediaMapper {
    MediaResponseDto toMediaResponseDto(Media media);
}
