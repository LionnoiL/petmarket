package org.petmarket.location.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CityRequestDto {

    @NotNull
    private Long stateId;
    @NotNull(message = "The 'title' cannot be null")
    @NotBlank(message = "The 'name' cannot be empty")
    @Size(min = 1, max = 250, message
        = "Name must be between 1 and 250 characters")
    private String name;

    @Schema(example = "14", description = "State ID")
    public Long getStateId() {
        return stateId;
    }

    @Schema(example = "Подільськ", description = "City name")
    public String getName() {
        return name;
    }
}
