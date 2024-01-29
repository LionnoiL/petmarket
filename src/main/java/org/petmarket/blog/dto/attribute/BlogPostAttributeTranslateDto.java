package org.petmarket.blog.dto.attribute;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BlogPostAttributeTranslateDto {
    @NotNull
    @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
    @Schema(description = "Attribute title", example = "Attribute title")
    private String title;
}
