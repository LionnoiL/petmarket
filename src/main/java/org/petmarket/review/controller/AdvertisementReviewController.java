package org.petmarket.review.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.advertisements.advertisement.service.AdvertisementService;
import org.petmarket.review.dto.AdvertisementReviewRequestDto;
import org.petmarket.review.dto.AdvertisementReviewResponseDto;
import org.petmarket.utils.annotations.parametrs.ParameterId;
import org.petmarket.utils.annotations.responses.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

import static org.petmarket.utils.MessageUtils.REQUEST_BODY_IS_MANDATORY;
import static org.petmarket.utils.MessageUtils.SUCCESSFULLY_OPERATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Review", description = "the reviews API")
@Slf4j
@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping(value = "/v1/advertisements")
public class AdvertisementReviewController {

    private final AdvertisementService advertisementService;

    @Operation(summary = "Get review by advertisement ID")
    @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION, content = {
            @Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(
                            implementation = AdvertisementReviewResponseDto.class))
            )
    })
    @ApiResponseBadRequest
    @ApiResponseNotFound
    @GetMapping("/{id}/reviews")
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
    @ApiResponseCreated
    @ApiResponseBadRequest
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @ApiResponseNotFound
    @PostMapping("/{id}/reviews")
    @PreAuthorize("isAuthenticated()")
    public AdvertisementReviewResponseDto addReview(
            @ParameterId @PathVariable @Positive Long id,
            @RequestBody @Valid
            @NotNull(message = REQUEST_BODY_IS_MANDATORY) final AdvertisementReviewRequestDto request,
            BindingResult bindingResult, Authentication authentication) {
        log.info("Received request to add review - {} to advertisement with id {}.", request, id);
        return advertisementService.addReview(id, request, bindingResult, authentication);
    }
}
