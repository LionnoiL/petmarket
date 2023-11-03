package org.petmarket.review.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.advertisements.advertisement.service.AdvertisementService;
import org.petmarket.errorhandling.ErrorResponse;
import org.petmarket.review.dto.AdvertisementReviewRequestDto;
import org.petmarket.review.dto.AdvertisementReviewResponseDto;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@Tag(name = "Review", description = "the reviews API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1/advertisements")
public class AdvertisementReviewController {

    private final AdvertisementService advertisementService;

    @Operation(summary = "Get review by advertisement ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = {
                    @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(
                                    implementation = AdvertisementReviewResponseDto.class))
                    )
            })
    })
    @GetMapping("/{id}/reviews")
    @ResponseBody
    public Collection<AdvertisementReviewResponseDto> getAdvertisementReviews(
            @Parameter(description = "The ID of the Advertisement to retrieve", required = true,
                    schema = @Schema(type = "integer", format = "int64")
            )
            @PathVariable Long id) {
        log.info("Received request to get reviews on the advertisement with id - {}.", id);
        Collection<AdvertisementReviewResponseDto> dtos = advertisementService.getReviewsByAdvertisementId(id);
        log.info("the reviews on the advertisement with id - {} - {}.", id, dtos);
        return dtos;
    }

    @Operation(summary = "Add review to advertisement")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successful operation", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = AdvertisementReviewResponseDto.class))
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
            @ApiResponse(responseCode = "404", description = "Advertisement not found", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = ErrorResponse.class))
            })
    })
    @PostMapping("/{id}/reviews")
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    public AdvertisementReviewResponseDto addReview(
            @Parameter(description = "The ID of the advertisement", required = true,
                    schema = @Schema(type = "integer", format = "int64")
            )
            @PathVariable Long id,
            @RequestBody @Valid @NotNull(message = "Request body is mandatory") final AdvertisementReviewRequestDto request,
            BindingResult bindingResult, Authentication authentication) {
        log.info("Received request to add review - {} to advertisement with id {}.", request, id);
        return advertisementService.addReview(id, request, bindingResult, authentication);
    }
}
