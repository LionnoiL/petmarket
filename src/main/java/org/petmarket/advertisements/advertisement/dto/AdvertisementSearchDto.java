package org.petmarket.advertisements.advertisement.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.petmarket.advertisements.category.dto.AdvertisementCategoryResponseDto;
import org.petmarket.location.dto.CityResponseDto;
import org.springframework.data.domain.Page;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdvertisementSearchDto {

    @JsonProperty("search_string")
    private String searchString;
    @JsonProperty("lang_code")
    private String langCode;
    private AdvertisementCategoryResponseDto category;
    private CityResponseDto city;
    private Page<AdvertisementShortResponseDto> advertisements;
}
