package org.petmarket.advertisements.category.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdvertisementCategoryResponseDto {

    private Long id;
    private String langCode;
    private String title;
    private String tagTitle;
    private String description;
    private Long parentId;

    @Schema(example = "1", description = "ID Category")
    public Long getId() {
        return id;
    }


    @Schema(example = "ua", description = "Category language")
    public String getLangCode() {
        return langCode;
    }

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
