package org.petmarket.advertisements.category.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.advertisements.advertisement.dto.AdvertisementResponseDto;
import org.petmarket.advertisements.category.dto.AdvertisementCategoryResponseDto;
import org.petmarket.advertisements.category.service.AdvertisementCategoryService;
import org.petmarket.errorhandling.ErrorResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@Tag(name = "Advertisement Categories", description = "the site advertisement categories API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1/categories")
public class AdvertisementCategoryController {

    private final AdvertisementCategoryService categoryService;

    @Operation(summary = "Get Category by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = AdvertisementCategoryResponseDto.class))
            }),
            @ApiResponse(responseCode = "404", description = "Category not found", content = {
                    @Content(mediaType = "application/json", schema =
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
                    schema = @Schema(type = "string")
            )
            @PathVariable String langCode) {
        log.info("Received request to get the Category with id - {}.", id);
        AdvertisementCategoryResponseDto dto = categoryService.findById(id, langCode);
        log.info("the Category with id - {} was retrieved - {}.", id, dto);
        return dto;
    }

    @Operation(summary = "Get all Categories.", description = "Obtaining all site categories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = {
                    @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(
                                    implementation = AdvertisementCategoryResponseDto.class))
                    )
            })
    })
    @GetMapping("/{langCode}")
    @ResponseBody
    public ResponseEntity<Collection<AdvertisementCategoryResponseDto>> getAll(
            @Parameter(description = "The Code Language of the categories to retrieve", required = true,
                    schema = @Schema(type = "string")
            )
            @PathVariable String langCode) {
        log.info("Received request to get all Categories.");
        Collection<AdvertisementCategoryResponseDto> dtos = categoryService.getAll(langCode);
        log.info("All Categories were retrieved - {}.", dtos);
        return ResponseEntity.ok().body(dtos);
    }

    @Operation(summary = "Get favorite Categories.", description = "Obtaining favorite site categories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = {
                    @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(
                                    implementation = AdvertisementCategoryResponseDto.class))
                    )
            })
    })
    @GetMapping("/favorite/{langCode}")
    @ResponseBody
    public ResponseEntity<Collection<AdvertisementCategoryResponseDto>> getFavorite(
            @Parameter(description = "The Code Language of the categories to retrieve", required = true,
                    schema = @Schema(type = "string")
            )
            @PathVariable String langCode) {
        log.info("Received request to get favorite Categories.");
        Collection<AdvertisementCategoryResponseDto> dtos = categoryService.getFavorite(langCode);
        log.info("All Categories were retrieved - {}.", dtos);
        return ResponseEntity.ok().body(dtos);
    }

    @Operation(summary = "Get Advertisements by category")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @GetMapping(path = "/{id}/{langCode}/advertisements")
    public Page<AdvertisementResponseDto> getAllAdvertisementsByCategory(
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
            ) @RequestParam(required = false, defaultValue = "ASC") String sortField,
            @Parameter(description = "List of attributes identifiers",
                    schema = @Schema(type = "array[integer]")
            ) @RequestParam(required = false) List<Long> attributes,
            @Parameter(description = "List of locations identifiers",
                    schema = @Schema(type = "array[integer]")
            ) @RequestParam(required = false) List<Long> locations,
            @Parameter(description = "Advertisement type (SIMPLE, PRODUCT, VIP)",
                    schema = @Schema(type = "string")
            ) @RequestParam(required = false, defaultValue = "SIMPLE") String type
    ) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.Direction.valueOf(sortDirection), sortField);
        //TODO finish after add AdvertisementService
        return null;
    }
}
