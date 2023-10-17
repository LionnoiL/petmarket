package org.petmarket.pages.mapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.language.entity.Language;
import org.petmarket.options.service.OptionsService;
import org.petmarket.pages.dto.SitePageResponseDto;
import org.petmarket.pages.entity.SitePage;
import org.petmarket.pages.entity.Translate;
import org.petmarket.translate.TranslationService;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class SitePageResponseTranslateMapper {

    private final OptionsService optionsService;
    private final TranslationService translationService;
    private final SitePageMapper pageMapper;

    public SitePageResponseDto mapEntityToDto(SitePage entity, Language language) {
        Translate translation = translationService.getTranslate(
                entity.getTranslations(),
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
