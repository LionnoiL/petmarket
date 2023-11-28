package org.petmarket.advertisements.category.controller;

import static org.petmarket.utils.MessageUtils.CATEGORY_NOT_FOUND;
import static org.petmarket.utils.MessageUtils.SUCCESSFULLY_OPERATION;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.advertisements.advertisement.dto.AdvertisementResponseDto;
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
import org.petmarket.errorhandling.ErrorResponse;
import org.petmarket.language.entity.Language;
import org.petmarket.language.service.LanguageService;
import org.petmarket.location.entity.City;
import org.petmarket.location.service.CityService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Advertisement Categories", description = "the site advertisement categories API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1/categories")
public class AdvertisementCategoryController {

    private final AdvertisementCategoryService categoryService;
    private final AdvertisementService advertisementService;
    private final LanguageService languageService;
    private final AttributeService attributeService;
    private final CityService cityService;
    private final AdvertisementResponseTranslateMapper advertisementMapper;

    @Operation(summary = "Get Category by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION, content = {
            @Content(mediaType = APPLICATION_JSON_VALUE, schema =
            @Schema(implementation = AdvertisementCategoryResponseDto.class))
        }),
        @ApiResponse(responseCode = "404", description = CATEGORY_NOT_FOUND, content = {
            @Content(mediaType = APPLICATION_JSON_VALUE, schema =
            @Schema(implementation = ErrorResponse.class))
        })
    })
    @GetMapping("/{id}/{langCode}")
    @ResponseBody
    public AdvertisementCategoryResponseDto getCategoryById(
        @Parameter(description = "The ID of the category to retrieve", required = true,
            schema = @Schema(type = "integer", format = "int64")
        )
        @PathVariable Long id,
        @Parameter(description = "The Code Language of the category to retrieve", required = true,
            schema = @Schema(type = "string"), example = "ua"
        )
        @PathVariable String langCode) {
        log.info("Received request to get the Category with id - {}.", id);
        AdvertisementCategoryResponseDto dto = categoryService.findById(id, langCode);
        log.info("the Category with id - {} was retrieved - {}.", id, dto);
        return dto;
    }

    @Operation(summary = "Get all Categories.", description = "Obtaining all site categories")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION, content = {
            @Content(
                mediaType = APPLICATION_JSON_VALUE,
                array = @ArraySchema(schema = @Schema(
                    implementation = AdvertisementCategoryResponseDto.class))
            )
        })
    })
    @GetMapping("/{langCode}")
    @ResponseBody
    public ResponseEntity<Collection<AdvertisementCategoryResponseDto>> getAll(
        @Parameter(description = "The Code Language of the categories to retrieve", required = true,
            schema = @Schema(type = "string"), example = "ua"
        )
        @PathVariable String langCode) {
        log.info("Received request to get all Categories.");
        Collection<AdvertisementCategoryResponseDto> dtos = categoryService.getAll(langCode);
        log.info("All Categories were retrieved - {}.", dtos);
        return ResponseEntity.ok().body(dtos);
    }

    @Operation(summary = "Get favorite Categories.", description = "Obtaining favorite site categories")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION, content = {
            @Content(
                mediaType = APPLICATION_JSON_VALUE,
                array = @ArraySchema(schema = @Schema(
                    implementation = AdvertisementCategoryResponseDto.class))
            )
        })
    })
    @GetMapping("/favorite/{langCode}/{size}")
    @ResponseBody
    public ResponseEntity<Collection<AdvertisementCategoryResponseDto>> getFavorite(
        @Parameter(description = "The Code Language of the categories to retrieve", required = true,
            schema = @Schema(type = "string"), example = "ua"
        )
        @PathVariable String langCode,
        @Parameter(description = "The size of the categories to be returned", required = true,
            schema = @Schema(type = "integer", defaultValue = "10")
        )
        @PathVariable Integer size) {
        log.info("Received request to get favorite Categories.");
        Collection<AdvertisementCategoryResponseDto> dtos = categoryService.getFavorite(langCode,
            size);
        log.info("All Categories were retrieved - {}.", dtos);
        return ResponseEntity.ok().body(dtos);
    }

    @Operation(summary = "Get favorite tags for Categories.", description = "Obtaining favorite tags for categories")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION, content = {
            @Content(
                mediaType = APPLICATION_JSON_VALUE,
                array = @ArraySchema(schema = @Schema(
                    implementation = AdvertisementCategoryTagResponseDto.class))
            )
        })
    })
    @GetMapping("/favorite-tags/{langCode}/{size}")
    @ResponseBody
    public ResponseEntity<Collection<AdvertisementCategoryTagResponseDto>> getFavoriteTags(
        @Parameter(description = "The Code Language of the categories to retrieve", required = true,
            schema = @Schema(type = "string"), example = "ua"
        )
        @PathVariable String langCode,
        @Parameter(description = "The size of the categories to be returned", required = true,
            schema = @Schema(type = "integer", defaultValue = "10")
        )
        @PathVariable Integer size) {
        log.info("Received request to get favorite Tags Categories.");
        Collection<AdvertisementCategoryTagResponseDto> dtos = categoryService.getFavoriteTags(
            langCode, size);
        log.info("All Categories were retrieved - {}.", dtos);
        return ResponseEntity.ok().body(dtos);
    }

    @Operation(summary = "Get Advertisements by category")
    @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION)
    @GetMapping(path = "/{id}/{langCode}/advertisements")
    public Page<AdvertisementResponseDto> getAllAdvertisementsByCategory(
        @Parameter(description = "The Code Language of the advertisements to retrieve", required = true,
            schema = @Schema(type = "string"), example = "ua"
        )
        @PathVariable String langCode,
        @Parameter(description = "The ID of the category to retrieve", required = true,
            schema = @Schema(type = "integer", format = "int64")
        )
        @PathVariable Long id,
        @Parameter(description = "Number of page (1..N)", required = true,
            schema = @Schema(type = "integer", defaultValue = "1")
        ) @RequestParam(defaultValue = "1") int page,
        @Parameter(description = "The size of the page to be returned", required = true,
            schema = @Schema(type = "integer", defaultValue = "30")
        ) @RequestParam(defaultValue = "30") int size,
        @Parameter(description = "Sort direction (ASC, DESC)",
            schema = @Schema(type = "string")
        ) @RequestParam(required = false, defaultValue = "ASC") String sortDirection,
        @Parameter(description = "Sort field",
            schema = @Schema(type = "string")
        ) @RequestParam(required = false, defaultValue = "created") String sortField,
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

        return advertisements.map(adv -> advertisementMapper.mapEntityToDto(adv, language));
    }
}
