package org.petmarket.location.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CountryResponseDto {

    @Schema(example = "1", description = "Country ID")
    private Long id;

    @Schema(example = "Україна", description = "Country name")
    private String name;

    @Schema(example = "ukraine", description = "Country alias")
    private String alias;
}
