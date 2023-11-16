package org.petmarket.blog.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.petmarket.blog.dto.posts.BlogPostResponseDto;
import org.petmarket.blog.entity.*;
import org.petmarket.config.MapperConfig;

@Mapper(config = MapperConfig.class, uses = {CategoryMapper.class, BlogCommentMapper.class})
public interface BlogPostMapper {
    @Mapping(source = "user.username", target = "userName")
    @Mapping(target = "firstName", source = "user.firstName")
    @Mapping(target = "lastName", source = "user.lastName")
    @Mapping(source = "comments", target = "comments")
    BlogPostResponseDto toDto(Post post);

    @AfterMapping
    default void getTranslations(@MappingTarget BlogPostResponseDto responseDto, Post entity) {
        entity.getTranslations().stream()
                .findFirst()
                .ifPresent(translation -> {
                    responseDto.setTitle(translation.getTitle());
                    responseDto.setDescription(translation.getDescription());
                    responseDto.setLangCode(translation.getLanguage().getLangCode());
                });
    }

}
