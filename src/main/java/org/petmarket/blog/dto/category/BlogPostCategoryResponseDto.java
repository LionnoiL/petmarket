package org.petmarket.blog.dto.category;

import lombok.Data;

import java.util.List;

@Data
public class BlogPostCategoryResponseDto {
    private Long id;
    private List<CategoryTranslationDto> translations;
}
