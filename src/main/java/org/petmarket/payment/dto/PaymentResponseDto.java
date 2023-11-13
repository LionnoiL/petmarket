package org.petmarket.payment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponseDto {

    @Schema(example = "1", description = "ID Payment")
    private Long id;

    @Schema(example = "true", description = "Payment enabled")
    private Boolean enable;

    @Schema(example = "ua", description = "Payment language")
    @JsonProperty("lang_code")
    private String langCode;

    @Schema(example = "Готівкою", description = "Payment name")
    private String name;

    @Schema(example = "Оплата готівкою при особистій зустрічі", description = "Payment description")
    private String description;

    @Override
    public String toString() {
        return "PaymentResponseDto{" +
                "id=" + id +
                ", enable=" + enable +
                ", langCode='" + langCode + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
