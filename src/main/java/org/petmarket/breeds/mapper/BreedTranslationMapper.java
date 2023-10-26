package org.petmarket.breeds.mapper;

import org.mapstruct.Mapper;
import org.petmarket.breeds.dto.BreedTranslationResponseDto;
import org.petmarket.breeds.entity.BreedTranslation;
import org.petmarket.config.MapperConfig;

@Mapper(config = MapperConfig.class)
public interface BreedTranslationMapper {
    BreedTranslationResponseDto toDto(BreedTranslation translation);
}
