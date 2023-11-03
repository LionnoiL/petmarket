package org.petmarket.payment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequestDto {

    @NotBlank(message = "The 'name' cannot be empty")
    @Size(min = 1, max = 250, message
            = "Name must be between 1 and 250 characters")
    private String name;
    private String description;
    private Boolean enable;

    @Schema(example = "Готівкою", description = "Payment name")
    public String getName() {
        return name;
    }

    @Schema(example = "True", description = "Payment enabled")
    public Boolean getEnable() {
        return enable;
    }

    @Schema(example = "Оплата готівкою при особистій зустрічі", description = "Payment description")
    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "PaymentRequestDto{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", enable=" + enable +
                '}';
    }
}
