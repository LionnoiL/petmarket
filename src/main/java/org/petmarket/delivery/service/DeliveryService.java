package org.petmarket.delivery.service;

import static org.petmarket.utils.MessageUtils.DELIVERY_NOT_FOUND;
import static org.petmarket.utils.MessageUtils.LANGUAGE_IS_PRESENT_IN_LIST;
import static org.petmarket.utils.MessageUtils.LANGUAGE_NOT_FOUND;
import static org.petmarket.utils.MessageUtils.NO_TRANSLATION;

import jakarta.transaction.Transactional;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
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

@Slf4j
@RequiredArgsConstructor
@Service
public class DeliveryService {

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

    @Transactional
    public DeliveryResponseDto addDelivery(DeliveryRequestDto request,
        BindingResult bindingResult) {
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

    @Transactional
    public DeliveryResponseDto updateDelivery(Long id, String langCode, DeliveryRequestDto request,
        BindingResult bindingResult) {
        ErrorUtils.checkItemNotUpdatedException(bindingResult);

        Delivery delivery = getDelivery(id);
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

    @Transactional
    public void deleteDelivery(Long id) {
        Delivery delivery = getDelivery(id);
        deliveryRepository.delete(delivery);
    }

    private Language getLanguage(String langCode) {
        return languageRepository.findByLangCodeAndEnableIsTrue(langCode).orElseThrow(() -> {
            throw new ItemNotFoundException(LANGUAGE_NOT_FOUND);
        });
    }

    private Delivery getEnableDelivery(Long id) {
        return deliveryRepository.findByIdAndEnableIsTrue(id).orElseThrow(() -> {
            throw new ItemNotFoundException(DELIVERY_NOT_FOUND);
        });
    }

    private Delivery getDelivery(Long id) {
        return deliveryRepository.findById(id).orElseThrow(() -> {
            throw new ItemNotFoundException(DELIVERY_NOT_FOUND);
        });
    }

    private DeliveryTranslate getTranslation(Delivery delivery, Language language) {
        return delivery.getTranslations().stream()
            .filter(t -> t.getLanguage().equals(language))
            .findFirst().orElseThrow(() -> new TranslateException(NO_TRANSLATION));
    }

    private void addTranslation(Delivery delivery, DeliveryTranslate translation) {
        if (checkLanguage(delivery, translation.getLanguage())) {
            throw new TranslateException(LANGUAGE_IS_PRESENT_IN_LIST);
        }
        translation.setDelivery(delivery);
        delivery.getTranslations().add(translation);
    }

    private boolean checkLanguage(Delivery delivery, Language language) {
        return delivery.getTranslations()
            .stream()
            .anyMatch(t -> t.getLanguage().equals(language));
    }
}
