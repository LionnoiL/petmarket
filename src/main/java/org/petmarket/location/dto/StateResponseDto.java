package org.petmarket.location.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StateResponseDto {

    @Schema(example = "14", description = "State ID")
    private Long id;

    @Schema(example = "Одеська область", description = "State name")
    private String name;

    @Schema(example = "odeska-oblast", description = "State alias")
    private String alias;

    @Schema(example = "1", description = "Country ID")
    @JsonProperty("country_id")
    private Long countryId;

    @Schema(example = "Україна", description = "Country name")
    @JsonProperty("country_name")
    private String countryName;
}
