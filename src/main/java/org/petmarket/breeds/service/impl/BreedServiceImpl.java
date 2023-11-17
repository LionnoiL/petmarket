package org.petmarket.breeds.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.petmarket.advertisements.category.service.AdvertisementCategoryService;
import org.petmarket.breeds.dto.BreedRequestDto;
import org.petmarket.breeds.dto.BreedResponseDto;
import org.petmarket.breeds.entity.Breed;
import org.petmarket.breeds.entity.BreedTranslation;
import org.petmarket.breeds.mapper.BreedMapper;
import org.petmarket.breeds.repository.BreedRepository;
import org.petmarket.breeds.service.BreedService;
import org.petmarket.errorhandling.ItemNotFoundException;
import org.petmarket.errorhandling.ItemNotUpdatedException;
import org.petmarket.language.entity.Language;
import org.petmarket.language.service.LanguageService;
import org.petmarket.options.service.OptionsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BreedServiceImpl implements BreedService {
    private final BreedRepository breedRepository;
    private final BreedMapper breedMapper;
    private final OptionsService optionsService;
    private final LanguageService languageService;
    private final AdvertisementCategoryService categoryService;

    @Override
    @Transactional
    public BreedResponseDto save(BreedRequestDto requestDto) {
        Breed breed = new Breed();
        List<BreedTranslation> translatioinsList = new ArrayList<>();
        BreedTranslation translation = createTranslation(requestDto,
                optionsService.getDefaultSiteLanguage(),
                breed);
        translatioinsList.add(translation);
        breed.setTranslations(translatioinsList);
        breed.setCategory(categoryService.findCategory(requestDto.getCategoryId()));
        breedRepository.save(breed);
        return breedMapper.toDto(breed, optionsService.getDefaultSiteLanguage().getLangCode());
    }

    @Override
    @Transactional
    public BreedResponseDto addTranslation(Long breedId, String langCode, BreedRequestDto requestDto) {
        Breed breed = findBreedById(breedId);
        List<BreedTranslation> breedTranslations = breed.getTranslations();
        if (breedTranslations.stream()
                .anyMatch(t -> t.getLanguage().getLangCode().equals(checkedLang(langCode)))) {
            throw new ItemNotUpdatedException(langCode + " translation is already exist");
        } else {
            BreedTranslation newTranslation = createTranslation(
                    requestDto,
                    languageService.getByLangCode(langCode),
                    breed);
            breedTranslations.add(newTranslation);
            breed.setTranslations(breedTranslations);
            breedRepository.save(breed);
        }
        return breedMapper.toDto(breed, checkedLang(langCode));
    }

    @Override
    public BreedResponseDto get(Long breedId, String langCode) {
        Breed breed = findBreedById(breedId);
        return breedMapper.toDto(breed, checkedLang(langCode));
    }

    @Override
    @Transactional
    public void delete(Long breedId) {
        Breed breed = findBreedById(breedId);
        breedRepository.deleteById(breed.getId());
    }

    @Override
    public BreedResponseDto update(Long breedId, String langCode, BreedRequestDto requestDto) {
        Breed breed = findBreedById(breedId);

        if (breed.getTranslations().stream().noneMatch(t -> t.getLanguage().getLangCode().equals(langCode))) {
            addTranslation(breedId, langCode, requestDto);
        } else {
            List<BreedTranslation> updatedTranslations = breed.getTranslations().stream()
                    .filter(t -> t.getLanguage().getLangCode().equals(checkedLang(langCode)))
                    .map(translation -> {
                        translation.setTitle(requestDto.getTitle());
                        translation.setDescription(requestDto.getDescription());
                        return translation;
                    })
                    .toList();
            breed.setCategory(categoryService.findCategory(requestDto.getCategoryId()));
            breed.setTranslations(updatedTranslations);
            breedRepository.save(breed);

        }
        return breedMapper.toDto(breed, checkedLang(langCode));
    }

    public Breed findBreedById(Long breedId) {
        return breedRepository.findById(breedId).orElseThrow(
                () -> new ItemNotFoundException("Can't find breed with id: " + breedId)
        );
    }

    @Override
    public List<BreedResponseDto> getAllByCategory(String langCode, Long categoryId) {
        List<Breed> allBreeds;
        if (categoryId != null) {
            allBreeds = findAllByBreedCategoryId(categoryId);
        } else {
            allBreeds = breedRepository.findAll();
        }
        return allBreeds.stream()
                .map(breed -> {
                    breed.setCategory(breed.getCategory());
                    return breed;
                })
                .map(b -> breedMapper.toDto(b, langCode))
                .toList();
    }

    private BreedTranslation createTranslation(BreedRequestDto requestDto, Language language, Breed breed) {
        return BreedTranslation.builder()
                .language(language)
                .title(requestDto.getTitle())
                .description(requestDto.getDescription())
                .breed(breed)
                .build();
    }

    private List<Breed> findAllByBreedCategoryId(Long categoryId) {
        return breedRepository.findBreedByCategoryId(categoryId);
    }

    private String checkedLang(String langCode) {
        return languageService.getByLangCode(langCode).getLangCode();
    }
}
