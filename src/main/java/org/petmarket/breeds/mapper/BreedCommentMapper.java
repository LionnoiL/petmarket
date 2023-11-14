package org.petmarket.breeds.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.petmarket.breeds.dto.BreedCommentResponseDto;
import org.petmarket.breeds.entity.BreedComment;
import org.petmarket.config.MapperConfig;

@Mapper(config = MapperConfig.class)
public interface BreedCommentMapper {
    @Mapping(target = "userName", source = "user.username")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "firstName", source = "user.firstName")
    @Mapping(target = "lastName", source = "user.lastName")
    @Mapping(target = "breedId", source = "breed.id")
    BreedCommentResponseDto toDto(BreedComment comment);
}
