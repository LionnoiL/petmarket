package org.petmarket.payment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponseDto {

    private Long id;
    private Boolean enable;
    private String langCode;
    private String name;
    private String description;

    @Schema(example = "Готівкою", description = "Payment name")
    public String getName() {
        return name;
    }

    @Schema(example = "1", description = "ID Payment")
    public Long getId() {
        return id;
    }

    @Schema(example = "true", description = "Payment enabled")
    public Boolean getEnable() {
        return enable;
    }

    @Schema(example = "ua", description = "Payment language")
    public String getLangCode() {
        return langCode;
    }

    @Schema(example = "Оплата готівкою при особистій зустрічі", description = "Payment description")
    public String getDescription() {
        return description;
    }
}
