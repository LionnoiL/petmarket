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
public class CountryRequestDto {

    private String name;

    @Schema(example = "Україна", description = "Country name")
    public String getName() {
        return name;
    }
}
