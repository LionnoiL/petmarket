package org.petmarket.blog.dto.posts;

import lombok.Data;

@Data
public class BlogPostTranslationResponseDto {
    private String langCode;
    private String title;
    private String text;
}
