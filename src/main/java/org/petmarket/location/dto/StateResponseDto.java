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
public class StateResponseDto {

    private Long id;
    private Long countryId;
    private String name;
    private String alias;

    @Schema(example = "14", description = "State ID")
    public Long getId() {
        return id;
    }

    @Schema(example = "1", description = "Country ID")
    public Long getCountryId() {
        return countryId;
    }

    @Schema(example = "Одеська область", description = "State name")
    public String getName() {
        return name;
    }

    @Schema(example = "odeska-oblast", description = "State alias")
    public String getAlias() {
        return alias;
    }
}
