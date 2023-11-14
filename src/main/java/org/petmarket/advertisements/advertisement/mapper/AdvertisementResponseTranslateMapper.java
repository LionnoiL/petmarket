package org.petmarket.advertisements.advertisement.mapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.advertisements.advertisement.dto.AdvertisementResponseDto;
import org.petmarket.advertisements.advertisement.entity.Advertisement;
import org.petmarket.advertisements.advertisement.entity.AdvertisementTranslate;
import org.petmarket.advertisements.attributes.mapper.AttributeTranslateMapper;
import org.petmarket.advertisements.category.mapper.AdvertisementCategoryResponseTranslateMapper;
import org.petmarket.breeds.mapper.BreedMapper;
import org.petmarket.delivery.mapper.DeliveryResponseTranslateMapper;
import org.petmarket.language.entity.Language;
import org.petmarket.options.service.OptionsService;
import org.petmarket.payment.mapper.PaymentResponseTranslateMapper;
import org.petmarket.translate.LanguageHolder;
import org.petmarket.translate.TranslationService;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class AdvertisementResponseTranslateMapper {

    private final OptionsService optionsService;
    private final TranslationService translationService;
    private final AdvertisementMapper mapper;
    private final AdvertisementCategoryResponseTranslateMapper categoryMapper;
    private final PaymentResponseTranslateMapper paymentMapper;
    private final DeliveryResponseTranslateMapper deliveryMapper;
    private final AttributeTranslateMapper attributeMapper;
    private final BreedMapper breedMapper;

    public AdvertisementResponseDto mapEntityToDto(Advertisement entity, Language language) {
        AdvertisementTranslate translation = (AdvertisementTranslate) translationService.getTranslate(
                entity.getTranslations().stream().map(LanguageHolder.class::cast).collect(Collectors.toSet()),
                language,
                optionsService.getDefaultSiteLanguage()
        );
        AdvertisementResponseDto dto = mapper.mapEntityToDto(entity);
        dto.setTitle(translation.getTitle());
        dto.setDescription(translation.getDescription());
        dto.setLangCode(language.getLangCode());
        dto.setCategory(categoryMapper.mapEntityToDto(entity.getCategory(), language));
        dto.setPayments(paymentMapper.mapEntityToDto(entity.getPayments(), language));
        dto.setDeliveries(deliveryMapper.mapEntityToDto(entity.getDeliveries(), language));
        dto.setAttributes(attributeMapper.mapEntityToDto(entity.getAttributes(), language));
        //TODO after complete breed mapper
        //dto.setBreed(breedMapper.toDto(entity.getBreed()));

        return dto;
    }

    public List<AdvertisementResponseDto> mapEntityToDto(List<Advertisement> advertisements,
                                                         Language language) {
        if (advertisements == null) {
            return Collections.emptyList();
        }
        return advertisements.stream()
                .map(p -> mapEntityToDto(p, language))
                .toList();
    }
}
