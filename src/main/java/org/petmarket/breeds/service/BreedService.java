package org.petmarket.breeds.service;

import org.petmarket.breeds.dto.BreedRequestDto;
import org.petmarket.breeds.dto.BreedResponseDto;
import org.petmarket.breeds.entity.Breed;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BreedService {
    BreedResponseDto save(BreedRequestDto requestDto);

    BreedResponseDto addTranslation(Long breedId, String langCode, BreedRequestDto breedRequestDto);

    BreedResponseDto get(Long breedId, String langCode);

    Breed findBreedById(Long breedId);

    List<BreedResponseDto> getAll(Pageable pageable, String langCode);

    void delete(Long breedId);

    BreedResponseDto updateById(Long breedId, String langCode, BreedResponseDto responseDto);
}
