package org.petmarket.advertisements.category.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.advertisements.category.dto.AdvertisementCategoryCreateRequestDto;
import org.petmarket.advertisements.category.dto.AdvertisementCategoryResponseDto;
import org.petmarket.advertisements.category.dto.AdvertisementCategoryUpdateRequestDto;
import org.petmarket.advertisements.category.entity.AdvertisementCategory;
import org.petmarket.advertisements.category.entity.AdvertisementCategoryTranslate;
import org.petmarket.advertisements.category.mapper.AdvertisementCategoryMapper;
import org.petmarket.advertisements.category.mapper.AdvertisementCategoryResponseTranslateMapper;
import org.petmarket.advertisements.category.repository.AdvertisementCategoryRepository;
import org.petmarket.errorhandling.ItemNotCreatedException;
import org.petmarket.errorhandling.ItemNotFoundException;
import org.petmarket.errorhandling.ItemNotUpdatedException;
import org.petmarket.language.entity.Language;
import org.petmarket.language.repository.LanguageRepository;
import org.petmarket.options.service.OptionsService;
import org.petmarket.translate.TranslateException;
import org.petmarket.utils.ErrorUtils;
import org.petmarket.utils.TransliterateUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdvertisementCategoryService {

    public static final String LANGUAGE_NOT_FOUND_MESSAGE = "Language not found";
    public static final String CATEGORY_NOT_FOUND_MESSAGE = "Category not found";

    private final AdvertisementCategoryRepository categoryRepository;
    private final LanguageRepository languageRepository;
    private final AdvertisementCategoryMapper categoryMapper;
    private final AdvertisementCategoryResponseTranslateMapper categoryResponseTranslateMapper;
    private final ErrorUtils errorUtils;
    private final OptionsService optionsService;
    private final TransliterateUtils transliterateUtils;

    private AdvertisementCategoryTranslate getTranslation(AdvertisementCategory page,
        Language language) {
        return page.getTranslations().stream()
            .filter(t -> t.getLanguage().equals(language))
            .findFirst().orElseThrow(() -> new TranslateException("No translation"));
    }

    private boolean checkLanguage(AdvertisementCategory page, Language language) {
        return page.getTranslations()
            .stream()
            .anyMatch(t -> t.getLanguage().equals(language));
    }

    private void addTranslation(AdvertisementCategory page,
        AdvertisementCategoryTranslate translation) {
        if (checkLanguage(page, translation.getLanguage())) {
            throw new TranslateException("Language is present in list");
        }
        translation.setCategory(page);
        page.getTranslations().add(translation);
    }

    public AdvertisementCategoryResponseDto findById(Long id, String langCode) {
        Language language = languageRepository.findByLangCodeAndEnableIsTrue(langCode)
            .orElseThrow(() -> {
                throw new ItemNotFoundException(LANGUAGE_NOT_FOUND_MESSAGE);
            });

        return categoryRepository.findById(id)
            .map(category -> categoryResponseTranslateMapper.mapEntityToDto(category, language))
            .orElseThrow(() -> new ItemNotFoundException(CATEGORY_NOT_FOUND_MESSAGE));
    }

    public Collection<AdvertisementCategoryResponseDto> getAll(String langCode) {
        Language language = languageRepository.findByLangCodeAndEnableIsTrue(langCode)
            .orElseThrow(() -> {
                throw new ItemNotFoundException(LANGUAGE_NOT_FOUND_MESSAGE);
            });
        List<AdvertisementCategory> categories = categoryRepository.findAll();

        return categories.stream()
            .map(p -> categoryResponseTranslateMapper.mapEntityToDto(p, language))
            .toList();
    }

    public AdvertisementCategoryResponseDto addCategory(
        AdvertisementCategoryCreateRequestDto request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ItemNotCreatedException(errorUtils.getErrorsString(bindingResult));
        }

        AdvertisementCategory parentCategory = null;
        if (request.getParentId() != null) {
            parentCategory = categoryRepository.findById(
                    request.getParentId())
                .orElseThrow(() -> new ItemNotFoundException(CATEGORY_NOT_FOUND_MESSAGE));
        }
        Language defaultSiteLanguage = optionsService.getDefaultSiteLanguage();

        AdvertisementCategory category = categoryMapper.mapDtoRequestToEntity(request);
        category.setAlias(
            transliterateUtils.getAlias(
                AdvertisementCategory.class.getSimpleName(),
                request.getTitle())
        );
        category.setParent(parentCategory);
        category.setTranslations(new HashSet<>());

        AdvertisementCategoryTranslate translation = AdvertisementCategoryTranslate.builder()
            .id(null)
            .category(category)
            .description(request.getDescription())
            .title(request.getTitle())
            .tagTitle(request.getTagTitle())
            .language(defaultSiteLanguage)
            .build();
        addTranslation(category, translation);

        categoryRepository.save(category);

        log.info("Advertisement Category created");
        return categoryResponseTranslateMapper.mapEntityToDto(category, defaultSiteLanguage);
    }

    public AdvertisementCategoryResponseDto updateCategory(Long id, String langCode,
        AdvertisementCategoryUpdateRequestDto request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ItemNotUpdatedException(errorUtils.getErrorsString(bindingResult));
        }

        Language language = languageRepository.findByLangCodeAndEnableIsTrue(langCode)
            .orElseThrow(() -> {
                throw new ItemNotFoundException(LANGUAGE_NOT_FOUND_MESSAGE);
            });
        AdvertisementCategory parentCategory = null;
        if (request.getParentId() != null) {
            parentCategory = categoryRepository.findById(
                    request.getParentId())
                .orElseThrow(() -> new ItemNotFoundException(CATEGORY_NOT_FOUND_MESSAGE));
        }
        AdvertisementCategory category = categoryRepository.findById(id).orElseThrow(() -> {
            throw new ItemNotFoundException(CATEGORY_NOT_FOUND_MESSAGE);
        });

        category.setParent(parentCategory);
        AdvertisementCategoryTranslate translation;
        if (checkLanguage(category, language)) {
            translation = getTranslation(category, language);
        } else {
            translation = new AdvertisementCategoryTranslate();
            addTranslation(category, translation);
            translation.setCategory(category);
            translation.setLanguage(language);
        }
        translation.setTitle(request.getTitle());
        translation.setDescription(request.getDescription());
        translation.setTagTitle(request.getTagTitle());

        categoryRepository.save(category);

        log.info("The Advertisement Category was updated");
        return categoryResponseTranslateMapper.mapEntityToDto(category, language);
    }

    public void deleteCategory(Long id) {
        AdvertisementCategory page = categoryRepository.findById(id).orElseThrow(() -> {
            throw new ItemNotFoundException(CATEGORY_NOT_FOUND_MESSAGE);
        });
        categoryRepository.deleteById(page.getId());
    }

    public List<AdvertisementCategoryResponseDto> setParentCategory(List<Long> ids, Long parentId) {
        AdvertisementCategory parentCategory = null;
        if (parentId != 0) {
            if (ids.contains(parentId)) {
                throw new ItemNotUpdatedException(
                    "The parent category id cannot be in the list of identifiers");
            }
            parentCategory = categoryRepository.findById(parentId)
                .orElseThrow(() -> new ItemNotFoundException(CATEGORY_NOT_FOUND_MESSAGE));
        }
        List<AdvertisementCategory> categories = categoryRepository.findAllById(ids);
        for (AdvertisementCategory category : categories) {
            category.setParent(parentCategory);
        }
        categoryRepository.saveAll(categories);
        return categories.stream()
            .map(p -> categoryResponseTranslateMapper.mapEntityToDto(p,
                optionsService.getDefaultSiteLanguage()))
            .toList();
    }
}
