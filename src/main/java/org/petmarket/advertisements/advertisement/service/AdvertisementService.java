package org.petmarket.advertisements.advertisement.service;

import static org.petmarket.utils.MessageUtils.ADVERTISEMENT_NOT_FOUND;
import static org.petmarket.utils.MessageUtils.BREED_NOT_FOUND;
import static org.petmarket.utils.MessageUtils.CATEGORY_NOT_FOUND;
import static org.petmarket.utils.MessageUtils.CITY_NOT_FOUND;
import static org.petmarket.utils.MessageUtils.LANGUAGE_IS_PRESENT_IN_LIST;
import static org.petmarket.utils.MessageUtils.LANGUAGE_NOT_FOUND;
import static org.petmarket.utils.MessageUtils.NO_TRANSLATION;
import static org.petmarket.utils.MessageUtils.USER_NOT_FOUND;

import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.advertisements.advertisement.dto.AdvertisementRequestDto;
import org.petmarket.advertisements.advertisement.dto.AdvertisementResponseDto;
import org.petmarket.advertisements.advertisement.entity.Advertisement;
import org.petmarket.advertisements.advertisement.entity.AdvertisementStatus;
import org.petmarket.advertisements.advertisement.entity.AdvertisementTranslate;
import org.petmarket.advertisements.advertisement.mapper.AdvertisementMapper;
import org.petmarket.advertisements.advertisement.mapper.AdvertisementResponseTranslateMapper;
import org.petmarket.advertisements.advertisement.repository.AdvertisementRepository;
import org.petmarket.advertisements.attributes.entity.Attribute;
import org.petmarket.advertisements.attributes.repository.AttributeRepository;
import org.petmarket.advertisements.category.entity.AdvertisementCategory;
import org.petmarket.advertisements.category.repository.AdvertisementCategoryRepository;
import org.petmarket.breeds.entity.Breed;
import org.petmarket.breeds.repository.BreedRepository;
import org.petmarket.delivery.entity.Delivery;
import org.petmarket.delivery.repository.DeliveryRepository;
import org.petmarket.errorhandling.ItemNotFoundException;
import org.petmarket.language.entity.Language;
import org.petmarket.language.repository.LanguageRepository;
import org.petmarket.location.entity.City;
import org.petmarket.location.entity.Location;
import org.petmarket.location.repository.CityRepository;
import org.petmarket.options.service.OptionsService;
import org.petmarket.payment.entity.Payment;
import org.petmarket.payment.repository.PaymentRepository;
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

@Slf4j
@RequiredArgsConstructor
@Service
public class AdvertisementService {

    private final LanguageRepository languageRepository;
    private final AdvertisementRepository advertisementRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final AdvertisementCategoryRepository categoryRepository;
    private final AttributeRepository attributeRepository;
    private final PaymentRepository paymentRepository;
    private final DeliveryRepository deliveryRepository;
    private final CityRepository cityRepository;
    private final BreedRepository breedRepository;
    private final AdvertisementResponseTranslateMapper translateMapper;
    private final AdvertisementMapper advertisementMapper;
    private final ReviewMapper reviewMapper;
    private final OptionsService optionsService;
    private final TransliterateUtils transliterateUtils;

    @Transactional
    public AdvertisementResponseDto addAdvertisement(AdvertisementRequestDto request,
        BindingResult bindingResult, Authentication authentication) {
        ErrorUtils.checkItemNotCreatedException(bindingResult);

        Language defaultSiteLanguage = optionsService.getDefaultSiteLanguage();

        Advertisement advertisement = advertisementMapper.mapDtoRequestToEntity(request);
        advertisement.setAlias(
            transliterateUtils.getAlias(
                Advertisement.class.getSimpleName(),
                request.getTitle()
            )
        );
        advertisement.setAuthor(getUserByEmail(authentication.getName()));
        advertisement.setStatus(AdvertisementStatus.NO_ACTIVE);

        fillDateEnding(advertisement);
        fillCategory(advertisement, request);
        fillBreed(advertisement, request);
        fillLocation(advertisement, request);
        fillAttributes(advertisement, request);
        fillPayments(advertisement, request);
        fillDeliveries(advertisement, request);
        fillTranslation(advertisement, request, defaultSiteLanguage);

        advertisementRepository.save(advertisement);

        log.info("Advertisement created");
        return translateMapper.mapEntityToDto(advertisement, defaultSiteLanguage);
    }

    private void fillPayments(Advertisement advertisement, AdvertisementRequestDto request) {
        List<Payment> payments = paymentRepository.getPaymentsFromIds(request.getPaymentsIds());
        advertisement.setPayments(payments);
    }

    private void fillDeliveries(Advertisement advertisement, AdvertisementRequestDto request) {
        List<Delivery> deliveries = deliveryRepository.getDeliveriesFromIds(
            request.getDeliveriesIds());
        advertisement.setDeliveries(deliveries);
    }

    private void fillTranslation(Advertisement advertisement, AdvertisementRequestDto request,
        Language defaultSiteLanguage) {
        advertisement.setTranslations(new HashSet<>());
        AdvertisementTranslate translation = AdvertisementTranslate.builder()
            .id(null)
            .advertisement(advertisement)
            .title(request.getTitle())
            .description(request.getDescription())
            .language(defaultSiteLanguage)
            .build();
        addTranslation(advertisement, translation);
    }

    private void fillCategory(Advertisement advertisement, AdvertisementRequestDto request) {
        AdvertisementCategory category = categoryRepository.findById(request.getCategoryId())
            .orElseThrow(
                () -> {
                    throw new ItemNotFoundException(CATEGORY_NOT_FOUND);
                }
            );
        advertisement.setCategory(category);
    }

    private void fillBreed(Advertisement advertisement, AdvertisementRequestDto request) {
        Breed breed = null;
        if (request.getBreedId() != null) {
            breed = breedRepository.findById(request.getBreedId()).orElseThrow(
                () -> {
                    throw new ItemNotFoundException(BREED_NOT_FOUND);
                }
            );
        }
        advertisement.setBreed(breed);
    }

    private void fillLocation(Advertisement advertisement, AdvertisementRequestDto request) {
        City city = cityRepository.findById(request.getCityId()).orElseThrow(
            () -> {
                throw new ItemNotFoundException(CITY_NOT_FOUND);
            }
        );
        Location location = Location.builder()
            .city(city)
            .latitude(request.getLatitude())
            .longitude(request.getLongitude())
            .build();
        advertisement.setLocation(location);
    }

    private void fillAttributes(Advertisement advertisement, AdvertisementRequestDto request) {
        List<Attribute> attributes = attributeRepository.getAttributesFromIds(
            request.getAttributesIds());
        advertisement.setAttributes(attributes);
    }

    public AdvertisementResponseDto findById(Long id, String langCode) {
        Language language = getLanguage(langCode);
        return translateMapper.mapEntityToDto(getAdvertisement(id), language);
    }

    public Collection<AdvertisementReviewResponseDto> getReviewsByAdvertisementId(Long id) {
        getAdvertisement(id);
        return reviewMapper.mapEntityToAdvertisementDto(
            reviewRepository.findReviewByAdvertisementID(id));
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
            .findFirst().orElseThrow(() -> new TranslateException(NO_TRANSLATION));
    }

    private void addTranslation(Advertisement advertisement, AdvertisementTranslate translation) {
        if (checkLanguage(advertisement, translation.getLanguage())) {
            throw new TranslateException(LANGUAGE_IS_PRESENT_IN_LIST);
        }
        translation.setAdvertisement(advertisement);
        advertisement.getTranslations().add(translation);
    }

    private boolean checkLanguage(Advertisement advertisement, Language language) {
        return advertisement.getTranslations()
            .stream()
            .anyMatch(t -> t.getLanguage().equals(language));
    }

    private void fillDateEnding(Advertisement advertisement) {
        LocalDate currentDate = LocalDate.now();
        LocalDate dateEnding = currentDate.plusDays(10);
        advertisement.setEnding(dateEnding);
    }
}
