package org.petmarket.advertisements.attributes.mapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.advertisements.attributes.dto.AttributeGroupResponseDto;
import org.petmarket.advertisements.attributes.entity.AttributeGroup;
import org.petmarket.advertisements.attributes.entity.AttributeGroupTranslate;
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
public class AttributeGroupTranslateMapper {

    private final OptionsService optionsService;
    private final TranslationService translationService;
    private final AttributeGroupMapper mapper;

    public AttributeGroupResponseDto mapEntityToDto(AttributeGroup entity, Language language) {
        AttributeGroupTranslate translation = (AttributeGroupTranslate) translationService.getTranslate(
                entity.getTranslations().stream().map(LanguageHolder.class::cast).collect(Collectors.toSet()),
                language,
                optionsService.getDefaultSiteLanguage()
        );

        AttributeGroupResponseDto dto = mapper.mapEntityToDto(entity);
        dto.setTitle(translation.getTitle());
        dto.setLangCode(language.getLangCode());
        dto.setDescription(translation.getDescription());

        return dto;
    }

    public List<AttributeGroupResponseDto> mapEntityToDto(List<AttributeGroup> categories,
                                                          Language language) {
        return categories.stream()
                .map(p -> mapEntityToDto(p, language))
                .toList();
    }
}
