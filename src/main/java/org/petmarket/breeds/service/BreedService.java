package org.petmarket.breeds.service;

import org.petmarket.breeds.dto.BreedRequestDto;
import org.petmarket.breeds.dto.BreedResponseDto;
import org.petmarket.breeds.entity.Breed;

import java.util.List;

public interface BreedService {
    BreedResponseDto save(BreedRequestDto requestDto);

    BreedResponseDto addTranslation(Long breedId, String langCode, BreedRequestDto breedRequestDto);

    BreedResponseDto get(Long breedId, String langCode);

    Breed findBreedById(Long breedId);

    List<BreedResponseDto> getAllByCategory(String langCode, Long categoryId);

    BreedResponseDto update(Long breedId, String langCode, BreedRequestDto requestDto);

    void delete(Long breedId);
}
