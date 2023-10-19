package org.petmarket.advertisements.category.mapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.advertisements.category.dto.AdvertisementCategoryResponseDto;
import org.petmarket.advertisements.category.entity.AdvertisementCategory;
import org.petmarket.advertisements.category.entity.AdvertisementCategoryTranslate;
import org.petmarket.language.entity.Language;
import org.petmarket.options.service.OptionsService;
import org.petmarket.translate.LanguageHolder;
import org.petmarket.translate.TranslationService;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class AdvertisementCategoryResponseTranslateMapper {

    private final OptionsService optionsService;
    private final TranslationService translationService;
    private final AdvertisementCategoryMapper mapper;

    public AdvertisementCategoryResponseDto mapEntityToDto(AdvertisementCategory entity, Language language) {
        AdvertisementCategoryTranslate translation = (AdvertisementCategoryTranslate) translationService.getTranslate(
                entity.getTranslations().stream().map(LanguageHolder.class::cast).collect(Collectors.toSet()),
                language,
                optionsService.getDefaultSiteLanguage()
        );
        AdvertisementCategoryResponseDto dto = mapper.mapEntityToDto(entity);
        dto.setTitle(translation.getTitle());
        dto.setDescription(translation.getDescription());
        dto.setLangCode(language.getLangCode());
        dto.setTagTitle(translation.getTagTitle());

        return dto;
    }
}
