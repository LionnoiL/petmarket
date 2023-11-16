package org.petmarket.blog.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.petmarket.blog.dto.category.BlogPostCategoryResponseDto;
import org.petmarket.blog.entity.BlogCategory;
import org.petmarket.config.MapperConfig;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    BlogPostCategoryResponseDto categoryToDto(BlogCategory blogCategory);

    @AfterMapping
    default void getTranslations(@MappingTarget BlogPostCategoryResponseDto responseDto, BlogCategory entity) {
        entity.getTranslations().stream()
                .findFirst()
                .ifPresent(translation -> {
                    responseDto.setTitle(translation.getTitle());
                    responseDto.setDescription(translation.getDescription());
                    responseDto.setLangCode(translation.getLanguage().getLangCode());
                });
    }
}
