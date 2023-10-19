package org.petmarket.advertisements.category.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdvertisementCategoryCreateRequestDto {

    @NotNull(message = "The 'title' cannot be null")
    @NotBlank(message = "The 'title' cannot be empty")
    @Size(min = 1, max = 250, message
        = "Title must be between 1 and 250 characters")
    private String title;

    @Size(max = 20, message = "The tag name must be less than or equal to 20 characters")
    private String tagTitle;
    private String description;
    private Long parentId;

    @Schema(example = "Собаки", description = "Category title")
    public String getTitle() {
        return title;
    }

    @Schema(example = "песики", description = "Title for category tag")
    public String getTagTitle() {
        return tagTitle;
    }

    @Schema(example = "Собаки..", description = "Category description")
    public String getDescription() {
        return description;
    }

    @Schema(example = "10", description = "Parent ID")
    public Long getParentId() {
        return parentId;
    }
}
