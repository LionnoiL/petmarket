package org.petmarket.location.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LocationShortResponseDto {

    @Schema(example = "1", description = "City ID")
    @JsonProperty("city_id")
    private Long cityId;

    @Schema(example = "Подільськ", description = "City name")
    @JsonProperty("city_name")
    private String cityName;

    @Schema(example = "м.")
    @JsonProperty("city_type_short_title")
    private String cityTypeShortName;
}
