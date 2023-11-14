package org.petmarket.breeds.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class BreedTranslationResponseDto {
    @Schema(description = "Lang Code", example = "ua")
    private String langCode;
    @Schema(example = "Вівчарка", description = "Назва породи")
    private String title;
    @Schema(example = "Вівчарки дуже розумні собаки", description = "Опис породи")
    private String description;
}
