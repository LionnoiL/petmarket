package org.petmarket.blog.mapper;

import org.mapstruct.*;
import org.petmarket.blog.dto.posts.BlogPostResponseDto;
import org.petmarket.blog.entity.*;
import org.petmarket.config.MapperConfig;
import org.petmarket.errorhandling.ItemNotUpdatedException;
import org.petmarket.language.entity.Language;
import org.petmarket.options.service.OptionsService;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Optional;

import static org.petmarket.utils.MessageUtils.NOT_FOUND;

@Mapper(config = MapperConfig.class, uses = {CategoryMapper.class, BlogCommentMapper.class, OptionsService.class})
public abstract class BlogPostMapper {
    @Autowired
    private OptionsService optionsService;

    @Mapping(source = "user.username", target = "userName")
    @Mapping(target = "firstName", source = "user.firstName")
    @Mapping(target = "lastName", source = "user.lastName")
    @Mapping(source = "comments", target = "comments")
    public abstract BlogPostResponseDto toDto(Post post, @Context Language language);

    @AfterMapping
    public void getTranslations(@MappingTarget BlogPostResponseDto responseDto,
                                Post entity,
                                @Context Language language) {
        Optional<PostTranslations> translation = entity.getTranslations().stream()
                .filter(t -> t.getLanguage().getLangCode().equals(language.getLangCode()))
                .findFirst();

        if (!translation.isPresent()) {
            translation = entity.getTranslations().stream()
                    .filter(postTranslations -> postTranslations.getLanguage().getLangCode().equals(
                            optionsService.getDefaultSiteLanguage().getLangCode()))
                    .findFirst();
        }

        if (translation.isPresent()) {
            responseDto.setTitle(translation.get().getTitle());
            responseDto.setDescription(translation.get().getDescription());
            responseDto.setLangCode(translation.get().getLanguage().getLangCode());
            responseDto.setShortText(translation.get().getShortText());
        } else {
            throw new ItemNotUpdatedException(NOT_FOUND);
        }
    }
}
