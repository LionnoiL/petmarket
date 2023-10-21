package org.petmarket.blog.mapper;

import org.mapstruct.Mapper;
import org.petmarket.blog.dto.category.CategoryTranslationDto;
import org.petmarket.blog.entity.CategoryTranslation;
import org.petmarket.config.MapperConfig;

@Mapper(config = MapperConfig.class)
public interface CategoryTranslationsMapper {
    CategoryTranslationDto categoryTranslationToCategoryTranslationDto(CategoryTranslation translation);
}
