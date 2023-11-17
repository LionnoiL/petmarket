package org.petmarket.blog.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.petmarket.blog.dto.category.BlogPostCategoryResponseDto;
import org.petmarket.blog.entity.BlogCategory;
import org.petmarket.blog.entity.CategoryTranslation;
import org.petmarket.config.MapperConfig;
import org.petmarket.errorhandling.ItemNotUpdatedException;
import org.petmarket.language.entity.Language;
import org.petmarket.options.service.OptionsService;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Optional;
import static org.petmarket.utils.MessageUtils.NOT_FOUND;

@Mapper(config = MapperConfig.class, uses = OptionsService.class)
public abstract class CategoryMapper {
    @Autowired
    private OptionsService optionsService;

    public abstract BlogPostCategoryResponseDto toDto(BlogCategory entity, @Context Language language);

    @AfterMapping
    public void getTranslations(@MappingTarget BlogPostCategoryResponseDto responseDto,
                                BlogCategory entity,
                                @Context Language language) {
        Optional<CategoryTranslation> translation = entity.getTranslations().stream()
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
        } else {
            throw new ItemNotUpdatedException(NOT_FOUND);
        }
    }
}
