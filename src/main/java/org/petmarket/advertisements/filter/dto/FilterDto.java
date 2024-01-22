package org.petmarket.advertisements.filter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.petmarket.advertisements.advertisement.dto.AdvertisementPriceRangeDto;
import org.petmarket.advertisements.attributes.dto.AttributeGroupShortResponseDto;
import org.petmarket.breeds.dto.BreedShortResponseDto;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FilterDto {

    @JsonProperty("lang_code")
    private String langCode;
    @JsonProperty("category_id")
    private Long categoryId;
    @JsonProperty("price_range")
    private AdvertisementPriceRangeDto priceRange;
    private List<BreedShortResponseDto> breeds;
    @JsonProperty("favorite_attributes")
    private List<AttributeGroupShortResponseDto> favoriteAttributes;
    private AttributeGroupShortResponseDto attributes;
}
