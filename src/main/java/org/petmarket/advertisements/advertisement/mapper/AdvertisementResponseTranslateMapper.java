package org.petmarket.advertisements.advertisement.mapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.advertisements.advertisement.dto.AdvertisementDetailsResponseDto;
import org.petmarket.advertisements.advertisement.dto.AdvertisementShortResponseDto;
import org.petmarket.advertisements.advertisement.entity.Advertisement;
import org.petmarket.advertisements.advertisement.entity.AdvertisementTranslate;
import org.petmarket.advertisements.attributes.entity.Attribute;
import org.petmarket.advertisements.attributes.mapper.AttributeTranslateMapper;
import org.petmarket.advertisements.attributes.service.AttributeService;
import org.petmarket.advertisements.category.mapper.AdvertisementCategoryResponseTranslateMapper;
import org.petmarket.advertisements.images.dto.AdvertisementImageResponseDto;
import org.petmarket.advertisements.images.entity.AdvertisementImageType;
import org.petmarket.breeds.mapper.BreedMapper;
import org.petmarket.delivery.mapper.DeliveryResponseTranslateMapper;
import org.petmarket.language.entity.Language;
import org.petmarket.options.service.OptionsService;
import org.petmarket.payment.mapper.PaymentResponseTranslateMapper;
import org.petmarket.translate.LanguageHolder;
import org.petmarket.translate.TranslationService;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class AdvertisementResponseTranslateMapper {

    private final AttributeService attributeService;
    private final OptionsService optionsService;
    private final TranslationService translationService;
    private final AdvertisementMapper mapper;
    private final AdvertisementCategoryResponseTranslateMapper categoryMapper;
    private final PaymentResponseTranslateMapper paymentMapper;
    private final DeliveryResponseTranslateMapper deliveryMapper;
    private final AttributeTranslateMapper attributeMapper;
    private final BreedMapper breedMapper;

    public AdvertisementDetailsResponseDto mapEntityToDto(Advertisement entity, Language language) {
        if (entity == null) {
            return null;
        }
        AdvertisementTranslate translation = (AdvertisementTranslate) translationService.getTranslate(
            entity.getTranslations().stream().map(LanguageHolder.class::cast)
                .collect(Collectors.toSet()),
            language,
            optionsService.getDefaultSiteLanguage()
        );
        AdvertisementDetailsResponseDto dto = mapper.mapEntityToDto(entity);
        dto.setTitle(translation.getTitle());
        dto.setAdditionalInformation(translation.getAdditionalInformation());
        dto.setDescription(translation.getDescription());
        dto.setLangCode(language.getLangCode());
        dto.setCategory(categoryMapper.mapEntityToDto(entity.getCategory(), language));
        dto.setPayments(paymentMapper.mapEntityToDto(entity.getPayments(), language));
        dto.setDeliveries(deliveryMapper.mapEntityToDto(entity.getDeliveries(), language));
        dto.setBreed(breedMapper.toDto(entity.getBreed(), language));

        List<Attribute> allAttributes = entity.getAttributes();
        List<Attribute> favoriteAttributes = attributeService.getFavoriteAttributes(allAttributes);

        List<Attribute> nonFavoriteAttributes = new ArrayList<>(allAttributes);
        nonFavoriteAttributes.removeAll(favoriteAttributes);

        dto.setFavoriteAttributes(attributeMapper.mapEntityToDto(favoriteAttributes, language));
        dto.setAttributes(attributeMapper.mapEntityToDto(nonFavoriteAttributes, language));

        splitImages(dto);
        return dto;
    }

    public void splitImages(AdvertisementDetailsResponseDto dto) {
        List<AdvertisementImageResponseDto> allImages = dto.getImages();
        List<AdvertisementImageResponseDto> images = allImages.stream()
            .filter(image -> AdvertisementImageType.ADVERTISEMENT_IMAGE.equals(image.getType()))
            .toList();
        List<AdvertisementImageResponseDto> documents = allImages.stream()
            .filter(image -> AdvertisementImageType.DOCUMENT.equals(image.getType()))
            .toList();
        List<AdvertisementImageResponseDto> vaccines = allImages.stream()
            .filter(image -> AdvertisementImageType.VACCINE.equals(image.getType()))
            .toList();

        dto.setImages(images);
        dto.setDocuments(documents);
        dto.setVaccines(vaccines);
    }

    public List<AdvertisementDetailsResponseDto> mapEntityToDto(List<Advertisement> advertisements,
        Language language) {
        if (advertisements == null) {
            return Collections.emptyList();
        }
        return advertisements.stream()
            .map(p -> mapEntityToDto(p, language))
            .toList();
    }

    public AdvertisementShortResponseDto mapEntityToShortDto(Advertisement entity,
        Language language) {
        if (entity == null) {
            return null;
        }
        AdvertisementTranslate translation = (AdvertisementTranslate) translationService.getTranslate(
            entity.getTranslations().stream().map(LanguageHolder.class::cast)
                .collect(Collectors.toSet()),
            language,
            optionsService.getDefaultSiteLanguage()
        );
        AdvertisementShortResponseDto dto = mapper.mapEntityToShortDto(entity);
        dto.setTitle(translation.getTitle());
        dto.setDescription(translation.getDescription());
        dto.setLangCode(language.getLangCode());

        List<Attribute> allAttributes = entity.getAttributes();
        List<Attribute> favoriteAttributes = attributeService.getFavoriteAttributes(allAttributes);

        List<Attribute> nonFavoriteAttributes = new ArrayList<>(allAttributes);
        nonFavoriteAttributes.removeAll(favoriteAttributes);

        dto.setFavoriteAttributes(attributeMapper.mapEntityToDto(favoriteAttributes, language));
        dto.setAttributes(attributeMapper.mapEntityToDto(nonFavoriteAttributes, language));

        dto.getImages()
            .removeIf(image -> !AdvertisementImageType.ADVERTISEMENT_IMAGE.equals(image.getType()));
        return dto;
    }

    public List<AdvertisementShortResponseDto> mapEntityToShortDto(
        List<Advertisement> advertisements,
        Language language) {
        if (advertisements == null) {
            return Collections.emptyList();
        }
        return advertisements.stream()
            .map(p -> mapEntityToShortDto(p, language))
            .toList();
    }
}
