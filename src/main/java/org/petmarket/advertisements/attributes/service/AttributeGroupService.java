package org.petmarket.advertisements.attributes.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.advertisements.attributes.dto.AttributeGroupRequestDto;
import org.petmarket.advertisements.attributes.dto.AttributeGroupResponseDto;
import org.petmarket.advertisements.attributes.entity.AttributeGroup;
import org.petmarket.advertisements.attributes.entity.AttributeGroupTranslate;
import org.petmarket.advertisements.attributes.mapper.AttributeGroupMapper;
import org.petmarket.advertisements.attributes.mapper.AttributeGroupTranslateMapper;
import org.petmarket.advertisements.attributes.repository.AttributeGroupRepository;
import org.petmarket.advertisements.category.entity.AdvertisementCategory;
import org.petmarket.advertisements.category.repository.AdvertisementCategoryRepository;
import org.petmarket.errorhandling.ItemNotFoundException;
import org.petmarket.language.entity.Language;
import org.petmarket.language.repository.LanguageRepository;
import org.petmarket.options.service.OptionsService;
import org.petmarket.translate.TranslateException;
import org.petmarket.utils.ErrorUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class AttributeGroupService {

    public static final String LANGUAGE_NOT_FOUND_MESSAGE = "Language not found";
    private static final String GROUP_NOT_FOUND_MESSAGE = "Attribute group not found";
    private static final String CATEGORY_NOT_FOUND_MESSAGE = "Category not found";
    private final AttributeGroupRepository attributeGroupRepository;
    private final AdvertisementCategoryRepository categoryRepository;
    private final LanguageRepository languageRepository;
    private final AttributeGroupTranslateMapper attributeGroupTranslateMapper;
    private final AttributeGroupMapper attributeGroupMapper;
    private final OptionsService optionsService;

    public Collection<AttributeGroupResponseDto> getAll(String langCode) {
        Language language = getLanguage(langCode);
        List<AttributeGroup> groups = attributeGroupRepository.findAllByOrderBySortValueAsc();

        return attributeGroupTranslateMapper.mapEntityToDto(groups, language);
    }

    public AttributeGroupResponseDto findById(Long id, String langCode) {
        Language language = getLanguage(langCode);
        return attributeGroupTranslateMapper.mapEntityToDto(getGroup(id), language);
    }

    public Collection<AttributeGroupResponseDto> getByCategory(Long id, String langCode) {
        Language language = getLanguage(langCode);
        AdvertisementCategory category = getCategory(id);
        List<AttributeGroup> groups = attributeGroupRepository.findAllByCategoryAndUseInFilter(
            category, true);

        return attributeGroupTranslateMapper.mapEntityToDto(groups, language);
    }

    public Collection<AttributeGroupResponseDto> getForFilter(String langCode) {
        Language language = getLanguage(langCode);
        List<AttributeGroup> groups = attributeGroupRepository.findAllByUseInFilterOrderBySortValueAsc(
            true);

        return attributeGroupTranslateMapper.mapEntityToDto(groups, language);
    }

    public AttributeGroupResponseDto addGroup(AttributeGroupRequestDto request,
        BindingResult bindingResult) {
        ErrorUtils.checkItemNotCreatedException(bindingResult);

        Language defaultSiteLanguage = optionsService.getDefaultSiteLanguage();

        AttributeGroup group = attributeGroupMapper.mapDtoRequestToEntity(request);
        group.setCategories(getCategories(request.getCategoriesIds()));

        group.setTranslations(new HashSet<>());
        AttributeGroupTranslate translation = AttributeGroupTranslate.builder()
            .id(null)
            .group(group)
            .description(request.getDescription())
            .title(request.getTitle())
            .language(defaultSiteLanguage)
            .build();
        addTranslation(group, translation);

        attributeGroupRepository.save(group);

        log.info("Attribute Group created");
        return attributeGroupTranslateMapper.mapEntityToDto(group, defaultSiteLanguage);
    }

    private List<AdvertisementCategory> getCategories(List<Long> ids) {
        return ids
            .stream()
            .map(this::getCategory)
            .collect(Collectors.toCollection(ArrayList::new));
    }

    public AttributeGroupResponseDto updateGroup(Long id, String langCode,
        AttributeGroupRequestDto request, BindingResult bindingResult) {
        ErrorUtils.checkItemNotUpdatedException(bindingResult);

        AttributeGroup group = getGroup(id);
        group.setCategories(getCategories(request.getCategoriesIds()));
        group.setType(request.getType());
        group.setSortValue(request.getSortValue());
        group.setUseInFilter(request.isUseInFilter());

        Language language = getLanguage(langCode);
        AttributeGroupTranslate translation;
        if (checkLanguage(group, language)) {
            translation = getTranslation(group, language);
        } else {
            translation = new AttributeGroupTranslate();
            addTranslation(group, translation);
            translation.setGroup(group);
            translation.setLanguage(language);
        }
        translation.setTitle(request.getTitle());
        translation.setDescription(request.getDescription());

        attributeGroupRepository.save(group);

        log.info("The Attribute Group was updated");
        return attributeGroupTranslateMapper.mapEntityToDto(group, language);
    }

    public void deleteGroup(Long id) {
        AttributeGroup group = getGroup(id);
        attributeGroupRepository.delete(group);
    }

    private Language getLanguage(String langCode) {
        return languageRepository.findByLangCodeAndEnableIsTrue(langCode).orElseThrow(() -> {
            throw new ItemNotFoundException(LANGUAGE_NOT_FOUND_MESSAGE);
        });
    }

    private AttributeGroup getGroup(Long id) {
        return attributeGroupRepository.findById(id).orElseThrow(() -> {
            throw new ItemNotFoundException(GROUP_NOT_FOUND_MESSAGE);
        });
    }

    private AdvertisementCategory getCategory(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> {
            throw new ItemNotFoundException(CATEGORY_NOT_FOUND_MESSAGE);
        });
    }

    private boolean checkLanguage(AttributeGroup group, Language language) {
        return group.getTranslations()
            .stream()
            .anyMatch(t -> t.getLanguage().equals(language));
    }

    private AttributeGroupTranslate getTranslation(AttributeGroup group,
        Language language) {
        return group.getTranslations().stream()
            .filter(t -> t.getLanguage().equals(language))
            .findFirst().orElseThrow(() -> new TranslateException("No translation"));
    }

    private void addTranslation(AttributeGroup group,
        AttributeGroupTranslate translation) {
        if (checkLanguage(group, translation.getLanguage())) {
            throw new TranslateException("Language is present in list");
        }
        translation.setGroup(group);
        group.getTranslations().add(translation);
    }
}
