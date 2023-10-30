package org.petmarket.breeds.service.impl;

import lombok.RequiredArgsConstructor;
import org.petmarket.advertisements.category.service.AdvertisementCategoryService;
import org.petmarket.blog.entity.CommentStatus;
import org.petmarket.breeds.dto.BreedRequestDto;
import org.petmarket.breeds.dto.BreedResponseDto;
import org.petmarket.breeds.entity.Breed;
import org.petmarket.breeds.entity.BreedTranslation;
import org.petmarket.breeds.mapper.BreedMapper;
import org.petmarket.breeds.repository.BreedRepository;
import org.petmarket.breeds.service.BreedService;
import org.petmarket.errorhandling.ItemNotFoundException;
import org.petmarket.errorhandling.ItemNotUpdatedException;
import org.petmarket.language.service.LanguageService;
import org.petmarket.options.service.OptionsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BreedServiceImpl implements BreedService {
    private final BreedRepository breedRepository;
    private final BreedMapper breedMapper;
    private final OptionsService optionsService;
    private final LanguageService languageService;
    private final AdvertisementCategoryService categoryService;

    @Override
    public BreedResponseDto save(BreedRequestDto requestDto) {
        Breed breed = new Breed();
        List<BreedTranslation> translatioinsList = new ArrayList<>();
        BreedTranslation translation = createTranslation(requestDto,
                optionsService.getDefaultSiteLanguage().getLangCode(),
                breed);
        translatioinsList.add(translation);
        breed.setTranslations(translatioinsList);
        breed.setCategory(categoryService.findCategory(requestDto.getCategoryId()));
        breedRepository.save(breed);
        return breedMapper.toDto(breed);
    }

    @Override
    public BreedResponseDto addTranslation(Long breedId, String langCode, BreedRequestDto requestDto) {
        Breed breed = findBreedById(breedId);
        List<BreedTranslation> breedTranslations = breed.getTranslations();
        if (breedTranslations.stream()
                .anyMatch(t -> t.getLangCode().equals(checkedLang(langCode)))) {
            throw new ItemNotUpdatedException(langCode + " translation is already exist");
        } else {
            BreedTranslation newTranslation = createTranslation(requestDto, langCode, breed);
            breedTranslations.add(newTranslation);
            breed.setTranslations(breedTranslations);
            breedRepository.save(breed);
        }
        return breedMapper.toDto(breed);
    }

    @Override
    public BreedResponseDto get(Long breedId, String langCode) {
        Breed breed = findBreedById(breedId);
        breed.setTranslations(getTranslation(breedId, langCode));
        breed.setComments(breed.getComments().stream()
                .filter(comment -> comment.getStatus().equals(CommentStatus.APPROVED))
                .collect(Collectors.toList()));

        return breedMapper.toDto(breed);
    }

    @Override
    public void delete(Long breedId) {
        breedRepository.deleteById(breedId);
    }

    @Override
    public BreedResponseDto updateById(Long id, String langCode, BreedResponseDto l) {
        return null;
    }

    private String checkedLang(String langCode) {
        return languageService.getByLangCode(langCode).getLangCode();
    }

    private List<BreedTranslation> getTranslation(Long breedId, String langCode) {
        Breed breed = breedRepository.findById(breedId).orElseThrow(
                () -> new ItemNotFoundException("Can't find breed with id: " + breedId)
        );
        List<BreedTranslation> translations = breed.getTranslations().stream()
                .filter(t -> t.getLangCode().equals(checkedLang(langCode)))
                .collect(Collectors.toList());

        if (translations.isEmpty()) {
            translations = breed.getTranslations().stream()
                    .filter(postTranslations -> postTranslations.getLangCode().equals(
                            optionsService.getDefaultSiteLanguage().getLangCode()))
                    .collect(Collectors.toList());
        }
        return translations;
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
                .peek(breed -> {
                    breed.setCategory(breed.getCategory());
                    breed.setComments(breed.getComments().stream()
                            .filter(breedComment -> breedComment.getStatus().equals(CommentStatus.APPROVED))
                            .collect(Collectors.toList()));
                    breed.setTranslations(getTranslation(breed.getId(), langCode));
                })
                .map(breedMapper::toDto)
                .toList();
    }

    private BreedTranslation createTranslation(BreedRequestDto requestDto, String langCode, Breed breed) {
        BreedTranslation newTranslation = new BreedTranslation();
        newTranslation.setLangCode(langCode);
        newTranslation.setTitle(requestDto.getTitle());
        newTranslation.setDescription(requestDto.getDescription());
        newTranslation.setBreed(breed);
        return newTranslation;
    }

    private List<Breed> findAllByBreedCategoryId(Long categoryId) {
        List<Breed> breeds = breedRepository.findBreedByCategoryId(categoryId);
        if (breeds.isEmpty()) {
            throw new ItemNotFoundException("Can't find breed for category: " + categoryId);
        }
        return breeds;
    }
}
