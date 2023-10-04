package org.petmarket.blog.service;

import org.petmarket.blog.dto.category.BlogPostCategoryRequestDto;
import org.petmarket.blog.dto.category.BlogPostCategoryResponseDto;

public interface CategoryService extends AbstractService<BlogPostCategoryResponseDto,
        BlogPostCategoryRequestDto> {
    BlogPostCategoryResponseDto saveWithLang(BlogPostCategoryRequestDto blogPostCategoryRequestDto,
                                             String langCode);

    BlogPostCategoryResponseDto getByIdAndLang(Long id, String langCode);
}
