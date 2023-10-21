package org.petmarket.location.dto;

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

    public Long getId() {
        return id;
    }

    public Long getStateId() {
        return stateId;
    }

    public String getName() {
        return name;
    }
}
