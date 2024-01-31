package org.petmarket.blog.dto.attribute;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BlogPostAttributeRequestDto {
    @NotNull
    @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
    @Schema(example = "Догляд за вівчаркою", description = "Назва атрибуту")
    private String title;
    @Schema(example = "1", description = "Sort order")
    @JsonProperty("sort_value")
    private int sortValue;
}
