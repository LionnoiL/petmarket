package org.petmarket.location.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CityRequestDto {

    @NotNull
    private Long stateId;
    @NotNull(message = "The 'title' cannot be null")
    @NotBlank(message = "The 'name' cannot be empty")
    @Size(min = 1, max = 250, message
        = "Name must be between 1 and 250 characters")
    private String name;

    public Long getStateId() {
        return stateId;
    }

    public String getName() {
        return name;
    }
}
