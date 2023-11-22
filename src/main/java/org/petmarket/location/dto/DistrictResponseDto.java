package org.petmarket.location.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DistrictResponseDto {

    @Schema(example = "1", description = "District ID")
    private Long id;

    @Schema(example = "Подільський район", description = "District name")
    private String name;

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

    @JsonProperty("koatuu_code")
    private String koatuuCode;
}
