package org.petmarket.blog.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.petmarket.blog.dto.posts.BlogPostResponseDto;
import org.petmarket.blog.dto.posts.BlogPostTranslationResponseDto;
import org.petmarket.blog.entity.*;
import org.petmarket.config.MapperConfig;

@Mapper(config = MapperConfig.class, uses = {CategoryTranslationsMapper.class, BlogCommentMapper.class})
public interface BlogPostMapper {
    @Mapping(source = "user.username", target = "userName")
    @Mapping(target = "firstName", source = "user.firstName")
    @Mapping(target = "lastName", source = "user.lastName")
    @Mapping(source = "translations", target = "translations")
    @Mapping(source = "comments", target = "comments")
    BlogPostResponseDto toDto(Post post);

    @Mapping(target = "langCode", source = "language.langCode")
    BlogPostTranslationResponseDto mapTranslation(PostTranslations translation);

}
