package org.petmarket.pages.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.petmarket.pages.entity.SitePageType;

import java.time.LocalDate;

@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SitePageResponseDto {

    private Long id;
    private LocalDate created;
    private LocalDate updated;
    private SitePageType type;
    private String langCode;
    private String title;
    private String description;

    @Schema(example = "1", description = "ID Page")
    public Long getId() {
        return id;
    }

    @Schema(example = "2023-10-17", description = "The date the page was created")
    public LocalDate getCreated() {
        return created;
    }

    @Schema(example = "2023-10-17", description = "Page update date")
    public LocalDate getUpdated() {
        return updated;
    }

    @Schema(example = "HOW_TO_SELL", description = "Page type")
    public SitePageType getType() {
        return type;
    }

    @Schema(example = "ua", description = "Page language")
    public String getLangCode() {
        return langCode;
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
