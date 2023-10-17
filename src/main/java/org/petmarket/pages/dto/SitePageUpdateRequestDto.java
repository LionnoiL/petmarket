package org.petmarket.pages.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.petmarket.pages.entity.SitePageType;

public class SitePageUpdateRequestDto {

    @NotNull
    private SitePageType type;
    @NotNull(message = "The 'title' cannot be null")
    @NotBlank(message = "The 'title' cannot be empty")
    @Size(min = 1, max = 250, message
            = "Title must be between 1 and 250 characters")
    private String title;
    private String description;

    @Schema(example = "HOW_TO_SELL", description = "Page type")
    public SitePageType getType() {
        return type;
    }

    @Schema(example = "Як безпечно купувати", description = "Page title")
    public String getTitle() {
        return title;
    }

    @Schema(example = "Стаття як безпечно купувати..", description = "Page description")
    public String getDescription() {
        return description;
    }
}
