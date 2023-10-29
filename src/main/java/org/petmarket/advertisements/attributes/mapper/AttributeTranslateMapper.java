package org.petmarket.advertisements.attributes.mapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.advertisements.attributes.dto.AttributeResponseDto;
import org.petmarket.advertisements.attributes.entity.Attribute;
import org.petmarket.advertisements.attributes.entity.AttributeGroupTranslate;
import org.petmarket.advertisements.attributes.entity.AttributeTranslate;
import org.petmarket.language.entity.Language;
import org.petmarket.options.service.OptionsService;
import org.petmarket.translate.LanguageHolder;
import org.petmarket.translate.TranslationService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class AttributeTranslateMapper {

    private final OptionsService optionsService;
    private final TranslationService translationService;
    private final AttributeMapper mapper;

    public AttributeResponseDto mapEntityToDto(Attribute entity, Language language) {
        AttributeTranslate translation = (AttributeTranslate) translationService.getTranslate(
                entity.getTranslations().stream().map(LanguageHolder.class::cast).collect(Collectors.toSet()),
                language,
                optionsService.getDefaultSiteLanguage()
        );

        AttributeGroupTranslate translationGroup = (AttributeGroupTranslate) translationService.getTranslate(
                entity.getGroup().getTranslations().stream().map(LanguageHolder.class::cast).collect(Collectors.toSet()),
                language,
                optionsService.getDefaultSiteLanguage()
        );

        AttributeResponseDto dto = mapper.mapEntityToDto(entity);
        dto.setTitle(translation.getTitle());
        dto.setLangCode(language.getLangCode());
        dto.setGroupTitle(translationGroup.getTitle());

        return dto;
    }

    public List<AttributeResponseDto> mapEntityToDto(List<Attribute> categories,
                                                     Language language) {
        return categories.stream()
                .map(p -> mapEntityToDto(p, language))
                .toList();
    }
}
