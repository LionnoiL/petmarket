package org.petmarket.location.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CityResponseDto {

    @Schema(example = "1", description = "City ID")
    private Long id;
    @Schema(example = "Подільськ", description = "City name")
    private String name;
    @Schema(example = "podilsk", description = "City alias")
    private String alias;
    @Schema(example = "14", description = "State ID")
    private Long stateId;
    @Schema(example = "Одеська", description = "State name")
    private String stateName;
    @Schema(example = "1", description = "Country ID")
    private String countryId;
    @Schema(example = "Україна", description = "Country name")
    private String countryName;
}
