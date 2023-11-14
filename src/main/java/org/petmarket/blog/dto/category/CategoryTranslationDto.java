package org.petmarket.blog.dto.category;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CategoryTranslationDto {
    @Schema(example = "ua", description = "Код перекладу")
    private String langCode;
    @Schema(example = "Догляд за собаками", description = "Назва категоріі для блог постів")
    private String categoryName;
    @Schema(example = "У цьому розділі ви знайдете все про догляд за песиками", description = "Опис категоріі")
    private String categoryDescription;
}
