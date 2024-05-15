package org.petmarket.advertisements.advertisement.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.petmarket.advertisements.advertisement.entity.AdvertisementSortOption;

import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdvertisementSearchRequestDto {
    @Schema(example = "dog")
    @JsonProperty("search_term")
    private String searchTerm;

    @Schema(example = "1")
    @JsonProperty("breed_ids")
    private List<Long> breedIds;

    @Schema(example = "1")
    @JsonProperty("attribute_ids")
    private List<Long> attributeIds;

    @Schema(example = "1")
    @JsonProperty("states_ids")
    private List<Long> statesIds;

    @Schema(example = "1")
    @JsonProperty("city_ids")
    private List<Long> cityIds;

    @Schema(example = "1")
    @JsonProperty("min_price")
    private BigDecimal minPrice;

    @Schema(example = "1000")
    @JsonProperty("max_price")
    private BigDecimal maxPrice;

    @Schema(example = "1")
    @JsonProperty(value = "sort_option")
    private AdvertisementSortOption sortOption;

    @Schema(example = "1")
    @JsonProperty("category_id")
    private Long categoryId;
}
