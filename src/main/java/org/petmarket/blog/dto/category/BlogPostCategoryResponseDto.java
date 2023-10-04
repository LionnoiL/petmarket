package org.petmarket.blog.dto.category;

import lombok.Data;

@Data
public class BlogPostCategoryResponseDto {
    private Long id;
    private String langCode;
    private String title;
    private String description;
}
