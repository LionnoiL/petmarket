package org.petmarket.advertisements.attributes.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.advertisements.attributes.mapper.AttributeGroupTranslateMapper;
import org.petmarket.advertisements.attributes.repository.AttributeGroupRepository;
import org.petmarket.advertisements.category.repository.AdvertisementCategoryRepository;
import org.petmarket.language.repository.LanguageRepository;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AttributeGroupAdminController {

    public static final String LANGUAGE_NOT_FOUND_MESSAGE = "Language not found";
    private static final String GROUP_NOT_FOUND_MESSAGE = "Attribute group not found";
    private static final String CATEGORY_NOT_FOUND_MESSAGE = "Category not found";
    private final AttributeGroupRepository attributeGroupRepository;
    private final AdvertisementCategoryRepository categoryRepository;
    private final LanguageRepository languageRepository;
    private final AttributeGroupTranslateMapper attributeGroupTranslateMapper;


}
