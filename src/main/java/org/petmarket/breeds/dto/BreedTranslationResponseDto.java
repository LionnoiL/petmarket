package org.petmarket.breeds.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class BreedTranslationResponseDto {
    @Schema(description = "Lang Code", example = "ua")
    @JsonProperty("lang_code")
    private String langCode;
    @Schema(example = "Вівчарка", description = "Назва породи")
    private String title;
    @Schema(example = "Вівчарки дуже розумні собаки", description = "Опис породи")
    private String description;
}
