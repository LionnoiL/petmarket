package org.petmarket.blog.mapper;

import org.mapstruct.*;
import org.petmarket.blog.dto.attribute.BlogPostAttributeResponseDto;
import org.petmarket.blog.entity.BlogAttribute;
import org.petmarket.blog.entity.BlogAttributeTranslation;
import org.petmarket.config.MapperConfig;
import org.petmarket.language.entity.Language;
import org.petmarket.options.service.OptionsService;
import org.petmarket.translate.LanguageHolder;
import org.petmarket.translate.TranslationService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Collectors;

@Mapper(config = MapperConfig.class, uses = {TranslationService.class, OptionsService.class})
public abstract class BlogAttributeMapper {
    private BlogAttributeTranslation translated;
    @Autowired
    private TranslationService translationService;
    @Autowired
    private OptionsService optionsService;

    @Mapping(target = "langCode", expression = "java(language.getLangCode())")
    public abstract BlogPostAttributeResponseDto toDto(BlogAttribute entity, @Context Language language);

    @AfterMapping
    public void getTranslations(BlogAttribute entity, @Context Language language) {
        translated = (BlogAttributeTranslation) translationService.getTranslate(
                entity.getTranslations().stream().map(LanguageHolder.class::cast).collect(Collectors.toSet()),
                language,
                optionsService.getDefaultSiteLanguage()
        );
    }

    @AfterMapping
    public void setTranslations(
            @MappingTarget BlogPostAttributeResponseDto responseDto) {
        responseDto.setTitle(translated.getTitle());
    }
}
