package org.petmarket.location.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CityRequestDto {

    @NotNull
    @Schema(example = "14", description = "State ID")
    @JsonProperty("state_id")
    private Long stateId;

    @NotNull(message = "The 'title' cannot be null")
    @NotBlank(message = "The 'name' cannot be empty")
    @Size(min = 1, max = 250, message
            = "Name must be between 1 and 250 characters")
    @Schema(example = "Подільськ", description = "City name")
    private String name;

    @Schema(example = "5111200000")
    @JsonProperty("koatuu_code")
    private String koatuuCode;
}
