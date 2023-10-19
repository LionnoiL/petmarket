package org.petmarket.advertisements.category.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.advertisements.advertisement.dto.AdvertisementResponseDto;
import org.petmarket.advertisements.category.dto.AdvertisementCategoryCreateRequestDto;
import org.petmarket.advertisements.category.dto.AdvertisementCategoryResponseDto;
import org.petmarket.advertisements.category.dto.AdvertisementCategoryUpdateRequestDto;
import org.petmarket.advertisements.category.service.AdvertisementCategoryService;
import org.petmarket.errorhandling.ErrorResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Advertisement Categories", description = "the site advertisement categories API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1/admin/categories")
public class AdvertisementCategoryAdminController {

    private final AdvertisementCategoryService categoryService;

    @Operation(summary = "Create a new Advertisement Category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = AdvertisementCategoryResponseDto.class))
            }),
            @ApiResponse(responseCode = "400", description =
                    "The Advertisement Category has already been added " +
                            "or some data is missing", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = ErrorResponse.class))
            }),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = ErrorResponse.class))
            }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = ErrorResponse.class))
            })
    })
    @PostMapping
    @ResponseBody
    public AdvertisementCategoryResponseDto addCategory(
            @RequestBody @Valid @NotNull(message = "Request body is mandatory") final AdvertisementCategoryCreateRequestDto request, BindingResult bindingResult) {
        log.info("Received request to create Advertisement Category - {}.", request);
        return categoryService.addCategory(request, bindingResult);
    }

    @Operation(summary = "Update Advertisement Category by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successful operation", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = AdvertisementCategoryResponseDto.class))
            }),
            @ApiResponse(responseCode = "400", description = "Some data is missing", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = ErrorResponse.class))
            }),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = ErrorResponse.class))
            }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = ErrorResponse.class))
            }),
            @ApiResponse(responseCode = "404", description = "Language or Advertisement Category not found", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = ErrorResponse.class))
            })
    })
    @PutMapping("/{id}/{langCode}")
    @ResponseBody
    public AdvertisementCategoryResponseDto updateCategory(
            @Parameter(description = "The ID of the advertisement category to update", required = true,
                    schema = @Schema(type = "integer", format = "int64")
            )
            @PathVariable Long id,
            @Parameter(description = "The Code Language of the advertisement category to retrieve", required = true,
                    schema = @Schema(type = "string")
            )
            @PathVariable String langCode,
            @RequestBody @Valid @NotNull(message = "Request body is mandatory") final AdvertisementCategoryUpdateRequestDto request, BindingResult bindingResult) {
        log.info("Received request to update advertisement category - {} with id {}.", request, id);
        return categoryService.updateCategory(id, langCode, request, bindingResult);
    }

    @Operation(summary = "Delete Advertisement Category by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successful operation"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = ErrorResponse.class))
            }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = ErrorResponse.class))
            }),
            @ApiResponse(responseCode = "404", description = "Advertisement Category not found", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = ErrorResponse.class))
            })
    })
    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteCategory(
            @Parameter(description = "The ID of the advertisement category to delete", required = true,
                    schema = @Schema(type = "integer", format = "int64")
            )
            @PathVariable Long id) {
        log.info("Received request to delete the Advertisement Category with id - {}.", id);
        categoryService.deleteCategory(id);
        log.info("the Advertisement Category with id - {} was deleted.", id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "Update Advertisement Category parent")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successful operation"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = ErrorResponse.class))
            }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = ErrorResponse.class))
            }),
            @ApiResponse(responseCode = "404", description = "Advertisement Category not found", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = ErrorResponse.class))
            })
    })
    @PutMapping("/{ids}/setParent/{parentId}")
    @ResponseBody
    public List<AdvertisementCategoryResponseDto> setParentCategory(
            @Parameter(description = "List of categories identifiers",
                    schema = @Schema(type = "array[integer]")
            ) @RequestParam(required = false) List<Long> ids,
            @Parameter(description = "The ID of the parent advertisement category", required = true,
                    schema = @Schema(type = "integer", format = "int64")
            )
            @PathVariable Long parentId) {
        log.info("Received request to set parent with id {} advertisement category with ids {}.",
                parentId, ids);
        return categoryService.setParentCategory(ids, parentId);
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
            @Parameter(description = "Advertisement type (SIMPLE, PRODUCT, VIP)",
                    schema = @Schema(type = "string")
            ) @RequestParam(required = false, defaultValue = "SIMPLE") String type
    ) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.Direction.valueOf(sortDirection), sortField);
        //TODO finish after add AdvertisementService
        return null;
    }
}
