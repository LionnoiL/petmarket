package org.petmarket.location.dto;

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

    public String getName() {
        return name;
    }
}
