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

    public LocalDate getCreated() {
        return created;
    }

    public LocalDate getUpdated() {
        return updated;
    }

    public SitePageType getType() {
        return type;
    }

    public String getLangCode() {
        return langCode;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
