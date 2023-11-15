package org.petmarket.location.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LocationResponseDto {

    @Schema(example = "1", description = "City ID")
    @JsonProperty("city_id")
    private Long cityId;

    @Schema(example = "Подільськ", description = "City name")
    @JsonProperty("city_name")
    private String cityName;

    @Schema(example = "Одеська", description = "State name")
    @JsonProperty("state_name")
    private String stateName;

    @Schema(example = "47.750717", description = "latitude")
    private float latitude;

    @Schema(example = "29.529612", description = "longitude")
    private float longitude;
}
