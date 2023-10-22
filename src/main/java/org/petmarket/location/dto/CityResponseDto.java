package org.petmarket.location.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CityResponseDto {

    private Long id;
    private Long stateId;
    private String name;
    private String alias;

    @Schema(example = "1", description = "City ID")
    public Long getId() {
        return id;
    }

    @Schema(example = "14", description = "State ID")
    public Long getStateId() {
        return stateId;
    }

    @Schema(example = "Подільськ", description = "City name")
    public String getName() {
        return name;
    }

    @Schema(example = "podilsk", description = "City alias")
    public String getAlias() {
        return alias;
    }
}
