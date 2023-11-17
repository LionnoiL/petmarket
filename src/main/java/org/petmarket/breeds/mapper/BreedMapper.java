package org.petmarket.breeds.mapper;

import org.mapstruct.*;
import org.petmarket.breeds.dto.BreedResponseDto;
import org.petmarket.breeds.entity.Breed;
import org.petmarket.breeds.entity.BreedTranslation;
import org.petmarket.config.MapperConfig;
import org.petmarket.errorhandling.ItemNotUpdatedException;
import org.petmarket.language.entity.Language;
import org.petmarket.options.service.OptionsService;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Optional;

import static org.petmarket.utils.MessageUtils.NOT_FOUND;

@Mapper(config = MapperConfig.class,
        uses = OptionsService.class)
public abstract class BreedMapper {
    @Autowired
    private OptionsService optionsService;

    @Mapping(target = "categoryId", source = "category.id")
    public abstract BreedResponseDto toDto(Breed entity, @Context Language langCode);

    @AfterMapping
    public void getTranslations(@MappingTarget BreedResponseDto responseDto,
                                Breed entity, @Context Language langCode) {
        Optional<BreedTranslation> translation = entity.getTranslations().stream()
                .filter(t -> t.getLanguage().getLangCode().equals(langCode.getLangCode()))
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
