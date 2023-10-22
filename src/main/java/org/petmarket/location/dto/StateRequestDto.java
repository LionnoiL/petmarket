package org.petmarket.location.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class StateRequestDto {

    @NotNull
    private Long countryId;
    @NotNull(message = "The 'title' cannot be null")
    @NotBlank(message = "The 'name' cannot be empty")
    @Size(min = 1, max = 250, message
        = "Name must be between 1 and 250 characters")
    private String name;

    @Schema(example = "1", description = "Country ID")
    public Long getCountryId() {
        return countryId;
    }

    @Schema(example = "Одеська область", description = "State name")
    public String getName() {
        return name;
    }
}
