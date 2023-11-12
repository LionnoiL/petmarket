package org.petmarket.breeds.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BreedResponseDto {
    @Schema(description = "Breed Id", example = "1")
    private Long id;
    private LocalDateTime created;
    private LocalDateTime updated;
    @Schema (description = "Category Id", example = "1")
    private Long category;
    private List<BreedTranslationResponseDto> translations;
}
