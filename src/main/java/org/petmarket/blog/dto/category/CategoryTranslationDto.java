package org.petmarket.blog.dto.category;

import lombok.Data;

@Data
public class CategoryTranslationDto {
    private String langCode;
    private String categoryName;
    private String categoryDescription;
}
