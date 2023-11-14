package org.petmarket.breeds.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.petmarket.advertisements.category.mapper.AdvertisementCategoryMapper;
import org.petmarket.breeds.dto.BreedResponseDto;
import org.petmarket.breeds.entity.Breed;
import org.petmarket.config.MapperConfig;

@Mapper(config = MapperConfig.class, uses = {
        BreedCommentMapper.class,
        AdvertisementCategoryMapper.class,
        BreedTranslationMapper.class})
public interface BreedMapper {
    @Mapping(target = "category", source = "category.id")
    BreedResponseDto toDto(Breed breed);
}
