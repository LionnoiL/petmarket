package org.petmarket.pages.dto;

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

    public SitePageType getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
