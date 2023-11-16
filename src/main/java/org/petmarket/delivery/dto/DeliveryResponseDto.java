package org.petmarket.delivery.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryResponseDto {

    @Schema(example = "1", description = "ID Delivery")
    private Long id;

    @Schema(example = "true", description = "Delivery enabled")
    private Boolean enable;

    @Schema(example = "ua", description = "Delivery language")
    @JsonProperty("lang_code")
    private String langCode;

    @Schema(example = "Самовивіз", description = "Delivery name")
    private String name;

    @Schema(example = "Покупець забирає товар самостійно", description = "Delivery description")
    private String description;

    @Override
    public String toString() {
        return "DeliveryResponseDto{" +
                "id=" + id +
                ", enable=" + enable +
                ", langCode='" + langCode + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
