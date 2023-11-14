package org.petmarket.breeds.mapper;

import org.mapstruct.*;
import org.petmarket.breeds.dto.BreedResponseDto;
import org.petmarket.breeds.entity.Breed;
import org.petmarket.config.MapperConfig;

@Mapper(config = MapperConfig.class, uses = {
        BreedTranslationMapper.class})
public interface BreedMapper {
    @Mapping(target = "category", source = "category.id")
    BreedResponseDto toDto(Breed breed);

    @AfterMapping
    default void getBreedTranslations(@MappingTarget BreedResponseDto responseDto, Breed breed) {
        breed.getTranslations().stream().findFirst().ifPresent(translation -> {
            responseDto.setTitle(translation.getTitle());
            responseDto.setDescription(translation.getDescription());
            responseDto.setLangCode(translation.getLanguage().getLangCode());
        });
    }
}
