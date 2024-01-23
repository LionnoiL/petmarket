package org.petmarket.breeds.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class BreedShortResponseDto {

    @Schema(description = "Breed Id", example = "1")
    private Long id;
    @Schema(description = "Breed Title", example = "Вівчарка")
    private String title;
}
