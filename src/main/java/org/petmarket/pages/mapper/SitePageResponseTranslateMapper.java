package org.petmarket.pages.mapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.language.entity.Language;
import org.petmarket.options.service.OptionsService;
import org.petmarket.pages.dto.SitePageResponseDto;
import org.petmarket.pages.entity.SitePage;
import org.petmarket.pages.entity.SitePageTranslate;
import org.petmarket.translate.LanguageHolder;
import org.petmarket.translate.TranslationService;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class SitePageResponseTranslateMapper {

    private final OptionsService optionsService;
    private final TranslationService translationService;
    private final SitePageMapper pageMapper;

    public SitePageResponseDto mapEntityToDto(SitePage entity, Language language) {
        SitePageTranslate translation = (SitePageTranslate) translationService.getTranslate(
                entity.getTranslations().stream().map(LanguageHolder.class::cast).collect(Collectors.toSet()),
                language,
                optionsService.getDefaultSiteLanguage()
        );
        SitePageResponseDto sitePageResponseDto = pageMapper.mapEntityToDto(entity);
        sitePageResponseDto.setTitle(translation.getTitle());
        sitePageResponseDto.setDescription(translation.getDescription());
        sitePageResponseDto.setLangCode(language.getLangCode());

        return sitePageResponseDto;
    }
}
