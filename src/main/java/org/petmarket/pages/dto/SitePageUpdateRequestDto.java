package org.petmarket.pages.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.petmarket.pages.entity.SitePageType;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SitePageUpdateRequestDto {

    @NotNull
    @Schema(example = "HOW_TO_SELL", description = "Page type")
    private SitePageType type;

    @NotNull(message = "The 'title' cannot be null")
    @NotBlank(message = "The 'title' cannot be empty")
    @Size(min = 1, max = 250, message
            = "Title must be between 1 and 250 characters")
    @Schema(example = "Як безпечно купувати", description = "Page title")
    private String title;

    @Schema(example = "Стаття як безпечно купувати..", description = "Page description")
    private String description;
}
