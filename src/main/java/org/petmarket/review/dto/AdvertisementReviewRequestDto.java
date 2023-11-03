package org.petmarket.review.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdvertisementReviewRequestDto {

    @NotNull
    @Min(value = 0, message = "The value must be greater than or equal to 0")
    @Max(value = 10, message = "The value must be less than or equal to 10")
    private Integer value;
    private String description;

    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "AdvertisementReviewRequestDto{" +
                "value=" + value +
                ", description='" + description + '\'' +
                '}';
    }
}
