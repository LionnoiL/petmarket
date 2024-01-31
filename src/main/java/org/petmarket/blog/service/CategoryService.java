package org.petmarket.blog.service;

import org.petmarket.blog.dto.category.BlogPostCategoryRequestDto;
import org.petmarket.blog.dto.category.BlogPostCategoryResponseDto;
import org.petmarket.blog.entity.BlogCategory;

import java.util.List;

public interface CategoryService extends AbstractService<BlogPostCategoryResponseDto,
        BlogPostCategoryRequestDto> {
    BlogCategory getBlogCategory(Long categoryId);

    List<BlogCategory> getBlogCategories(List<Long> categoryIds);

    BlogPostCategoryResponseDto addTranslation(Long categoryId,
                                               String langCode, BlogPostCategoryRequestDto requestDto);
}
