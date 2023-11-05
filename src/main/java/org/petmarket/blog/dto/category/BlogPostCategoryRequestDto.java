package org.petmarket.blog.dto.category;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BlogPostCategoryRequestDto {
    @NotNull
    @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
    @Schema(example = "Догляд за собаками", description = "Назва категоріі для блог постів")
    private String title;
    @NotNull
    @Size(min = 1, max = 500, message = "Title must be between 1 and 255 characters")
    @Schema(example = "У цьому розділі ви знайдете все про догляд за песиками", description = "Опис категоріі")
    private String description;
}
