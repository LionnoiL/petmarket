package org.petmarket.advertisements.attributes.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.advertisements.attributes.dto.AttributeGroupResponseDto;
import org.petmarket.advertisements.attributes.entity.AttributeGroup;
import org.petmarket.advertisements.attributes.mapper.AttributeGroupTranslateMapper;
import org.petmarket.advertisements.attributes.repository.AttributeGroupRepository;
import org.petmarket.advertisements.category.entity.AdvertisementCategory;
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
public class AttributeGroupService {

    public static final String LANGUAGE_NOT_FOUND_MESSAGE = "Language not found";
    private static final String GROUP_NOT_FOUND_MESSAGE = "Attribute group not found";
    private static final String CATEGORY_NOT_FOUND_MESSAGE = "Category not found";
    private final AttributeGroupRepository attributeGroupRepository;
    private final AdvertisementCategoryRepository categoryRepository;
    private final LanguageRepository languageRepository;
    private final AttributeGroupTranslateMapper attributeGroupTranslateMapper;

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
        List<AttributeGroup> groups = attributeGroupRepository.findAllByCategoryOrderBySortValueAsc(category);

        return attributeGroupTranslateMapper.mapEntityToDto(groups, language);
    }

    public Collection<AttributeGroupResponseDto> getForFilter(String langCode) {
        Language language = getLanguage(langCode);
        List<AttributeGroup> groups = attributeGroupRepository.findAllByUseInFilterOrderBySortValueAsc(true);

        return attributeGroupTranslateMapper.mapEntityToDto(groups, language);
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
}
