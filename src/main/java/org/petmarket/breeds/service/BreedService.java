package org.petmarket.breeds.service;

import org.petmarket.advertisements.category.entity.AdvertisementCategory;
import org.petmarket.breeds.dto.BreedFilterDto;
import org.petmarket.breeds.dto.BreedRequestDto;
import org.petmarket.breeds.dto.BreedResponseDto;
import org.petmarket.breeds.entity.Breed;
import org.petmarket.language.entity.Language;

import java.util.List;

public interface BreedService {
    BreedResponseDto save(BreedRequestDto requestDto);

    BreedResponseDto addTranslation(Long breedId, String langCode, BreedRequestDto breedRequestDto);

    BreedResponseDto get(Long breedId, String langCode);

    Breed findBreedById(Long breedId);

    List<BreedResponseDto> getAllByCategory(String langCode, Long categoryId);

    List<BreedFilterDto> getAllBreedsByAdvertisementsAndCategory(Language language, AdvertisementCategory category);

    BreedResponseDto update(Long breedId, String langCode, BreedRequestDto requestDto);

    void delete(Long breedId);
}
