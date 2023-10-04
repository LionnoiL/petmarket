package org.petmarket.blog.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.petmarket.blog.dto.category.BlogPostCategoryRequestDto;
import org.petmarket.blog.dto.category.BlogPostCategoryResponseDto;
import org.petmarket.blog.entity.Category;
import org.petmarket.blog.entity.CategoryTranslation;
import org.petmarket.config.MapperConfig;

import java.util.NoSuchElementException;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {

    BlogPostCategoryResponseDto toDto(Category category);

    @Mapping(target = "langCode", source = "langCode")
    @Mapping(target = "title",
            expression = "java(getTranslation(category, langCode).getCategoryName())")
    @Mapping(target = "description",
            expression = "java(getTranslation(category, langCode).getCategoryDescription())")
    BlogPostCategoryResponseDto categoryToDto(Category category, String langCode);

    default CategoryTranslation getTranslation(Category category, String langCode) {
        return category.getTranslations().stream()
                .filter(t -> t.getLangCode().equals(langCode))
                .findFirst()
                .orElseThrow(
                        () -> new NoSuchElementException(
                                "Can't find translation for langCode: " + langCode));
    }

    Category toModel(BlogPostCategoryRequestDto requestDto);
}
