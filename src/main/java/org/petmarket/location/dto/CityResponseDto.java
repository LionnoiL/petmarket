package org.petmarket.location.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    @Schema(example = "місто")
    @JsonProperty("city_type_title")
    private String cityTypeTitle;

    @Schema(example = "м.")
    @JsonProperty("city_type_short_title")
    private String cityTypeShortTitle;

    @Schema(example = "1", description = "District ID")
    @JsonProperty("district_id")
    private Long districtId;

    @Schema(example = "Подільський район", description = "District name")
    @JsonProperty("district_name")
    private String districtName;

    @Schema(example = "14", description = "State ID")
    @JsonProperty("state_id")
    private Long stateId;

    @Schema(example = "Одеська", description = "State name")
    @JsonProperty("state_name")
    private String stateName;

    @Schema(example = "1", description = "Country ID")
    @JsonProperty("country_id")
    private String countryId;

    @Schema(example = "Україна", description = "Country name")
    @JsonProperty("country_name")
    private String countryName;
}
