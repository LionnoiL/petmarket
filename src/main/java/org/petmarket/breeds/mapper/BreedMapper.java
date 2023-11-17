package org.petmarket.breeds.mapper;

import org.mapstruct.*;
import org.petmarket.breeds.dto.BreedResponseDto;
import org.petmarket.breeds.entity.Breed;
import org.petmarket.breeds.entity.BreedTranslation;
import org.petmarket.config.MapperConfig;
import org.petmarket.options.service.OptionsService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(config = MapperConfig.class,
        uses = {BreedTranslationMapper.class, OptionsService.class},
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class BreedMapper {
    @Autowired
    private OptionsService optionsService;

    @Mapping(target = "category", source = "category.id")
    public abstract BreedResponseDto toDto(Breed entity, @Context String lanCode);

    @AfterMapping
    public void getTranslations(@MappingTarget BreedResponseDto responseDto,
                                Breed entity, @Context String lanCode) {
        List<BreedTranslation> translations = entity.getTranslations().stream()
                .filter(t -> t.getLanguage().getLangCode().equals(lanCode))
                .toList();

        if (translations.isEmpty()) {
            translations = entity.getTranslations().stream()
                    .filter(postTranslations -> postTranslations.getLanguage().getLangCode().equals(
                            optionsService.getDefaultSiteLanguage().getLangCode()))
                    .toList();
        }

        translations.stream()
                .findFirst()
                .ifPresent(translation -> {
                    responseDto.setTitle(translation.getTitle());
                    responseDto.setDescription(translation.getDescription());
                    responseDto.setLangCode(translation.getLanguage().getLangCode());
                });
    }
}
