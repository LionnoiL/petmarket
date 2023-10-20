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
public class AdvertisementCategoryTagResponseDto {

    private Long categoryId;
    private String langCode;
    private String tagTitle;

    @Schema(example = "1", description = "Category ID")
    public Long getCategoryId() {
        return categoryId;
    }

    @Schema(example = "ua", description = "Category language")
    public String getLangCode() {
        return langCode;
    }

    @Schema(example = "песики", description = "Title for category tag")
    public String getTagTitle() {
        return tagTitle;
    }
}
