package org.petmarket.advertisements.advertisement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.advertisements.advertisement.dto.AdvertisementDetailsResponseDto;
import org.petmarket.advertisements.advertisement.dto.AdvertisementRequestDto;
import org.petmarket.advertisements.advertisement.dto.AdvertisementShortResponseDto;
import org.petmarket.advertisements.advertisement.entity.Advertisement;
import org.petmarket.advertisements.advertisement.entity.AdvertisementStatus;
import org.petmarket.advertisements.advertisement.mapper.AdvertisementResponseTranslateMapper;
import org.petmarket.advertisements.advertisement.service.AdvertisementAccessCheckerService;
import org.petmarket.advertisements.advertisement.service.AdvertisementService;
import org.petmarket.advertisements.category.entity.AdvertisementCategory;
import org.petmarket.advertisements.category.service.AdvertisementCategoryService;
import org.petmarket.advertisements.images.dto.AdvertisementImageResponseDto;
import org.petmarket.advertisements.images.entity.AdvertisementImage;
import org.petmarket.advertisements.images.mapper.AdvertisementImageMapper;
import org.petmarket.advertisements.images.service.AdvertisementImageService;
import org.petmarket.language.entity.Language;
import org.petmarket.language.service.LanguageService;
import org.petmarket.options.service.OptionsService;
import org.petmarket.utils.annotations.parametrs.ParameterId;
import org.petmarket.utils.annotations.parametrs.ParameterLanguage;
import org.petmarket.utils.annotations.parametrs.ParameterPageNumber;
import org.petmarket.utils.annotations.parametrs.ParameterPageSize;
import org.petmarket.utils.annotations.responses.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.petmarket.utils.MessageUtils.REQUEST_BODY_IS_MANDATORY;
import static org.petmarket.utils.MessageUtils.SUCCESSFULLY_OPERATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Advertisement")
@Slf4j
@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping(value = "/v1/advertisements")
public class AdvertisementController {

    private final AdvertisementService advertisementService;
    private final AdvertisementAccessCheckerService accessCheckerService;
    private final AdvertisementCategoryService categoryService;
    private final LanguageService languageService;
    private final OptionsService optionsService;
    private final AdvertisementImageService advertisementImageService;
    private final AdvertisementResponseTranslateMapper advertisementMapper;
    private final AdvertisementImageMapper imageMapper;

    @Operation(summary = "Get Advertisement by ID",
            description = """
                        Advertisements with Active status can be seen by all users.
                        Ads with a different status can only be accessed by administrators or ad authors.
                    """)
    @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION, content = {
            @Content(mediaType = APPLICATION_JSON_VALUE, schema =
            @Schema(implementation = AdvertisementDetailsResponseDto.class))
    })
    @ApiResponseNotFound
    @GetMapping("/{id}/{langCode}")
    public AdvertisementDetailsResponseDto getAdvertisementById(
            @ParameterId @PathVariable Long id,
            @ParameterLanguage @PathVariable String langCode) {
        log.info("Received request to get Advertisement Advertisement with id - {}.", id);
        Language language = languageService.getByLangCode(langCode);
        Advertisement advertisement = advertisementService.getAdvertisement(id);
        accessCheckerService.checkViewAccess(advertisement);
        return advertisementMapper.mapEntityToDto(advertisement, language);
    }

    @Operation(summary = "Create a new Advertisement",
            description = """
                        Only authorized users have access to create ads.
                    """)
    @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION, content = {
            @Content(mediaType = APPLICATION_JSON_VALUE, schema =
            @Schema(implementation = AdvertisementDetailsResponseDto.class))
    })
    @ApiResponseBadRequest
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public AdvertisementDetailsResponseDto addAdvertisement(
            @RequestBody @Valid @NotNull(message = REQUEST_BODY_IS_MANDATORY) final AdvertisementRequestDto request,
            BindingResult bindingResult, Authentication authentication) {
        log.info("Received request to create Delivery - {}.", request);
        return advertisementService.addAdvertisement(request, bindingResult, authentication);
    }

    @Operation(summary = "Get Favorite Advertisements")
    @ApiResponseSuccessful
    @ApiResponseLanguageNotFound
    @GetMapping("/favorite/{langCode}")
    public Page<AdvertisementShortResponseDto> getFavoriteAds(
            @ParameterLanguage @PathVariable String langCode,
            @ParameterPageNumber @RequestParam(defaultValue = "1") @Min(1) int page,
            @ParameterPageSize @RequestParam(defaultValue = "30") @Min(1) int size,
            @Parameter(description = "List of categories identifiers",
                    schema = @Schema(type = "array[integer]")
            ) @RequestParam(required = false) List<Long> categoriesIds
    ) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Language language = languageService.getByLangCode(langCode);
        List<AdvertisementCategory> categories = categoryService.getByIds(categoriesIds);
        Page<Advertisement> advertisements = advertisementService.getFavoriteAds(categories,
                pageable);
        return advertisements.map(adv -> advertisementMapper.mapEntityToShortDto(adv, language));
    }

    @Operation(summary = "Set Advertisement status to Draft",
            description = """
                    Changes the status of the list of ads to Draft.
                    Only authorized users have access.
                    Users without administrator rights can only access their ads.
                    """)
    @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION, content = {
            @Content(mediaType = APPLICATION_JSON_VALUE, schema =
            @Schema(implementation = AdvertisementDetailsResponseDto.class))
    })
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/status-draft")
    public List<AdvertisementDetailsResponseDto> setStatusDraft(
            @Parameter(description = "List of advertisements identifiers",
                    schema = @Schema(type = "array[integer]")
            ) @RequestParam(required = false) List<Long> ids
    ) {
        List<Advertisement> advertisements = advertisementService.getAdvertisements(ids);
        accessCheckerService.checkUpdateAccess(advertisements);
        advertisementService.setStatus(advertisements, AdvertisementStatus.DRAFT);
        Language defaultSiteLanguage = optionsService.getDefaultSiteLanguage();
        return advertisements.stream()
                .map(adv -> advertisementMapper.mapEntityToDto(adv, defaultSiteLanguage))
                .toList();
    }

    @Operation(summary = "Set Advertisement status to Pending",
            description = """
                    Changes the status of the list of ads to Pending.
                    Only authorized users have access.
                    Users without administrator rights can only access their ads.
                    """)
    @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION, content = {
            @Content(mediaType = APPLICATION_JSON_VALUE, schema =
            @Schema(implementation = AdvertisementDetailsResponseDto.class))
    })
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/status-pending")
    public List<AdvertisementDetailsResponseDto> setStatusPublic(
            @Parameter(description = "List of advertisements identifiers",
                    schema = @Schema(type = "array[integer]")
            ) @RequestParam(required = false) List<Long> ids
    ) {
        List<Advertisement> advertisements = advertisementService.getAdvertisements(ids);
        accessCheckerService.checkUpdateAccess(advertisements);
        advertisementService.setStatus(advertisements, AdvertisementStatus.PENDING);
        Language defaultSiteLanguage = optionsService.getDefaultSiteLanguage();
        return advertisements.stream()
                .map(adv -> advertisementMapper.mapEntityToDto(adv, defaultSiteLanguage))
                .toList();
    }

    @Operation(summary = "Adding images to advertisement",
            description = """
                        Only authorized users have access.
                        Users without administrator rights can only access their ads.
                        One ad can have no more than 10 images
                    """)
    @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION, content = {
            @Content(mediaType = APPLICATION_JSON_VALUE, schema =
            @Schema(implementation = AdvertisementDetailsResponseDto.class))
    })
    @ApiResponseBadRequest
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @ApiResponseNotFound
    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/{id}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Set<AdvertisementImageResponseDto> uploadImages(@PathVariable Long id,
                                                           @RequestParam("images") List<MultipartFile> images) {
        Advertisement advertisement = advertisementService.getAdvertisement(id);
        accessCheckerService.checkUpdateAccess(List.of(advertisement));
        Set<AdvertisementImage> advertisementImages = advertisementImageService.uploadImages(advertisement, images);
        return advertisementImages.stream().map(imageMapper::toDto).collect(Collectors.toSet());
    }
}
