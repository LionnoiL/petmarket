package org.petmarket.blog.dto.category;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class BlogPostCategoryResponseDto {
    @Schema(example = "1", description = "ID категоріі")
    private Long id;
    @Schema(example = "Догляд за собаками", description = "Назва категоріі для блог постів")
    private String title;
    @Schema(example = "У цьому розділі ви знайдете все про догляд за песиками", description = "Опис категоріі")
    private String description;
    @Schema(description = "Translation lang-code", example = "ua")
    @JsonProperty("lang_code")
    private String langCode;
}
