package org.petmarket.location.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CountryRequestDto {

    @Schema(example = "Україна", description = "Country name")
    private String name;
}
