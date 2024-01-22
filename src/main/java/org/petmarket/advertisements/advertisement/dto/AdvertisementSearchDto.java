package org.petmarket.advertisements.advertisement.dto;

import lombok.*;
import org.petmarket.advertisements.category.dto.AdvertisementCategoryResponseDto;
import org.springframework.data.domain.Page;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdvertisementSearchDto {

    private String searchTerm;
    private AdvertisementCategoryResponseDto category;
    private Page<AdvertisementShortResponseDto> advertisements;
}
