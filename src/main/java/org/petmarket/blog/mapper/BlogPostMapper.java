package org.petmarket.blog.mapper;

import org.mapstruct.*;
import org.petmarket.blog.dto.posts.BlogPostResponseDto;
import org.petmarket.blog.entity.*;
import org.petmarket.config.MapperConfig;
import org.petmarket.language.entity.Language;
import org.petmarket.options.service.OptionsService;
import org.petmarket.translate.LanguageHolder;
import org.petmarket.translate.TranslationService;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.stream.Collectors;

@Mapper(config = MapperConfig.class, uses = {
        TranslationService.class,
        CategoryMapper.class,
        BlogCommentMapper.class,
        OptionsService.class,
        BlogAttributeMapper.class})
public abstract class BlogPostMapper {
    private PostTranslations translated;
    @Autowired
    private TranslationService translationService;
    @Autowired
    private OptionsService optionsService;

    @Mapping(source = "user.username", target = "userName")
    @Mapping(target = "firstName", source = "user.firstName")
    @Mapping(target = "lastName", source = "user.lastName")
    @Mapping(source = "comments", target = "comments")
    public abstract BlogPostResponseDto toDto(Post post, @Context Language language);

    @AfterMapping
    public void getTranslations(Post entity, @Context Language language) {
        translated = (PostTranslations) translationService.getTranslate(
                entity.getTranslations().stream().map(LanguageHolder.class::cast).collect(Collectors.toSet()),
                language,
                optionsService.getDefaultSiteLanguage()
        );
    }

    @AfterMapping
    public void setTranslations(
            @MappingTarget BlogPostResponseDto responseDto) {
        responseDto.setTitle(translated.getTitle());
        responseDto.setDescription(translated.getDescription());
        responseDto.setLangCode(translated.getLanguage().getLangCode());
        responseDto.setShortText(translated.getShortText());
    }
}
