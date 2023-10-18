package org.petmarket.advertisements.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.advertisements.category.dto.AdvertisementCategoryResponseDto;
import org.petmarket.advertisements.category.entity.AdvertisementCategory;
import org.petmarket.advertisements.category.mapper.AdvertisementCategoryResponseTranslateMapper;
import org.petmarket.advertisements.category.repository.AdvertisementCategoryRepository;
import org.petmarket.errorhandling.ItemNotFoundException;
import org.petmarket.language.entity.Language;
import org.petmarket.language.repository.LanguageRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdvertisementCategoryService {

    public static final String LANGUAGE_NOT_FOUND_MESSAGE = "Language not found";
    public static final String CATEGORY_NOT_FOUND_MESSAGE = "Category not found";

    private final AdvertisementCategoryRepository categoryRepository;
    private final LanguageRepository languageRepository;
    private final AdvertisementCategoryResponseTranslateMapper categoryResponseTranslateMapper;

    public AdvertisementCategoryResponseDto findById(Long id, String langCode) {
        Language language = languageRepository.findByLangCodeAndEnableIsTrue(langCode).orElseThrow(() -> {
            throw new ItemNotFoundException(LANGUAGE_NOT_FOUND_MESSAGE);
        });

        return categoryRepository.findById(id)
                .map(category -> categoryResponseTranslateMapper.mapEntityToDto(category, language))
                .orElseThrow(() -> new ItemNotFoundException(CATEGORY_NOT_FOUND_MESSAGE));
    }

    public Collection<AdvertisementCategoryResponseDto> getAll(String langCode) {
        Language language = languageRepository.findByLangCodeAndEnableIsTrue(langCode).orElseThrow(() -> {
            throw new ItemNotFoundException(LANGUAGE_NOT_FOUND_MESSAGE);
        });
        List<AdvertisementCategory> categories = categoryRepository.findAll();

        return categories.stream()
                .map(p -> categoryResponseTranslateMapper.mapEntityToDto(p, language))
                .toList();
    }
}
