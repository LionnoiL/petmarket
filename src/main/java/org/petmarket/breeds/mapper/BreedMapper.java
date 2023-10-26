package org.petmarket.breeds.mapper;

import org.mapstruct.Mapper;
import org.petmarket.breeds.dto.BreedResponseDto;
import org.petmarket.breeds.entity.Breed;
import org.petmarket.config.MapperConfig;

@Mapper(config = MapperConfig.class)
public interface BreedMapper {
    BreedResponseDto toDto(Breed breed);
}
