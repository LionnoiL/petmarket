package org.petmarket.blog.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.petmarket.blog.dto.category.BlogPostCategoryResponseDto;
import org.petmarket.blog.dto.category.CategoryTranslationDto;
import org.petmarket.blog.entity.BlogCategory;
import org.petmarket.blog.entity.CategoryTranslation;
import org.petmarket.config.MapperConfig;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    @Mapping(source = "translations", target = "translations")
    BlogPostCategoryResponseDto categoryToDto(BlogCategory blogCategory);

    CategoryTranslationDto mapTranslation(CategoryTranslation translation);
}
