package org.petmarket.advertisements.category.service;

import static org.petmarket.utils.MessageUtils.CATEGORY_NOT_FOUND;
import static org.petmarket.utils.MessageUtils.LANGUAGE_IS_PRESENT_IN_LIST;
import static org.petmarket.utils.MessageUtils.LANGUAGE_NOT_FOUND;
import static org.petmarket.utils.MessageUtils.NO_TRANSLATION;
import static org.petmarket.utils.MessageUtils.PARENT_CATEGORY_CANNOT_IN_LIST;

import jakarta.transaction.Transactional;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.advertisements.advertisement.repository.AdvertisementRepository;
import org.petmarket.advertisements.category.dto.AdvertisementCategoryCreateRequestDto;
import org.petmarket.advertisements.category.dto.AdvertisementCategoryResponseDto;
import org.petmarket.advertisements.category.dto.AdvertisementCategoryTagResponseDto;
import org.petmarket.advertisements.category.dto.AdvertisementCategoryUpdateRequestDto;
import org.petmarket.advertisements.category.entity.AdvertisementCategory;
import org.petmarket.advertisements.category.entity.AdvertisementCategoryTranslate;
import org.petmarket.advertisements.category.mapper.AdvertisementCategoryMapper;
import org.petmarket.advertisements.category.mapper.AdvertisementCategoryResponseTranslateMapper;
import org.petmarket.advertisements.category.repository.AdvertisementCategoryRepository;
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

    private final AdvertisementCategoryRepository categoryRepository;
    private final LanguageRepository languageRepository;
    private final AdvertisementRepository advertisementRepository;
    private final AdvertisementCategoryMapper categoryMapper;
    private final AdvertisementCategoryResponseTranslateMapper categoryResponseTranslateMapper;
    private final OptionsService optionsService;
    private final TransliterateUtils transliterateUtils;

    public AdvertisementCategoryResponseDto findById(Long id, String langCode) {
        Language language = getLanguage(langCode);
        return categoryResponseTranslateMapper.mapEntityToDto(getCategory(id), language);
    }

    public AdvertisementCategoryResponseDto mapEntityToDto(AdvertisementCategory category, String langCode) {
        Language language = getLanguage(langCode);
        return categoryResponseTranslateMapper.mapEntityToDto(category, language);
    }

    public List<AdvertisementCategory> getByIds(List<Long> categoriesIds) {
        if (categoriesIds == null) {
            return Collections.emptyList();
        }
        return categoryRepository.findAllById(categoriesIds);
    }

    public Collection<AdvertisementCategoryResponseDto> getAll(String langCode) {
        Language language = getLanguage(langCode);
        List<AdvertisementCategory> categories = categoryRepository.findAll();

        return categoryResponseTranslateMapper.mapEntityToDto(categories, language);
    }

    @Transactional
    public AdvertisementCategoryResponseDto addCategory(
        AdvertisementCategoryCreateRequestDto request, BindingResult bindingResult) {
        ErrorUtils.checkItemNotCreatedException(bindingResult);

        AdvertisementCategory parentCategory = null;
        if (request.getParentId() != null && request.getParentId() != 0) {
            parentCategory = getCategory(request.getParentId());
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

    @Transactional
    public AdvertisementCategoryResponseDto updateCategory(
        Long id, String langCode, AdvertisementCategoryUpdateRequestDto request,
        BindingResult bindingResult) {
        ErrorUtils.checkItemNotUpdatedException(bindingResult);

        Language language = getLanguage(langCode);
        AdvertisementCategory parentCategory = null;
        if (request.getParentId() != null) {
            parentCategory = getCategory(request.getParentId());
        }
        AdvertisementCategory category = getCategory(id);

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

    @Transactional
    public void deleteCategory(Long id) {
        AdvertisementCategory category = getCategory(id);
        categoryRepository.deleteById(category.getId());
    }

    @Transactional
    public List<AdvertisementCategoryResponseDto> setParentCategory(List<Long> ids, Long parentId) {
        AdvertisementCategory parentCategory = null;
        if (parentId != 0) {
            if (ids.contains(parentId)) {
                throw new ItemNotUpdatedException(PARENT_CATEGORY_CANNOT_IN_LIST);
            }
            parentCategory = getCategory(parentId);
        }
        List<AdvertisementCategory> categories = categoryRepository.findAllById(ids);
        for (AdvertisementCategory category : categories) {
            category.setParent(parentCategory);
        }
        categoryRepository.saveAll(categories);
        return categoryResponseTranslateMapper.mapEntityToDto(categories,
            optionsService.getDefaultSiteLanguage());
    }

    public Collection<AdvertisementCategoryResponseDto> getFavorite(String langCode, Integer size) {
        Language language = getLanguage(langCode);

        List<Long> categoriesIds = advertisementRepository.findFavoriteCategories(size);
        List<AdvertisementCategory> categories = categoryRepository.findAllById(categoriesIds);

        return categoryResponseTranslateMapper.mapEntityToDto(categories, language);
    }

    public Collection<AdvertisementCategoryTagResponseDto> getFavoriteTags(String langCode,
        Integer size) {
        Language language = getLanguage(langCode);

        List<Long> categoriesIds = advertisementRepository.findFavoriteTags(size);
        List<AdvertisementCategory> categories = categoryRepository.findAllById(categoriesIds);

        return categoryResponseTranslateMapper.mapEntityToTagDto(categories, language);
    }

    public AdvertisementCategory findCategory(Long categoryId) {

        return categoryRepository.findById(categoryId)
            .orElseThrow(() -> new ItemNotFoundException(CATEGORY_NOT_FOUND));
    }

    private Language getLanguage(String langCode) {
        return languageRepository.findByLangCodeAndEnableIsTrue(langCode)
            .orElseThrow(() -> {
                throw new ItemNotFoundException(LANGUAGE_NOT_FOUND);
            });
    }

    private AdvertisementCategoryTranslate getTranslation(AdvertisementCategory category,
        Language language) {
        return category.getTranslations().stream()
            .filter(t -> t.getLanguage().equals(language))
            .findFirst().orElseThrow(() -> new TranslateException(NO_TRANSLATION));
    }

    private boolean checkLanguage(AdvertisementCategory category, Language language) {
        return category.getTranslations()
            .stream()
            .anyMatch(t -> t.getLanguage().equals(language));
    }

    private void addTranslation(AdvertisementCategory category,
        AdvertisementCategoryTranslate translation) {
        if (checkLanguage(category, translation.getLanguage())) {
            throw new TranslateException(LANGUAGE_IS_PRESENT_IN_LIST);
        }
        translation.setCategory(category);
        category.getTranslations().add(translation);
    }

    private AdvertisementCategory getCategory(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> {
            throw new ItemNotFoundException(CATEGORY_NOT_FOUND);
        });
    }
}
