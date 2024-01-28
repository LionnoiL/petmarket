package org.petmarket.blog.dto.attribute;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class BlogPostAttributeRequestDto {
    @Schema(example = "Догляд за вівчаркою", description = "Назва атрибуту")
    private String title;
    @Schema(example = "ua", description = "Код перекладу")
    @JsonProperty("lang_code")
    private String langCode;
    @Schema(example = "1", description = "Sort order")
    @JsonProperty("sort_value")
    private int sortValue;
}
