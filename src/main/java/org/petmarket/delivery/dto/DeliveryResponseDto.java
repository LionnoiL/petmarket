package org.petmarket.delivery.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryResponseDto {

    private Long id;
    private Boolean enable;
    private String langCode;
    private String name;
    private String description;

    @Schema(example = "Самовивіз", description = "Delivery name")
    public String getName() {
        return name;
    }

    @Schema(example = "1", description = "ID Delivery")
    public Long getId() {
        return id;
    }

    @Schema(example = "true", description = "Delivery enabled")
    public Boolean getEnable() {
        return enable;
    }

    @Schema(example = "ua", description = "Delivery language")
    public String getLangCode() {
        return langCode;
    }

    @Schema(example = "Покупець забирає товар самостійно", description = "Delivery description")
    public String getDescription() {
        return description;
    }
}
