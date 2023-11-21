package org.petmarket.blog.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.petmarket.blog.dto.category.BlogPostCategoryResponseDto;
import org.petmarket.blog.entity.BlogCategory;
import org.petmarket.blog.entity.CategoryTranslation;
import org.petmarket.config.MapperConfig;
import org.petmarket.language.entity.Language;
import org.petmarket.options.service.OptionsService;
import org.petmarket.translate.LanguageHolder;
import org.petmarket.translate.TranslationService;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.stream.Collectors;

@Mapper(config = MapperConfig.class, uses = {TranslationService.class, OptionsService.class})
public abstract class CategoryMapper {
    private CategoryTranslation translated;
    @Autowired
    private TranslationService translationService;
    @Autowired
    private OptionsService optionsService;

    public abstract BlogPostCategoryResponseDto toDto(BlogCategory entity, @Context Language language);

    @AfterMapping
    public void getTranslations(BlogCategory entity, @Context Language language) {
        translated = (CategoryTranslation) translationService.getTranslate(
                entity.getTranslations().stream().map(LanguageHolder.class::cast).collect(Collectors.toSet()),
                language,
                optionsService.getDefaultSiteLanguage()
        );
    }

    @AfterMapping
    public void setTranslations(
            @MappingTarget BlogPostCategoryResponseDto responseDto) {
        responseDto.setTitle(translated.getTitle());
        responseDto.setDescription(translated.getDescription());
        responseDto.setLangCode(translated.getLanguage().getLangCode());
    }
}
