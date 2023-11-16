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
public class AdvertisementCategoryUpdateRequestDto {

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

    @Schema(example = "10", description = "Parent ID")
    @JsonProperty("parent_id")
    private Long parentId;

    @Schema(example = "true", description = "Availability category for tags")
    @JsonProperty("available_in_tags")
    private boolean availableInTags;

    @Schema(example = "true", description = "Availability category for favorites")
    @JsonProperty("available_in_favorite")
    private boolean availableInFavorite;
}
