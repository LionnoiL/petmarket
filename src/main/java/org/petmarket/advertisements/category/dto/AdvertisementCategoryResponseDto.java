package org.petmarket.advertisements.category.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdvertisementCategoryResponseDto {

    @Schema(example = "1", description = "ID Category")
    private Long id;

    @JsonProperty("lang_code")
    @Schema(example = "ua", description = "Category language")
    private String langCode;

    @Schema(example = "sobaki", description = "Category alias")
    private String alias;

    @Schema(example = "Собаки", description = "Category title")
    private String title;

    @JsonProperty("tag_title")
    @Schema(example = "песики", description = "Title for category tag")
    private String tagTitle;

    @Schema(example = "Собаки..", description = "Category description")
    private String description;

    @JsonProperty("parent_id")
    @Schema(example = "10", description = "Parent ID")
    private Long parentId;

    @JsonProperty("available_in_tags")
    @Schema(example = "true", description = "Availability category for tags")
    private boolean availableInTags;

    @JsonProperty("available_in_favorite")
    @Schema(example = "true", description = "Availability category for favorites")
    private boolean availableInFavorite;
}
