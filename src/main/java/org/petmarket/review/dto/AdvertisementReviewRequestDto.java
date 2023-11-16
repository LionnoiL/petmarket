package org.petmarket.review.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdvertisementReviewRequestDto {

    @NotNull
    @Min(value = 0, message = "The value must be greater than or equal to 0")
    @Max(value = 10, message = "The value must be less than or equal to 10")
    @Schema(example = "2")
    private Integer value;

    @Schema(example = "Товар поганої якості")
    private String description;

    @Override
    public String toString() {
        return "AdvertisementReviewRequestDto{" +
                "value=" + value +
                ", description='" + description + '\'' +
                '}';
    }
}
