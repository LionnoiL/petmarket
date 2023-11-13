package org.petmarket.advertisements.category.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdvertisementCategoryTagResponseDto {

    @JsonProperty("category_id")
    @Schema(example = "1", description = "Category ID")
    private Long categoryId;

    @JsonProperty("lang_code")
    @Schema(example = "ua", description = "Category language")
    private String langCode;

    @JsonProperty("tag_title")
    @Schema(example = "песики", description = "Title for category tag")
    private String tagTitle;

}
