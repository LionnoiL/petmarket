package org.petmarket.advertisements.category.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdvertisementCategoryCreateRequestDto {

    @NotNull(message = "The 'title' cannot be null")
    @NotBlank(message = "The 'title' cannot be empty")
    @Size(min = 1, max = 250, message
            = "Title must be between 1 and 250 characters")
    @Schema(example = "Собаки", description = "Category title")
    private String title;

    @NotNull(message = "The 'tagTitle' cannot be null")
    @NotBlank(message = "The 'tagTitle' cannot be empty")
    @Size(min = 1, max = 20, message
            = "Tag title must be between 1 and 250 characters")
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
