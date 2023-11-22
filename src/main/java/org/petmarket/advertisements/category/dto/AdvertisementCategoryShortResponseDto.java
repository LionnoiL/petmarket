package org.petmarket.advertisements.category.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdvertisementCategoryShortResponseDto {

    @Schema(example = "1", description = "ID Category")
    private Long id;

    @JsonProperty("lang_code")
    @Schema(example = "ua", description = "Category language")
    private String langCode;

    @Schema(example = "Собаки", description = "Category title")
    private String title;

    @Schema(example = "Собаки..", description = "Category description")
    private String description;
}
