package org.petmarket.blog.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.petmarket.blog.dto.category.BlogPostCategoryResponseDto;
import org.petmarket.blog.dto.posts.BlogPostResponseDto;
import org.petmarket.blog.entity.BlogCategory;
import org.petmarket.blog.entity.CategoryTranslation;
import org.petmarket.blog.entity.Post;
import org.petmarket.blog.entity.PostTranslations;
import org.petmarket.config.MapperConfig;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

@Mapper(config = MapperConfig.class)
public interface BlogPostMapper {

    @Mapping(target = "title", expression = "java(getTranslation(post, langCode).getTitle())")
    @Mapping(target = "text", expression = "java(getTranslation(post, langCode).getText())")
    @Mapping(target = "userName", expression = "java(getUserName(post))")
    @Mapping(target = "categories", expression = "java(mapCategories(post, langCode))")
    @Mapping(target = "readingMinutes", source = "post.readingMinutes")
    BlogPostResponseDto toDto(Post post, String langCode);

    default PostTranslations getTranslation(Post post, String langCode) {
        return post.getTranslations().stream()
                .filter(p -> p.getLangCode().equals(langCode))
                .findFirst()
                .orElseThrow(
                        () -> new NoSuchElementException("No Translation for this lang: " + langCode)
                );
    }

    default List<BlogPostCategoryResponseDto> mapCategories(Post post, String langCode) {
        CategoryTranslation categoryTranslation = post.getCategories().stream()
                .map(BlogCategory::getTranslations)
                .flatMap(Collection::stream)
                .filter(transactionLang -> transactionLang.getLangCode().equals(langCode))
                .findFirst()
                .orElseThrow(
                        () -> new NoSuchElementException("No Translation for this lang: " + langCode)
                );
        BlogPostCategoryResponseDto responseDto = new BlogPostCategoryResponseDto();
        responseDto.setId(categoryTranslation.getId());
        responseDto.setLangCode(langCode);
        responseDto.setTitle(categoryTranslation.getCategoryName());
        responseDto.setDescription(categoryTranslation.getCategoryDescription());
        List<BlogPostCategoryResponseDto> resultList = new ArrayList<>();
        resultList.add(responseDto);
        return resultList;
    }

    default String getUserName(Post post) {
        return post.getUser().getUsername();
    }

}
