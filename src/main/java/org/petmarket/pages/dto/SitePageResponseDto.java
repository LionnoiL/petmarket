package org.petmarket.pages.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.petmarket.pages.entity.SitePageType;

import java.time.LocalDate;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SitePageResponseDto {

    @Schema(example = "1", description = "ID Page")
    private Long id;

    @Schema(example = "2023-10-17", description = "The date the page was created")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate created;

    @Schema(example = "2023-10-17", description = "Page update date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate updated;

    @Schema(example = "HOW_TO_SELL", description = "Page type")
    private SitePageType type;

    @Schema(example = "ua", description = "Page language")
    @JsonProperty("lang_code")
    private String langCode;

    @Schema(example = "Як безпечно купувати", description = "Page title")
    private String title;

    @Schema(example = "Стаття як безпечно купувати..", description = "Page description")
    private String description;
}
