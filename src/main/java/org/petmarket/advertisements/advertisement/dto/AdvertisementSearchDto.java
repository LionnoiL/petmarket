package org.petmarket.advertisements.advertisement.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.petmarket.advertisements.category.dto.AdvertisementCategoryResponseDto;
import org.petmarket.location.entity.Location;
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
    private Location location;
    private Page<AdvertisementShortResponseDto> advertisements;
}
