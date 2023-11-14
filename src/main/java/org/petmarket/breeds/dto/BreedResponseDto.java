package org.petmarket.breeds.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BreedResponseDto {
    @Schema(description = "Breed Id", example = "1")
    private Long id;
    private LocalDateTime created;
    private LocalDateTime updated;
    @Schema(description = "Breed Title", example = "Вівчарка")
    private String title;
    @Schema(description = "Breed Description", example = "Гарна порода собак")
    private String description;
    @Schema(description = "Translation lang-code", example = "ua")
    private String langCode;
    @Schema (description = "Category Id", example = "1")
    private Long category;
}
