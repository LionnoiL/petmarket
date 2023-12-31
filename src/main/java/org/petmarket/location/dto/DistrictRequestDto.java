package org.petmarket.location.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * @author Andriy Gaponov
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DistrictRequestDto {

    @NotNull
    @Schema(example = "1", description = "State ID")
    @JsonProperty("state_id")
    private Long stateId;

    @NotNull(message = "The 'name' cannot be null")
    @NotBlank(message = "The 'name' cannot be empty")
    @Size(min = 1, max = 250, message
            = "Name must be between 1 and 250 characters")
    @Schema(example = "Подільський район", description = "District name")
    private String name;

    @Schema(example = "5100000000")
    @JsonProperty("koatuu_code")
    private String koatuuCode;
}
