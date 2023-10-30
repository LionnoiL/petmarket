package org.petmarket.delivery.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.delivery.dto.DeliveryRequestDto;
import org.petmarket.delivery.dto.DeliveryResponseDto;
import org.petmarket.delivery.entity.Delivery;
import org.petmarket.delivery.entity.DeliveryTranslate;
import org.petmarket.delivery.mapper.DeliveryMapper;
import org.petmarket.delivery.mapper.DeliveryResponseTranslateMapper;
import org.petmarket.delivery.repository.DeliveryRepository;
import org.petmarket.errorhandling.ItemNotFoundException;
import org.petmarket.language.entity.Language;
import org.petmarket.language.repository.LanguageRepository;
import org.petmarket.options.service.OptionsService;
import org.petmarket.translate.TranslateException;
import org.petmarket.utils.ErrorUtils;
import org.petmarket.utils.TransliterateUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class DeliveryService {

    public static final String LANGUAGE_NOT_FOUND_MESSAGE = "Language not found";
    private static final String DELIVERY_NOT_FOUND_MESSAGE = "Delivery not found";
    private final DeliveryRepository deliveryRepository;
    private final LanguageRepository languageRepository;
    private final DeliveryResponseTranslateMapper deliveryResponseTranslateMapper;
    private final DeliveryMapper deliveryMapper;
    private final OptionsService optionsService;
    private final TransliterateUtils transliterateUtils;

    public DeliveryResponseDto findEnabledById(Long id, String langCode) {
        Language language = getLanguage(langCode);
        return deliveryResponseTranslateMapper.mapEntityToDto(getEnableDelivery(id), language);
    }

    public DeliveryResponseDto findById(Long id, String langCode) {
        Language language = getLanguage(langCode);
        return deliveryResponseTranslateMapper.mapEntityToDto(getDelivery(id), language);
    }

    public Collection<DeliveryResponseDto> getEnabled(String langCode) {
        Language language = getLanguage(langCode);
        List<Delivery> deliveries = deliveryRepository.findAllByEnableIsTrue();
        return deliveryResponseTranslateMapper.mapEntityToDto(deliveries, language);
    }

    public Collection<DeliveryResponseDto> getAll(String langCode) {
        Language language = getLanguage(langCode);
        List<Delivery> deliveries = deliveryRepository.findAll();
        return deliveryResponseTranslateMapper.mapEntityToDto(deliveries, language);
    }

    public DeliveryResponseDto addDelivery(DeliveryRequestDto request, BindingResult bindingResult) {
        ErrorUtils.checkItemNotCreatedException(bindingResult);

        Language defaultSiteLanguage = optionsService.getDefaultSiteLanguage();

        Delivery delivery = deliveryMapper.mapDtoRequestToEntity(request);
        delivery.setAlias(
                transliterateUtils.getAlias(
                        Delivery.class.getSimpleName(),
                        request.getName()));

        delivery.setTranslations(new HashSet<>());
        DeliveryTranslate translation = DeliveryTranslate.builder()
                .id(null)
                .delivery(delivery)
                .name(request.getName())
                .description(request.getDescription())
                .language(defaultSiteLanguage)
                .build();
        addTranslation(delivery, translation);

        deliveryRepository.save(delivery);

        log.info("Delivery created");
        return deliveryResponseTranslateMapper.mapEntityToDto(delivery, defaultSiteLanguage);
    }

    public DeliveryResponseDto updateDelivery(Long id, String langCode, DeliveryRequestDto request,
                                              BindingResult bindingResult) {
        ErrorUtils.checkItemNotUpdatedException(bindingResult);

        Delivery delivery = getEnableDelivery(id);
        delivery.setEnable(request.getEnable());

        Language language = getLanguage(langCode);
        DeliveryTranslate translation;
        if (checkLanguage(delivery, language)) {
            translation = getTranslation(delivery, language);
        } else {
            translation = new DeliveryTranslate();
            addTranslation(delivery, translation);
            translation.setDelivery(delivery);
            translation.setLanguage(language);
        }
        translation.setName(request.getName());
        translation.setDescription(request.getDescription());

        deliveryRepository.save(delivery);

        log.info("The Delivery was updated");
        return deliveryResponseTranslateMapper.mapEntityToDto(delivery, language);
    }

    public void deleteDelivery(Long id) {
        Delivery attribute = getEnableDelivery(id);
        deliveryRepository.delete(attribute);
    }

    private Language getLanguage(String langCode) {
        return languageRepository.findByLangCodeAndEnableIsTrue(langCode).orElseThrow(() -> {
            throw new ItemNotFoundException(LANGUAGE_NOT_FOUND_MESSAGE);
        });
    }

    private Delivery getEnableDelivery(Long id) {
        return deliveryRepository.findByIdAndEnableIsTrue(id).orElseThrow(() -> {
            throw new ItemNotFoundException(DELIVERY_NOT_FOUND_MESSAGE);
        });
    }

    private Delivery getDelivery(Long id) {
        return deliveryRepository.findById(id).orElseThrow(() -> {
            throw new ItemNotFoundException(DELIVERY_NOT_FOUND_MESSAGE);
        });
    }

    private DeliveryTranslate getTranslation(Delivery delivery, Language language) {
        return delivery.getTranslations().stream()
                .filter(t -> t.getLanguage().equals(language))
                .findFirst().orElseThrow(() -> new TranslateException("No translation"));
    }

    private void addTranslation(Delivery delivery, DeliveryTranslate translation) {
        if (checkLanguage(delivery, translation.getLanguage())) {
            throw new TranslateException("Language is present in list");
        }
        translation.setDelivery(delivery);
        delivery.getTranslations().add(translation);
    }

    private boolean checkLanguage(Delivery group, Language language) {
        return group.getTranslations()
                .stream()
                .anyMatch(t -> t.getLanguage().equals(language));
    }
}
