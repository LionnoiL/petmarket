package org.petmarket.advertisements.advertisement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.advertisements.advertisement.dto.AdvertisementDetailsResponseDto;
import org.petmarket.advertisements.advertisement.dto.AdvertisementShortResponseDto;
import org.petmarket.advertisements.advertisement.entity.Advertisement;
import org.petmarket.advertisements.advertisement.entity.AdvertisementStatus;
import org.petmarket.advertisements.advertisement.entity.AdvertisementType;
import org.petmarket.advertisements.advertisement.mapper.AdvertisementResponseTranslateMapper;
import org.petmarket.advertisements.advertisement.service.AdvertisementService;
import org.petmarket.advertisements.category.entity.AdvertisementCategory;
import org.petmarket.advertisements.category.service.AdvertisementCategoryService;
import org.petmarket.language.entity.Language;
import org.petmarket.language.service.LanguageService;
import org.petmarket.location.entity.City;
import org.petmarket.location.service.CityService;
import org.petmarket.options.service.OptionsService;
import org.petmarket.utils.annotations.parametrs.*;
import org.petmarket.utils.annotations.responses.ApiResponseForbidden;
import org.petmarket.utils.annotations.responses.ApiResponseLanguageNotFound;
import org.petmarket.utils.annotations.responses.ApiResponseSuccessful;
import org.petmarket.utils.annotations.responses.ApiResponseUnauthorized;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Advertisement")
@Slf4j
@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping(value = "/v1/admin/advertisements")
public class AdvertisementAdminController {

    private final AdvertisementService advertisementService;
    private final AdvertisementCategoryService categoryService;
    private final CityService cityService;
    private final LanguageService languageService;
    private final OptionsService optionsService;
    private final AdvertisementResponseTranslateMapper advertisementMapper;

    @Operation(summary = "Get Advertisements")
    @ApiResponseSuccessful
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @ApiResponseLanguageNotFound
    @GetMapping(path = "/{langCode}")
    public Page<AdvertisementShortResponseDto> getAds(
            @ParameterLanguage @PathVariable String langCode,
            @ParameterPageNumber @RequestParam(defaultValue = "1") @Positive int page,
            @ParameterPageSize @RequestParam(defaultValue = "30") @Positive int size,
            @ParameterPageSort @RequestParam(required = false, defaultValue = "ASC") String sortDirection,
            @ParameterPageSortField @RequestParam(required = false, defaultValue = "created") String sortField,
            @RequestParam(required = false) AdvertisementStatus status,
            @RequestParam(required = false) AdvertisementType type,
            @Parameter(description = "List of categories identifiers", schema = @Schema(type = "array[integer]")
            ) @RequestParam(required = false) List<Long> categoriesIds,
            @Parameter(description = "List of cities identifiers",
                    schema = @Schema(type = "array[integer]")
            ) @RequestParam(required = false) List<Long> citiesIds
    ) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.Direction.valueOf(sortDirection), sortField);
        Language language = languageService.getByLangCode(langCode);
        List<AdvertisementCategory> categories = categoryService.getByIds(categoriesIds);
        List<City> cities = cityService.getByIds(citiesIds);
        Page<Advertisement> advertisements = advertisementService.getAdvertisements(categories, cities, status,
                type, pageable);
        return advertisements.map(adv -> advertisementMapper.mapEntityToShortDto(adv, language));
    }

    @Operation(summary = "Set Advertisement status")
    @ApiResponseSuccessful
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @PutMapping("/status")
    public List<AdvertisementDetailsResponseDto> setStatusDraft(
            @Parameter(description = "List of advertisements identifiers",
                    schema = @Schema(type = "array[integer]")
            ) @RequestParam(required = false) List<Long> ids,
            @Parameter(description = "New status") @RequestParam AdvertisementStatus status
    ) {
        List<Advertisement> advertisements = advertisementService.getAdvertisements(ids);
        advertisementService.setStatus(advertisements, status);
        Language defaultSiteLanguage = optionsService.getDefaultSiteLanguage();
        return advertisements.stream()
                .map(adv -> advertisementMapper.mapEntityToDto(adv, defaultSiteLanguage))
                .toList();
    }

    @Operation(summary = "Update Advertisements top rating")
    @ApiResponseSuccessful
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @PutMapping("/top-rating")
    public void updateAllTopRatings() {
        advertisementService.updateAllTopRatings();
    }

    @Operation(summary = "Update Advertisements rating")
    @ApiResponseSuccessful
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @PutMapping("/rating")
    public void updateAllRatings() {
        advertisementService.updateAllRatings();
    }
}
