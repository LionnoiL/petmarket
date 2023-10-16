package org.petmarket.pages.mapper;

import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.petmarket.language.entity.Language;
import org.petmarket.pages.dto.SitePageCreateRequestDto;
import org.petmarket.pages.dto.SitePageResponseDto;
import org.petmarket.pages.entity.SitePage;

import java.util.List;
import org.petmarket.pages.entity.SitePageTranslation;
import org.petmarket.translate.TranslateException;

@Mapper(componentModel = "spring")
public interface SitePageMapper {

    SitePageResponseDto mapEntityToDto(SitePage entity);

    SitePage mapDtoRequestToEntity(SitePageCreateRequestDto dto);

    List<SitePageResponseDto> mapEntityToDto(List<SitePage> entities);

    default SitePageTranslation getTranslation(SitePage entity, Language language, Language defaultLanguage){
        Set<SitePageTranslation> translations = entity.getTranslations();
        SitePageTranslation sitePageTranslation;

        sitePageTranslation = translations.stream()
            .filter(t -> t.getLanguage().equals(language))
            .findFirst().orElse(null);

        if (sitePageTranslation == null){
            sitePageTranslation = translations.stream()
                .filter(t -> t.getLanguage().equals(defaultLanguage))
                .findFirst().orElseThrow(() -> new TranslateException("No translation"));
        }
        return sitePageTranslation;
    }
}
