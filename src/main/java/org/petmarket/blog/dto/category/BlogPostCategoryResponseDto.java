package org.petmarket.blog.dto.category;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class BlogPostCategoryResponseDto {
    @Schema(example = "1", description = "ID категоріі")
    private Long id;
    private List<CategoryTranslationDto> translations;
}
