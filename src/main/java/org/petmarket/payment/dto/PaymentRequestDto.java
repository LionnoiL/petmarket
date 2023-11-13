package org.petmarket.payment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequestDto {

    @NotBlank(message = "The 'name' cannot be empty")
    @Size(min = 1, max = 250, message
            = "Name must be between 1 and 250 characters")
    @Schema(example = "Готівкою", description = "Payment name")
    private String name;

    @Schema(example = "Оплата готівкою при особистій зустрічі", description = "Payment description")
    private String description;

    @Schema(example = "True", description = "Payment enabled")
    private Boolean enable;

    @Override
    public String toString() {
        return "PaymentRequestDto{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", enable=" + enable +
                '}';
    }
}
