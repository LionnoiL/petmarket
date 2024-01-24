package org.petmarket.advertisements.filter.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.advertisements.advertisement.dto.AdvertisementPriceRangeDto;
import org.petmarket.advertisements.advertisement.service.AdvertisementService;
import org.petmarket.advertisements.attributes.entity.Attribute;
import org.petmarket.advertisements.attributes.mapper.AttributeShortMapper;
import org.petmarket.advertisements.attributes.service.AttributeService;
import org.petmarket.advertisements.category.entity.AdvertisementCategory;
import org.petmarket.advertisements.filter.dto.FilterDto;
import org.petmarket.breeds.dto.BreedShortResponseDto;
import org.petmarket.breeds.mapper.BreedMapper;
import org.petmarket.breeds.service.BreedService;
import org.petmarket.language.entity.Language;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class FilterService {

    private final AdvertisementService advertisementService;
    private final BreedService breedService;
    private final AttributeService attributeService;
    private final BreedMapper breedMapper;
    private final AttributeShortMapper attributeShortMapper;

    public FilterDto getLeftSideBarFilter(Language language, AdvertisementCategory category) {

        AdvertisementPriceRangeDto range = advertisementService.getAdvertisementPriceRangeByCategory(category.getId());
        List<BreedShortResponseDto> breeds = breedService.getAllBreedsByAdvertisementsAndCategory(language, category)
                .stream().map(breedMapper::toDto).toList();
        List<Attribute> allAttributes = attributeService.getAttributesForFilter(category);
        List<Attribute> favoriteAttributes = attributeService.getFavoriteAttributes(allAttributes);
        List<Attribute> nonFavoriteAttributes = new ArrayList<>(allAttributes);
        nonFavoriteAttributes.removeAll(favoriteAttributes);

        return FilterDto.builder()
                .langCode(language.getLangCode())
                .categoryId(category.getId())
                .priceRange(range)
                .breeds(breeds)
                .favoriteAttributes(attributeShortMapper.fromAttributes(favoriteAttributes, language))
                .attributes(attributeShortMapper.fromAttributes(nonFavoriteAttributes, language))
                .build();
    }
}
