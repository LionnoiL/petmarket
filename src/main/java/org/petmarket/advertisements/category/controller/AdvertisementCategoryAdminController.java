package org.petmarket.advertisements.category.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.advertisements.advertisement.dto.AdvertisementShortResponseDto;
import org.petmarket.advertisements.advertisement.entity.Advertisement;
import org.petmarket.advertisements.advertisement.entity.AdvertisementType;
import org.petmarket.advertisements.advertisement.mapper.AdvertisementResponseTranslateMapper;
import org.petmarket.advertisements.advertisement.service.AdvertisementService;
import org.petmarket.advertisements.attributes.entity.Attribute;
import org.petmarket.advertisements.attributes.service.AttributeService;
import org.petmarket.advertisements.category.dto.AdvertisementCategoryCreateRequestDto;
import org.petmarket.advertisements.category.dto.AdvertisementCategoryResponseDto;
import org.petmarket.advertisements.category.dto.AdvertisementCategoryUpdateRequestDto;
import org.petmarket.advertisements.category.entity.AdvertisementCategory;
import org.petmarket.advertisements.category.service.AdvertisementCategoryService;
import org.petmarket.language.entity.Language;
import org.petmarket.language.service.LanguageService;
import org.petmarket.location.entity.City;
import org.petmarket.location.service.CityService;
import org.petmarket.utils.annotations.parametrs.*;
import org.petmarket.utils.annotations.responses.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.petmarket.utils.MessageUtils.REQUEST_BODY_IS_MANDATORY;

@Tag(name = "Advertisement Categories", description = "the site advertisement categories API")
@Slf4j
@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping(value = "/v1/admin/categories")
public class AdvertisementCategoryAdminController {

    private final AdvertisementCategoryService categoryService;
    private final AdvertisementService advertisementService;
    private final LanguageService languageService;
    private final AttributeService attributeService;
    private final CityService cityService;
    private final AdvertisementResponseTranslateMapper advertisementMapper;

    @Operation(summary = "Create a new Advertisement Category")
    @ApiResponseSuccessful
    @ApiResponseBadRequest
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @ApiResponseNotFound
    @PostMapping
    public AdvertisementCategoryResponseDto addCategory(
            @RequestBody @Valid @NotNull(message = REQUEST_BODY_IS_MANDATORY)
            final AdvertisementCategoryCreateRequestDto request, BindingResult bindingResult) {
        log.info("Received request to create Advertisement Category - {}.", request);
        return categoryService.addCategory(request, bindingResult);
    }

    @Operation(summary = "Update Advertisement Category by ID")
    @ApiResponseCreated
    @ApiResponseBadRequest
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @ApiResponseNotFound
    @PutMapping("/{id}/{langCode}")
    public ResponseEntity<AdvertisementCategoryResponseDto> updateCategory(
            @ParameterId @PathVariable @Positive Long id,
            @ParameterLanguage @PathVariable String langCode,
            @RequestBody @Valid @NotNull(message = REQUEST_BODY_IS_MANDATORY)
            final AdvertisementCategoryUpdateRequestDto request, BindingResult bindingResult) {
        log.info("Received request to update advertisement category - {} with id {}.", request, id);
        AdvertisementCategoryResponseDto responseDto = categoryService.updateCategory(id, langCode, request, bindingResult);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @Operation(summary = "Delete Advertisement Category by ID")
    @ApiResponseDeleted
    @ApiResponseBadRequest
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @ApiResponseNotFound
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(
            @ParameterId @PathVariable @Positive Long id) {
        log.info("Received request to delete the Advertisement Category with id - {}.", id);
        categoryService.deleteCategory(id);
        log.info("the Advertisement Category with id - {} was deleted.", id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Update Advertisement Category parent")
    @ApiResponseSuccessful
    @ApiResponseBadRequest
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @ApiResponseNotFound
    @PutMapping("/{ids}/setParent/{parentId}")
    public List<AdvertisementCategoryResponseDto> setParentCategory(
            @Parameter(description = "List of categories identifiers",
                    schema = @Schema(type = "array[integer]")
            ) @PathVariable List<Long> ids,
            @ParameterId @PathVariable @Positive Long parentId) {
        log.info("Received request to set parent with id {} advertisement category with ids {}.",
                parentId, ids);
        return categoryService.setParentCategory(ids, parentId);
    }

    @Operation(summary = "Get Advertisements by category")
    @ApiResponseSuccessful
    @ApiResponseBadRequest
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @ApiResponseNotFound
    @GetMapping(path = "/{id}/{langCode}/advertisements")
    public Page<AdvertisementShortResponseDto> getAllAdvertisementsByCategory(
            @ParameterLanguage @PathVariable String langCode,
            @ParameterId @PathVariable Long id,
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
                attributes, cities, type, null, pageable);

        return advertisements.map(adv -> advertisementMapper.mapEntityToShortDto(adv, language));
    }
}
