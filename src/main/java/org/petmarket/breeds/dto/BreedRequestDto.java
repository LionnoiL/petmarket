package org.petmarket.breeds.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BreedRequestDto {
    @NotNull
    @Size(min = 1, max = 250, message = "Title must be between 1 and 255 characters")
    @Schema(example = "Вівчарка", description = "Назва породи")
    private String title;
    @NotNull
    @Schema(example = "Вівчарки дуже розумні собаки", description = "Опис породи")
    private String description;
    @NotNull
    @Schema(example = "1", description = "Category id")
    @JsonProperty("category_id")
    private Long categoryId;
}
