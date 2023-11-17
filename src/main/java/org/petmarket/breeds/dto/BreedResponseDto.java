package org.petmarket.breeds.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BreedResponseDto {
    @Schema(description = "Breed Id", example = "1")
    private Long id;
    @Schema(description = "Translation lang-code", example = "ua")
    @JsonProperty("lang_code")
    private String langCode;
    @Schema(description = "Breed Title", example = "Вівчарка")
    private String title;
    @Schema(description = "Breed Description", example = "Гарна порода собак")
    private String description;
    @Schema (description = "Category Id", example = "1")
    @JsonProperty("category_id")
    private Long categoryId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updated;
}
