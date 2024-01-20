package org.petmarket.advertisements.advertisement.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdvertisementPriceRangeDto {

        @JsonProperty("min_price")
        private BigDecimal minPrice;
        @JsonProperty("max_price")
        private BigDecimal maxPrice;
}
