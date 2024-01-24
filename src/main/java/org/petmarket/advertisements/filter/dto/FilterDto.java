package org.petmarket.advertisements.filter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.petmarket.advertisements.advertisement.dto.AdvertisementPriceRangeDto;
import org.petmarket.advertisements.attributes.dto.AttributeGroupShortResponseDto;
import org.petmarket.breeds.dto.BreedShortResponseDto;
import org.petmarket.location.dto.CityResponseDto;

import java.util.List;

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
    private List<AttributeGroupShortResponseDto> attributes;
    private List<CityResponseDto> cities;
}
