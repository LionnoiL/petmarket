package org.petmarket.advertisements.advertisement.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.advertisements.advertisement.dto.AdvertisementResponseDto;
import org.petmarket.advertisements.advertisement.entity.Advertisement;
import org.petmarket.advertisements.advertisement.mapper.AdvertisementResponseTranslateMapper;
import org.petmarket.advertisements.advertisement.repository.AdvertisementRepository;
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

    public static final String LANGUAGE_NOT_FOUND_MESSAGE = "Language not found";
    public static final String ADVERTISEMENT_NOT_FOUND_MESSAGE = "Advertisement not found";

    private final LanguageRepository languageRepository;
    private final AdvertisementRepository advertisementRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final AdvertisementResponseTranslateMapper translateMapper;
    private final ReviewMapper reviewMapper;

    private final OptionsService optionsService;
    private final TransliterateUtils transliterateUtils;

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
                    throw new ItemNotFoundException(LANGUAGE_NOT_FOUND_MESSAGE);
                });
    }

    private Advertisement getAdvertisement(Long id) {
        return advertisementRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException(ADVERTISEMENT_NOT_FOUND_MESSAGE));
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ItemNotFoundException("User not found"));
    }
}
