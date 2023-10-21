package org.petmarket.location.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Andriy Gaponov
 */
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StateResponseDto {

    private Long id;
    private Long countryId;
    private String name;

    public Long getId() {
        return id;
    }

    public Long getCountryId() {
        return countryId;
    }

    public String getName() {
        return name;
    }
}
