package org.petmarket.advertisements.advertisement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
import org.petmarket.errorhandling.ErrorResponse;
import org.petmarket.language.entity.Language;
import org.petmarket.language.service.LanguageService;
import org.petmarket.location.entity.City;
import org.petmarket.location.service.CityService;
import org.petmarket.options.service.OptionsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.petmarket.utils.MessageUtils.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST, content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema =
                    @Schema(implementation = ErrorResponse.class))
            }),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED, content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema =
                    @Schema(implementation = ErrorResponse.class))
            }),
            @ApiResponse(responseCode = "403", description = FORBIDDEN, content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema =
                    @Schema(implementation = ErrorResponse.class))
            })
    })
    @GetMapping(path = "/{langCode}")
    public Page<AdvertisementShortResponseDto> getAds(
            @Parameter(description = "The Code Language of the advertisements to retrieve", required = true,
                    schema = @Schema(type = "string"), example = "ua"
            )
            @PathVariable String langCode,
            @Parameter(description = "Number of page (1..N)", required = true,
                    schema = @Schema(type = "integer", defaultValue = "1")
            ) @RequestParam(defaultValue = "1") @Positive int page,
            @Parameter(description = "The size of the page to be returned", required = true,
                    schema = @Schema(type = "integer", defaultValue = "30")
            ) @RequestParam(defaultValue = "30") @Positive int size,
            @Parameter(description = "Sort direction (ASC, DESC)",
                    schema = @Schema(type = "string")
            ) @RequestParam(required = false, defaultValue = "ASC") String sortDirection,
            @Parameter(description = "Sort field",
                    schema = @Schema(type = "string")
            ) @RequestParam(required = false, defaultValue = "created") String sortField,
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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION, content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema =
                    @Schema(implementation = AdvertisementDetailsResponseDto.class))
            }),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED, content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema =
                    @Schema(implementation = ErrorResponse.class))
            }),
            @ApiResponse(responseCode = "403", description = FORBIDDEN, content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema =
                    @Schema(implementation = ErrorResponse.class))
            })
    })
    @PutMapping("/status")
    @ResponseBody
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
}
