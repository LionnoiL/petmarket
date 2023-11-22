package org.petmarket.advertisements.category.mapper;

import org.mapstruct.*;
import org.petmarket.advertisements.category.dto.AdvertisementCategoryCreateRequestDto;
import org.petmarket.advertisements.category.dto.AdvertisementCategoryResponseDto;
import org.petmarket.advertisements.category.dto.AdvertisementCategoryShortResponseDto;
import org.petmarket.advertisements.category.entity.AdvertisementCategory;
import org.petmarket.advertisements.category.entity.AdvertisementCategoryTranslate;
import org.petmarket.config.MapperConfig;
import org.petmarket.language.entity.Language;
import org.petmarket.options.service.OptionsService;
import org.petmarket.translate.LanguageHolder;
import org.petmarket.translate.TranslationService;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.stream.Collectors;

@Mapper(config = MapperConfig.class,
        uses = {TranslationService.class, OptionsService.class})
public abstract class AdvCategoryMapper {
    private AdvertisementCategoryTranslate translated;
    @Autowired
    private TranslationService translationService;
    @Autowired
    private OptionsService optionsService;

    @Mapping(target = "parentId", source = "parent.id")
    public abstract AdvertisementCategoryResponseDto toDto(
            AdvertisementCategory entity,
            @Context Language langCode);

    public abstract AdvertisementCategoryShortResponseDto shortToDto(
            AdvertisementCategory entity,
            @Context Language langCode);

    public abstract AdvertisementCategory toEntity(
            AdvertisementCategoryCreateRequestDto requestDto,
            @Context Language langCode);

    @AfterMapping
    public void getTranslations(AdvertisementCategory entity, @Context Language langCode) {
        translated = (AdvertisementCategoryTranslate) translationService.getTranslate(
                entity.getTranslations().stream().map(LanguageHolder.class::cast).collect(Collectors.toSet()),
                langCode,
                optionsService.getDefaultSiteLanguage()
        );
    }

    @AfterMapping
    public void setTranslations(
            @MappingTarget AdvertisementCategoryResponseDto
                    .AdvertisementCategoryResponseDtoBuilder responseDtoBuilder) {
        responseDtoBuilder.title(translated.getTitle());
        responseDtoBuilder.description(translated.getDescription());
        responseDtoBuilder.langCode(translated.getLanguage().getLangCode());
        responseDtoBuilder.tagTitle(translated.getTagTitle());
    }

    @AfterMapping
    public void setShortTranslations(
            @MappingTarget AdvertisementCategoryShortResponseDto
                    .AdvertisementCategoryShortResponseDtoBuilder responseDtoBuilder) {
        responseDtoBuilder.title(translated.getTitle());
        responseDtoBuilder.description(translated.getDescription());
        responseDtoBuilder.langCode(translated.getLanguage().getLangCode());
    }
}
