package org.petmarket.blog.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.petmarket.blog.dto.category.BlogPostCategoryResponseDto;
import org.petmarket.blog.entity.BlogCategory;
import org.petmarket.blog.entity.CategoryTranslation;
import org.petmarket.config.MapperConfig;
import java.util.NoSuchElementException;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    @Mapping(target = "langCode", source = "langCode")
    @Mapping(target = "title",
            expression = "java(getTranslation(blogCategory, langCode).getCategoryName())")
    @Mapping(target = "description",
            expression = "java(getTranslation(blogCategory, langCode).getCategoryDescription())")
    BlogPostCategoryResponseDto categoryToDto(BlogCategory blogCategory, String langCode);

    default CategoryTranslation getTranslation(BlogCategory blogCategory, String langCode) {
        return blogCategory.getTranslations().stream()
                .filter(t -> t.getLangCode().equals(langCode))
                .findFirst()
                .orElseThrow(
                        () -> new NoSuchElementException("No Translation for this lang: " + langCode)
                );
    }
}
