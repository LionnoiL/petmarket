package org.petmarket.advertisements.category.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.advertisements.advertisement.dto.AdvertisementShortResponseDto;
import org.petmarket.advertisements.advertisement.entity.Advertisement;
import org.petmarket.advertisements.advertisement.entity.AdvertisementStatus;
import org.petmarket.advertisements.advertisement.entity.AdvertisementType;
import org.petmarket.advertisements.advertisement.mapper.AdvertisementResponseTranslateMapper;
import org.petmarket.advertisements.advertisement.service.AdvertisementService;
import org.petmarket.advertisements.attributes.entity.Attribute;
import org.petmarket.advertisements.attributes.service.AttributeService;
import org.petmarket.advertisements.category.dto.AdvertisementCategoryResponseDto;
import org.petmarket.advertisements.category.dto.AdvertisementCategoryTagResponseDto;
import org.petmarket.advertisements.category.entity.AdvertisementCategory;
import org.petmarket.advertisements.category.service.AdvertisementCategoryService;
import org.petmarket.language.entity.Language;
import org.petmarket.language.service.LanguageService;
import org.petmarket.location.entity.City;
import org.petmarket.location.service.CityService;
import org.petmarket.utils.annotations.parametrs.*;
import org.petmarket.utils.annotations.responses.ApiResponseBadRequest;
import org.petmarket.utils.annotations.responses.ApiResponseNotFound;
import org.petmarket.utils.annotations.responses.ApiResponseSuccessful;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

import static org.petmarket.utils.MessageUtils.SUCCESSFULLY_OPERATION;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Tag(name = "Advertisement Categories", description = "the site advertisement categories API")
@Slf4j
@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping(value = "/v1/categories")
public class AdvertisementCategoryController {

    private final AdvertisementCategoryService categoryService;
    private final AdvertisementService advertisementService;
    private final LanguageService languageService;
    private final AttributeService attributeService;
    private final CityService cityService;
    private final AdvertisementResponseTranslateMapper advertisementMapper;

    @Operation(summary = "Get Category by ID")
    @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION, content = {
            @Content(mediaType = APPLICATION_JSON_VALUE, schema =
            @Schema(implementation = AdvertisementCategoryResponseDto.class))
    })
    @ApiResponseBadRequest
    @ApiResponseNotFound
    @GetMapping("/{id}/{langCode}")
    public AdvertisementCategoryResponseDto getCategoryById(
            @ParameterId @PathVariable @Positive Long id,
            @ParameterLanguage @PathVariable String langCode) {
        log.info("Received request to get the Category with id - {}.", id);
        AdvertisementCategoryResponseDto dto = categoryService.findById(id, langCode);
        log.info("the Category with id - {} was retrieved - {}.", id, dto);
        return dto;
    }

    @Operation(summary = "Get all Categories.", description = "Obtaining all site categories")
    @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION, content = {
            @Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(
                            implementation = AdvertisementCategoryResponseDto.class))
            )
    })
    @ApiResponseNotFound
    @GetMapping("/{langCode}")
    public ResponseEntity<Collection<AdvertisementCategoryResponseDto>> getAll(
            @ParameterLanguage @PathVariable String langCode) {
        log.info("Received request to get all Categories.");
        Collection<AdvertisementCategoryResponseDto> dtos = categoryService.getAll(langCode);
        log.info("All Categories were retrieved - {}.", dtos);
        return ResponseEntity.ok().body(dtos);
    }

    @Operation(summary = "Get favorite Categories.", description = "Obtaining favorite site categories")
    @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION, content = {
            @Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(
                            implementation = AdvertisementCategoryResponseDto.class))
            )
    })
    @ApiResponseBadRequest
    @ApiResponseNotFound
    @GetMapping("/favorite/{langCode}/{size}")
    public ResponseEntity<Collection<AdvertisementCategoryResponseDto>> getFavorite(
            @ParameterLanguage @PathVariable String langCode,
            @ParameterPageSize @PathVariable @Positive Integer size) {
        log.info("Received request to get favorite Categories.");
        Collection<AdvertisementCategoryResponseDto> dtos = categoryService.getFavorite(langCode,
                size);
        log.info("All Categories were retrieved - {}.", dtos);
        return ResponseEntity.ok().body(dtos);
    }

    @Operation(summary = "Get favorite tags for Categories.", description = "Obtaining favorite tags for categories")
    @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION, content = {
            @Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(
                            implementation = AdvertisementCategoryTagResponseDto.class))
            )
    })
    @ApiResponseBadRequest
    @ApiResponseNotFound
    @GetMapping("/favorite-tags/{langCode}/{size}")
    public ResponseEntity<Collection<AdvertisementCategoryTagResponseDto>> getFavoriteTags(
            @ParameterLanguage @PathVariable String langCode,
            @ParameterPageSize @PathVariable @Positive Integer size) {
        log.info("Received request to get favorite Tags Categories.");
        Collection<AdvertisementCategoryTagResponseDto> dtos = categoryService.getFavoriteTags(
                langCode, size);
        log.info("All Categories were retrieved - {}.", dtos);
        return ResponseEntity.ok().body(dtos);
    }

    @Operation(summary = "Get Advertisements by category")
    @ApiResponseSuccessful
    @ApiResponseBadRequest
    @ApiResponseNotFound
    @GetMapping(path = "/{id}/{langCode}/advertisements")
    public Page<AdvertisementShortResponseDto> getAllAdvertisementsByCategory(
            @ParameterLanguage @PathVariable String langCode,
            @ParameterId @PathVariable @Positive Long id,
            @ParameterPageNumber @RequestParam(defaultValue = "1") @Positive int page,
            @ParameterPageSize @RequestParam(defaultValue = "30") @Positive int size,
            @ParameterPageSort @RequestParam(required = false, defaultValue = "ASC") String sortDirection,
            @ParameterPageSortField @RequestParam(required = false, defaultValue = "created") String sortField,
            @Parameter(description = "List of attributes identifiers",
                    schema = @Schema(type = "array[integer]")
            ) @RequestParam(required = false) List<Long> attributesIds,
            @Parameter(description = "List of cities identifiers",
                    schema = @Schema(type = "array[integer]")
            ) @RequestParam(required = false) List<Long> citiesIds,
            @Parameter(description = "Advertisement type")
            @RequestParam(required = false) AdvertisementType type
    ) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.Direction.valueOf(sortDirection),
                sortField);
        Language language = languageService.getByLangCode(langCode);
        List<Attribute> attributes = attributeService.getByIds(attributesIds);
        List<City> cities = cityService.getByIds(citiesIds);
        AdvertisementCategory category = categoryService.findCategory(id);
        Page<Advertisement> advertisements = advertisementService.getByCategoryTypeCitiesAttributes(
                category,
                attributes, cities, type, AdvertisementStatus.ACTIVE, pageable);

        return advertisements.map(adv -> advertisementMapper.mapEntityToShortDto(adv, language));
    }
}
