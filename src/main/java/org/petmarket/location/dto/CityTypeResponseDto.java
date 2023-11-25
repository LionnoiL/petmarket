package org.petmarket.location.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CityTypeResponseDto {

    @Schema(example = "1", description = "City type ID")
    private Long id;

    @Schema(example = "місто", description = "City type title")
    private String title;

    @Schema(example = "м.", description = "City type short title")
    private String shortTitle;
}
