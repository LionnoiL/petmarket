package org.petmarket.advertisements.attributes.service;

import static org.petmarket.utils.MessageUtils.ATTRIBUTE_GROUP_NOT_FOUND;
import static org.petmarket.utils.MessageUtils.ATTRIBUTE_NOT_FOUND;
import static org.petmarket.utils.MessageUtils.LANGUAGE_IS_PRESENT_IN_LIST;
import static org.petmarket.utils.MessageUtils.LANGUAGE_NOT_FOUND;
import static org.petmarket.utils.MessageUtils.NO_TRANSLATION;

import jakarta.transaction.Transactional;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.advertisements.attributes.dto.AttributeRequestDto;
import org.petmarket.advertisements.attributes.dto.AttributeResponseDto;
import org.petmarket.advertisements.attributes.entity.Attribute;
import org.petmarket.advertisements.attributes.entity.AttributeGroup;
import org.petmarket.advertisements.attributes.entity.AttributeTranslate;
import org.petmarket.advertisements.attributes.mapper.AttributeMapper;
import org.petmarket.advertisements.attributes.mapper.AttributeTranslateMapper;
import org.petmarket.advertisements.attributes.repository.AttributeGroupRepository;
import org.petmarket.advertisements.attributes.repository.AttributeRepository;
import org.petmarket.errorhandling.ItemNotFoundException;
import org.petmarket.language.entity.Language;
import org.petmarket.language.repository.LanguageRepository;
import org.petmarket.options.service.OptionsService;
import org.petmarket.translate.TranslateException;
import org.petmarket.utils.ErrorUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

@Slf4j
@RequiredArgsConstructor
@Service
public class AttributeService {

    private final AttributeRepository attributeRepository;
    private final LanguageRepository languageRepository;
    private final AttributeGroupRepository attributeGroupRepository;
    private final AttributeTranslateMapper attributeTranslateMapper;
    private final AttributeMapper attributeMapper;
    private final OptionsService optionsService;

    public AttributeResponseDto findById(Long id, String langCode) {
        Language language = getLanguage(langCode);
        return attributeTranslateMapper.mapEntityToDto(getAttribute(id), language);
    }

    public Collection<AttributeResponseDto> getByGroup(Long id, String langCode) {
        Language language = getLanguage(langCode);
        AttributeGroup group = getGroup(id);
        List<Attribute> attributes = group.getAttributes()
            .stream()
            .sorted(Comparator.comparing(a -> a.getSortValue()))
            .toList();
        return attributeTranslateMapper.mapEntityToDto(attributes, language);
    }

    public List<Attribute> getByIds(List<Long> attributesIds) {
        if (attributesIds == null) {
            return Collections.emptyList();
        }
        return attributeRepository.findAllById(attributesIds);
    }

    @Transactional
    public AttributeResponseDto addAttribute(AttributeRequestDto request,
        BindingResult bindingResult) {
        ErrorUtils.checkItemNotCreatedException(bindingResult);

        Language defaultSiteLanguage = optionsService.getDefaultSiteLanguage();
        AttributeGroup group = getGroup(request.getGroupId());

        Attribute attribute = attributeMapper.mapDtoRequestToEntity(request);
        attribute.setGroup(group);
        attribute.setTranslations(new HashSet<>());
        AttributeTranslate translation = AttributeTranslate.builder()
            .id(null)
            .attribute(attribute)
            .title(request.getTitle())
            .language(defaultSiteLanguage)
            .build();
        addTranslation(attribute, translation);

        attributeRepository.save(attribute);

        log.info("Attribute created");
        return attributeTranslateMapper.mapEntityToDto(attribute, defaultSiteLanguage);
    }

    @Transactional
    public AttributeResponseDto updateAttribute(Long id, String langCode,
        AttributeRequestDto request, BindingResult bindingResult) {
        ErrorUtils.checkItemNotUpdatedException(bindingResult);

        AttributeGroup group = getGroup(request.getGroupId());

        Attribute attribute = getAttribute(id);
        attribute.setSortValue(request.getSortValue());
        attribute.setGroup(group);

        Language language = getLanguage(langCode);
        AttributeTranslate translation;
        if (checkLanguage(attribute, language)) {
            translation = getTranslation(attribute, language);
        } else {
            translation = new AttributeTranslate();
            addTranslation(attribute, translation);
            translation.setAttribute(attribute);
            translation.setLanguage(language);
        }
        translation.setTitle(request.getTitle());

        attributeRepository.save(attribute);

        log.info("The Attribute was updated");
        return attributeTranslateMapper.mapEntityToDto(attribute, language);
    }

    @Transactional
    public void deleteAttribute(Long id) {
        Attribute attribute = getAttribute(id);
        attributeRepository.delete(attribute);
    }

    private Language getLanguage(String langCode) {
        return languageRepository.findByLangCodeAndEnableIsTrue(langCode).orElseThrow(() -> {
            throw new ItemNotFoundException(LANGUAGE_NOT_FOUND);
        });
    }

    private Attribute getAttribute(Long id) {
        return attributeRepository.findById(id).orElseThrow(() -> {
            throw new ItemNotFoundException(ATTRIBUTE_NOT_FOUND);
        });
    }

    private AttributeGroup getGroup(Long id) {
        return attributeGroupRepository.findById(id).orElseThrow(() -> {
            throw new ItemNotFoundException(ATTRIBUTE_GROUP_NOT_FOUND);
        });
    }

    private AttributeTranslate getTranslation(Attribute attribute, Language language) {
        return attribute.getTranslations().stream()
            .filter(t -> t.getLanguage().equals(language))
            .findFirst().orElseThrow(() -> new TranslateException(NO_TRANSLATION));
    }

    private void addTranslation(Attribute attribute, AttributeTranslate translation) {
        if (checkLanguage(attribute, translation.getLanguage())) {
            throw new TranslateException(LANGUAGE_IS_PRESENT_IN_LIST);
        }
        translation.setAttribute(attribute);
        attribute.getTranslations().add(translation);
    }

    private boolean checkLanguage(Attribute group, Language language) {
        return group.getTranslations()
            .stream()
            .anyMatch(t -> t.getLanguage().equals(language));
    }
}
