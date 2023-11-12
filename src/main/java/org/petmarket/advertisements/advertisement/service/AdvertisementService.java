package org.petmarket.advertisements.advertisement.service;

import static org.petmarket.utils.MessageUtils.ADVERTISEMENT_NOT_FOUND;
import static org.petmarket.utils.MessageUtils.LANGUAGE_NOT_FOUND;
import static org.petmarket.utils.MessageUtils.USER_NOT_FOUND;

import java.util.HashSet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.advertisements.advertisement.dto.AdvertisementRequestDto;
import org.petmarket.advertisements.advertisement.dto.AdvertisementResponseDto;
import org.petmarket.advertisements.advertisement.entity.Advertisement;
import org.petmarket.advertisements.advertisement.entity.AdvertisementTranslate;
import org.petmarket.advertisements.advertisement.mapper.AdvertisementMapper;
import org.petmarket.advertisements.advertisement.mapper.AdvertisementResponseTranslateMapper;
import org.petmarket.advertisements.advertisement.repository.AdvertisementRepository;
import org.petmarket.delivery.entity.Delivery;
import org.petmarket.delivery.entity.DeliveryTranslate;
import org.petmarket.errorhandling.ItemNotFoundException;
import org.petmarket.language.entity.Language;
import org.petmarket.language.repository.LanguageRepository;
import org.petmarket.options.service.OptionsService;
import org.petmarket.review.dto.AdvertisementReviewRequestDto;
import org.petmarket.review.dto.AdvertisementReviewResponseDto;
import org.petmarket.review.entity.Review;
import org.petmarket.review.entity.ReviewType;
import org.petmarket.review.mapper.ReviewMapper;
import org.petmarket.review.repository.ReviewRepository;
import org.petmarket.translate.TranslateException;
import org.petmarket.users.entity.User;
import org.petmarket.users.repository.UserRepository;
import org.petmarket.utils.ErrorUtils;
import org.petmarket.utils.TransliterateUtils;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdvertisementService {

    private final LanguageRepository languageRepository;
    private final AdvertisementRepository advertisementRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final AdvertisementResponseTranslateMapper translateMapper;
    private final AdvertisementMapper advertisementMapper;
    private final ReviewMapper reviewMapper;

    private final OptionsService optionsService;
    private final TransliterateUtils transliterateUtils;

    public AdvertisementResponseDto addAdvertisement(AdvertisementRequestDto request,
        BindingResult bindingResult) {
        ErrorUtils.checkItemNotCreatedException(bindingResult);

        Language defaultSiteLanguage = optionsService.getDefaultSiteLanguage();

        Advertisement advertisement = advertisementMapper.mapDtoRequestToEntity(request);
        advertisement.setAlias(
            transliterateUtils.getAlias(
                Delivery.class.getSimpleName(),
                request.getTitle()));

        advertisement.setTranslations(new HashSet<>());
        AdvertisementTranslate translation = AdvertisementTranslate.builder()
            .id(null)
            .advertisement(advertisement)
            .title(request.getTitle())
            .description(request.getDescription())
            .language(defaultSiteLanguage)
            .build();
        addTranslation(advertisement, translation);

        advertisementRepository.save(advertisement);

        log.info("Advertisement created");
        return translateMapper.mapEntityToDto(advertisement, defaultSiteLanguage);
    }

    public AdvertisementResponseDto findById(Long id, String langCode) {
        Language language = getLanguage(langCode);
        return translateMapper.mapEntityToDto(getAdvertisement(id), language);
    }

    public Collection<AdvertisementReviewResponseDto> getReviewsByAdvertisementId(Long id) {
        getAdvertisement(id);
        return reviewMapper.mapEntityToAdvertisementDto(reviewRepository.findReviewByAdvertisementID(id));
    }

    public AdvertisementReviewResponseDto addReview(Long id, AdvertisementReviewRequestDto request,
                                                    BindingResult bindingResult, Authentication authentication) {
        ErrorUtils.checkItemNotCreatedException(bindingResult);

        User author = getUserByEmail(authentication.getName());
        Advertisement advertisement = getAdvertisement(id);

        Review review = Review.builder()
                .author(author)
                .type(ReviewType.USER_TO_ADVERTISEMENT)
                .value(request.getValue())
                .description(request.getDescription())
                .advertisement(advertisement)
                .build();
        reviewRepository.save(review);

        return reviewMapper.mapEntityToAdvertisementDto(review);
    }

    private Language getLanguage(String langCode) {
        return languageRepository.findByLangCodeAndEnableIsTrue(langCode)
                .orElseThrow(() -> {
                    throw new ItemNotFoundException(LANGUAGE_NOT_FOUND);
                });
    }

    private Advertisement getAdvertisement(Long id) {
        return advertisementRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException(ADVERTISEMENT_NOT_FOUND));
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ItemNotFoundException(USER_NOT_FOUND));
    }

    private AdvertisementTranslate getTranslation(Advertisement advertisement, Language language) {
        return advertisement.getTranslations().stream()
            .filter(t -> t.getLanguage().equals(language))
            .findFirst().orElseThrow(() -> new TranslateException("No translation"));
    }

    private void addTranslation(Advertisement advertisement, AdvertisementTranslate translation) {
        if (checkLanguage(advertisement, translation.getLanguage())) {
            throw new TranslateException("Language is present in list");
        }
        translation.setAdvertisement(advertisement);
        advertisement.getTranslations().add(translation);
    }

    private boolean checkLanguage(Advertisement advertisement, Language language) {
        return advertisement.getTranslations()
            .stream()
            .anyMatch(t -> t.getLanguage().equals(language));
    }
}
