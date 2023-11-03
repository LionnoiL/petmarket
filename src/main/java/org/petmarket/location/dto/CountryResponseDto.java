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
public class CountryResponseDto {

    private Long id;
    private String name;
    private String alias;

    @Schema(example = "1", description = "Country ID")
    public Long getId() {
        return id;
    }

    @Schema(example = "Україна", description = "Country name")
    public String getName() {
        return name;
    }

    @Schema(example = "ukraine", description = "Country alias")
    public String getAlias() {
        return alias;
    }
}
