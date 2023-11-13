package org.petmarket.delivery.mapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.delivery.dto.DeliveryResponseDto;
import org.petmarket.delivery.entity.Delivery;
import org.petmarket.delivery.entity.DeliveryTranslate;
import org.petmarket.language.entity.Language;
import org.petmarket.options.service.OptionsService;
import org.petmarket.translate.LanguageHolder;
import org.petmarket.translate.TranslationService;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class DeliveryResponseTranslateMapper {

    private final OptionsService optionsService;
    private final TranslationService translationService;
    private final DeliveryMapper deliveryMapper;

    public DeliveryResponseDto mapEntityToDto(Delivery entity, Language language) {
        DeliveryTranslate translation = (DeliveryTranslate) translationService.getTranslate(
                entity.getTranslations().stream().map(LanguageHolder.class::cast).collect(Collectors.toSet()),
                language,
                optionsService.getDefaultSiteLanguage()
        );
        DeliveryResponseDto deliveryResponseDto = deliveryMapper.mapEntityToDto(entity);
        deliveryResponseDto.setName(translation.getName());
        deliveryResponseDto.setDescription(translation.getDescription());
        deliveryResponseDto.setLangCode(language.getLangCode());

        return deliveryResponseDto;
    }

    public List<DeliveryResponseDto> mapEntityToDto(List<Delivery> deliveries,
                                                    Language language) {
        if (deliveries == null) {
            return Collections.emptyList();
        }
        return deliveries.stream()
                .map(p -> mapEntityToDto(p, language))
                .toList();
    }
}
