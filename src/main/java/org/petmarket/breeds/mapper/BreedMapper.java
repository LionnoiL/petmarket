package org.petmarket.breeds.mapper;

import org.mapstruct.*;
import org.petmarket.advertisements.category.mapper.AdvCategoryMapper;
import org.petmarket.breeds.dto.BreedResponseDto;
import org.petmarket.breeds.dto.BreedShortResponseDto;
import org.petmarket.breeds.entity.Breed;
import org.petmarket.breeds.entity.BreedTranslation;
import org.petmarket.config.MapperConfig;
import org.petmarket.language.entity.Language;
import org.petmarket.options.service.OptionsService;
import org.petmarket.translate.LanguageHolder;
import org.petmarket.translate.TranslationService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Collectors;

@Mapper(config = MapperConfig.class,
        uses = {OptionsService.class, AdvCategoryMapper.class, TranslationService.class})
public abstract class BreedMapper {
    private BreedTranslation translated;
    @Autowired
    private TranslationService translationService;
    @Autowired
    private OptionsService optionsService;

    @Mapping(target = "category", source = "category")
    public abstract BreedResponseDto toDto(Breed entity, @Context Language langCode);

    public abstract BreedShortResponseDto toDto(BreedResponseDto fromDto);

    @AfterMapping
    public void getTranslations(Breed entity, @Context Language langCode) {
        translated = (BreedTranslation) translationService.getTranslate(
                entity.getTranslations().stream().map(LanguageHolder.class::cast).collect(Collectors.toSet()),
                langCode,
                optionsService.getDefaultSiteLanguage()
        );
    }

    @AfterMapping
    public void setTranslations(
            @MappingTarget BreedResponseDto responseDto) {
        responseDto.setTitle(translated.getTitle());
        responseDto.setDescription(translated.getDescription());
        responseDto.setLangCode(translated.getLanguage().getLangCode());
    }
}
