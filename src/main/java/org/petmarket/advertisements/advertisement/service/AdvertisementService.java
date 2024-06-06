package org.petmarket.advertisements.advertisement.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.search.engine.search.predicate.dsl.BooleanPredicateClausesStep;
import org.hibernate.search.engine.search.predicate.dsl.SearchPredicateFactory;
import org.hibernate.search.engine.search.query.SearchQuery;
import org.hibernate.search.engine.search.sort.SearchSort;
import org.hibernate.search.engine.search.sort.dsl.FieldSortOptionsStep;
import org.hibernate.search.engine.search.sort.dsl.SearchSortFactory;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.scope.SearchScope;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.petmarket.advertisements.advertisement.dto.AdvertisementDetailsResponseDto;
import org.petmarket.advertisements.advertisement.dto.AdvertisementPriceRangeDto;
import org.petmarket.advertisements.advertisement.dto.AdvertisementRequestDto;
import org.petmarket.advertisements.advertisement.entity.*;
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
import org.petmarket.errorhandling.BadRequestException;
import org.petmarket.errorhandling.ItemNotCreatedException;
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
import org.petmarket.users.entity.UserStatus;
import org.petmarket.users.repository.UserRepository;
import org.petmarket.utils.ErrorUtils;
import org.petmarket.utils.TransliterateUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.petmarket.utils.MessageUtils.*;

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
    private final EntityManager entityManager;

    public Page<Advertisement> getByCategoryTypeCitiesAttributes(
            AdvertisementCategory category, List<Attribute> attributes, List<City> cities,
            AdvertisementType type,
            AdvertisementStatus status, Pageable pageable
    ) {

        Specification<Object> where = Specification.where((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            root.fetch("author");

            if (category != null) {
                predicates.add(criteriaBuilder.equal(root.get("category"), category));
            }
            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }
            if (type != null) {
                predicates.add(criteriaBuilder.equal(root.get("type"), type));
            }
            if (!cities.isEmpty()) {
                predicates.add(root.join("location").get("city").in(cities));
            }
            if (!attributes.isEmpty()) {
                List<Predicate> orPredicates = new ArrayList<>();
                for (Attribute attribute : attributes) {
                    orPredicates.add(criteriaBuilder.isMember(attribute, root.get("attributes")));
                }
                predicates.add(criteriaBuilder.or(orPredicates.toArray(new Predicate[0])));
            }

            predicates.add(criteriaBuilder.notEqual(root.get("author").get("status"), UserStatus.DELETED));

            return criteriaBuilder.and(predicates.toArray(new Predicate[]{}));
        });

        return advertisementRepository.findAll(where, pageable);
    }

    public Page<Advertisement> getAdvertisements(List<AdvertisementCategory> categories,
                                                 List<City> cities,
                                                 AdvertisementStatus status,
                                                 AdvertisementType type, Pageable pageable) {
        Specification<Object> where = Specification.where((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!categories.isEmpty()) {
                predicates.add(root.join("category").in(categories));
            }
            if (!cities.isEmpty()) {
                predicates.add(root.join("location").get("city").in(cities));
            }
            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }
            if (type != null) {
                predicates.add(criteriaBuilder.equal(root.get("type"), type));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[]{}));
        });

        return advertisementRepository.findAll(where, pageable);
    }

    public Page<Advertisement> getFavoriteAds(List<AdvertisementCategory> categories,
                                              Pageable pageable) {
        if (categories.isEmpty()) {
            return advertisementRepository.findAllByStatusOrderByCreatedDesc(
                    AdvertisementStatus.ACTIVE, pageable);
        } else {
            return advertisementRepository.findAllByCategoryInAndStatusOrderByCreatedDesc(
                    categories,
                    AdvertisementStatus.ACTIVE,
                    pageable
            );
        }
    }

    @Transactional
    public AdvertisementDetailsResponseDto addAdvertisement(AdvertisementRequestDto request,
                                                            BindingResult bindingResult,
                                                            String email) {
        ErrorUtils.checkItemNotCreatedException(bindingResult);

        Advertisement advertisement = advertisementMapper.mapDtoRequestToEntity(request);
        advertisement.setAlias(
                transliterateUtils.getAlias(
                        Advertisement.class.getSimpleName(),
                        request.getTitle()
                )
        );

        advertisement.setAuthor(getUserByEmail(email));
        advertisement.setStatus(AdvertisementStatus.DRAFT);
        //setter
        fillDateEnding(advertisement);
        fillCategory(advertisement, request);
        fillBreed(advertisement, request);
        fillLocation(advertisement, request);
        fillAttributes(advertisement, request);
        fillPayments(advertisement, request);
        fillDeliveries(advertisement, request);

        Language defaultSiteLanguage = optionsService.getDefaultSiteLanguage();
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
                .additionalInformation(request.getAdditionalInformation())
                .language(defaultSiteLanguage)
                .build();
        addTranslation(advertisement, translation);
    }

    private void fillCategory(Advertisement advertisement, AdvertisementRequestDto request) {
        AdvertisementCategory category = null;
        if (request.getCategoryId() != null) {
            category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(
                            () -> {
                                throw new ItemNotFoundException(CATEGORY_NOT_FOUND);
                            }
                    );
        }
        advertisement.setCategory(category);
    }

    private void fillBreed(Advertisement advertisement, AdvertisementRequestDto request) {
        Breed breed = null;
        if (request.getBreedId() != null) {
            breed = breedRepository.findById(request.getBreedId()).orElseThrow(
                    () -> new ItemNotFoundException(BREED_NOT_FOUND)
            );
            if (advertisement.getCategory() != null && !advertisement.getCategory().equals(breed.getCategory())) {
                throw new ItemNotCreatedException("The breed category does not match the current ad category");
            }
        }
        advertisement.setBreed(breed);
    }

    private void fillLocation(Advertisement advertisement, AdvertisementRequestDto request) {
        Location location = null;
        if (request.getCityId() != null) {
            City city = cityRepository.findById(request.getCityId()).orElseThrow(
                    () -> new ItemNotFoundException(CITY_NOT_FOUND)
            );
            location = Location.builder()
                    .city(city)
                    .latitude(request.getLatitude())
                    .longitude(request.getLongitude())
                    .build();
        }
        advertisement.setLocation(location);
    }

    private void fillAttributes(Advertisement advertisement, AdvertisementRequestDto request) {
        List<Attribute> attributes = attributeRepository.getAttributesFromIds(
                request.getAttributesIds());
        advertisement.setAttributes(attributes);
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
                .user(advertisement.getAuthor())
                .type(ReviewType.BUYER_TO_SELLER)
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

    public Advertisement getAdvertisement(Long id) {
        Advertisement advertisement = advertisementRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException(ADVERTISEMENT_NOT_FOUND));

        if (advertisementRepository.getAdvertisementAuthorStatus(id) == UserStatus.DELETED) {
            throw new ItemNotFoundException(ADVERTISEMENT_NOT_FOUND);
        }

        return advertisement;
    }

    public List<Advertisement> getAdvertisements(List<Long> ids) {
        return advertisementRepository.findAllById(ids);
    }

    public void setStatus(List<Advertisement> advertisements, AdvertisementStatus status) {
        if (advertisements == null) {
            throw new BadRequestException("advertisements is null");
        }
        for (Advertisement advertisement : advertisements) {
            if (advertisement.getCategory() == null || advertisement.getLocation() == null) {
                status = AdvertisementStatus.DRAFT;
            }
            advertisement.setStatus(status);
            advertisementRepository.save(advertisement);
        }
    }

    public Page<Advertisement> getAuthorsAdvertisements(Long authorId, Long excludedAdvertisementId,
                                                        Pageable pageable) {
        return advertisementRepository
                .findAllByAuthorIdAndStatusAndIdNotOrderByCreatedDesc(
                        authorId, AdvertisementStatus.ACTIVE, excludedAdvertisementId, pageable);
    }

    public Page<Advertisement> search(String searchTerm, int page, int size, List<Long> breedsIds,
                                      List<Long> attributeIds, List<Long> statesIds, List<Long> cityIds,
                                      BigDecimal minPrice, BigDecimal maxPrice,
                                      AdvertisementSortOption sortOption, Long categoryId) {
        SearchSession searchSession = Search.session(entityManager);
        SearchScope<Advertisement> scope = searchSession.scope(Advertisement.class);
        Pageable pageable = PageRequest.of(page - 1, size);

        if (categoryId == null) {
            categoryId = getCategoryIdFromSearch(searchTerm);
        }

        Long finalCategoryId = categoryId;
        SearchQuery<Advertisement> searchQuery = searchSession.search(Advertisement.class)
                .where(f -> buildSearchQueryWithFilters(
                        f, searchTerm, finalCategoryId, breedsIds, attributeIds, statesIds, cityIds,
                        minPrice, maxPrice
                ))
                .sort(buildAdvertisementSort(scope, sortOption))
                .toQuery();

        List<Advertisement> hits = searchQuery
                .fetchHits((page - 1) * size, size);

        return new PageImpl<>(hits, pageable, searchQuery.fetchTotalHitCount());
    }

    public AdvertisementPriceRangeDto getAdvertisementPriceRangeByCategory(Long categoryId) {
        AdvertisementPriceRangeDto priceRange = advertisementRepository
                .getAdvertisementPriceRangeByCategory(categoryId);

        if (priceRange.getMaxPrice() == null) {
            priceRange.setMaxPrice(BigDecimal.ZERO);
        }
        if (priceRange.getMinPrice() == null) {
            priceRange.setMinPrice(BigDecimal.ZERO);
        }

        return priceRange;
    }

    public Page<Advertisement> getSimilarAdvertisements(Long currentAdvertisementId, int size, int page) {
        SearchSession searchSession = Search.session(entityManager);
        Advertisement currentAdvertisement = getAdvertisement(currentAdvertisementId);
        Pageable pageable = PageRequest.of(page - 1, size);

        SearchQuery<Advertisement> searchQuery = searchSession.search(Advertisement.class)
                .where(f -> buildSimilarQuery(f, currentAdvertisement))
                .sort(f -> f.field("updated").desc())
                .toQuery();
        List<Advertisement> similarAdvertisements = searchQuery
                .fetchHits((page - 1) * size, size);

        return new PageImpl<>(similarAdvertisements, pageable, searchQuery.fetchTotalHitCount());
    }

    public Advertisement getAdvertisementByImageId(Long imageId) {
        return advertisementRepository.findAdvertisementByImageId(imageId)
                .orElseThrow(() -> new ItemNotFoundException(ADVERTISEMENT_NOT_FOUND));
    }

    public List<Advertisement> getAdvertisementsByImageIds(List<Long> imageIds) {
        return advertisementRepository.findAdvertisementsByImageIds(imageIds);
    }

    private Long getCategoryIdFromSearch(String search) {
        if (search == null || search.isBlank()) {
            return null;
        }

        SearchSession searchSession = Search.session(entityManager);
        List<Advertisement> hits = searchSession.search(Advertisement.class)
                .where(f -> buildSearchQuery(f, search))
                .fetchHits(20);
        HashMap<Long, Long> categoriesIdsCount = new HashMap<>();

        for (Advertisement adv : hits) {
            categoriesIdsCount.put(adv.getCategory().getId(),
                    categoriesIdsCount.getOrDefault(adv.getCategory().getId(), 0L) + 1);
        }
        if (hits.isEmpty()) {
            return null;
        }

        return categoriesIdsCount.entrySet().stream()
                .max(Map.Entry.comparingByValue()).orElseThrow().getKey();
    }

    private SearchSort buildAdvertisementSort(SearchScope<Advertisement> scope, AdvertisementSortOption sortOption) {
        SearchSortFactory f = scope.sort();
        FieldSortOptionsStep<?, ?> s;
        switch (sortOption) {
            case RATING_LOWEST -> s = f.field("rating").asc();
            case PRICE_LOWEST -> s = f.field("price").asc();
            case PRICE_HIGHEST -> s = f.field("price").desc();
            case NEWEST -> s = f.field("updated").desc();
            case OLDEST -> s = f.field("updated").asc();
            default -> s = f.field("rating").desc();
        }
        return s.toSort();
    }

    private BooleanPredicateClausesStep<?> buildSearchQueryWithFilters(
            SearchPredicateFactory f, String searchTerm, Long categoryId, List<Long> breedsIds, List<Long> attributeIds,
            List<Long> statesIds, List<Long> cityIds, BigDecimal minPrice, BigDecimal maxPrice) {
        BooleanPredicateClausesStep<?> queryStep = f.bool();
        queryStep.mustNot(f.terms().field("author.status").matchingAny(UserStatus.DELETED));
        queryStep.must(f.terms().field("status").matchingAny(AdvertisementStatus.ACTIVE));

        if (categoryId != null) {
            queryStep.must(f.terms().field("category.id").matchingAny(categoryId));
        }
        if (searchTerm != null && !searchTerm.isBlank()) {
            queryStep.must(buildSearchQuery(f, searchTerm));
        }
        if (searchTerm == null || searchTerm.isBlank()) {
            queryStep.must(f.matchAll());
        }
        if (breedsIds != null && !breedsIds.isEmpty()) {
            queryStep.must(f.terms().field("breed.id").matchingAny(breedsIds));
        }
        if (attributeIds != null && !attributeIds.isEmpty()) {
            queryStep.must(f.terms().field("attributes.id").matchingAny(attributeIds));
        }
        if (statesIds != null && !statesIds.isEmpty()) {
            queryStep.must(f.terms().field("location.city.state.id").matchingAny(statesIds));
        }
        if (cityIds != null && !cityIds.isEmpty()) {
            queryStep.must(f.terms().field("location.city.id").matchingAny(cityIds));
        }
        if (minPrice != null) {
            queryStep.must(f.range().field("price").atLeast(minPrice));
        }
        if (maxPrice != null) {
            queryStep.must(f.range().field("price").atMost(maxPrice));
        }

        return queryStep;
    }

    private BooleanPredicateClausesStep<?> buildSearchQuery(SearchPredicateFactory f, String search) {
        return f.bool()
                .should(f.match().field("translations.title").boost(2.0F).matching(search).fuzzy(1))
                .should(f.match().field("translations.description").boost(1.5F).matching(search).fuzzy(1))
                .should(f.match().field("category.translations.title").boost(1.5F).matching(search).fuzzy(1))
                .should(f.match().field("attributes.translations.title").boost(1.5F).matching(search).fuzzy(1))
                .should(f.match().field("breed.translations.title").boost(1.0F).matching(search).fuzzy(1))
                .should(f.match().field("location.city.name").boost(1.0F).matching(search).fuzzy(1));
    }

    private BooleanPredicateClausesStep<?> buildSimilarQuery(SearchPredicateFactory f, Advertisement advertisement) {
        List<Long> attributesIds = new ArrayList<>();

        for (Attribute attribute : advertisement.getAttributes()) {
            attributesIds.add(attribute.getId());
        }

        BooleanPredicateClausesStep<?> queryStep = f.bool()
                .mustNot(f.terms().field("id").matchingAny(advertisement.getId()))
                .mustNot(f.terms().field("author.status").matchingAny(UserStatus.DELETED))
                .must(f.terms().field("category.id").matchingAny(advertisement.getCategory().getId()))
                .must(f.terms().field("status").matchingAny(AdvertisementStatus.ACTIVE));
        if (advertisement.getBreed() != null) {
            queryStep.should(f.match().field("breed.id").matching(advertisement.getBreed().getId()));
        }
        if (advertisement.getLocation() != null && advertisement.getLocation().getCity() != null &&
                advertisement.getLocation().getCity().getId() != null) {
            queryStep.should(f.match().field("location.city.id")
                    .matching(advertisement.getLocation().getCity().getId()));
        }
        if (!attributesIds.isEmpty()) {
            queryStep.should(f.terms().field("attributes.id").matchingAny(attributesIds));
        }

        return queryStep;
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
        LocalDate dateEnding = currentDate.plusDays(optionsService.getNumberOfDaysAdvIsActive());
        advertisement.setEnding(dateEnding);
    }
}
