package org.petmarket.delivery.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryRequestDto {

    @NotBlank(message = "The 'name' cannot be empty")
    @Size(min = 1, max = 250, message
            = "Name must be between 1 and 250 characters")
    @Schema(example = "Самовивіз", description = "Delivery name")
    private String name;

    @Schema(example = "Покупець забирає товар самостійно", description = "Delivery description")
    private String description;

    @Schema(example = "true", description = "Delivery enabled")
    private Boolean enable;

    @Override
    public String toString() {
        return "DeliveryRequestDto{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", enable=" + enable +
                '}';
    }
}
