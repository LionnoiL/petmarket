package org.petmarket.blog.service;

import org.petmarket.blog.dto.category.BlogPostCategoryRequestDto;
import org.petmarket.blog.dto.category.BlogPostCategoryResponseDto;
import org.petmarket.blog.entity.BlogCategory;

public interface CategoryService extends AbstractService<BlogPostCategoryResponseDto,
        BlogPostCategoryRequestDto> {
    BlogCategory getBlogCategory(Long categoryId);

    BlogPostCategoryResponseDto addTranslation(Long categoryId,
                                               String langCode, BlogPostCategoryRequestDto requestDto);
}
