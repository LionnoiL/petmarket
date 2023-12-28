package org.petmarket.review.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.review.service.ReviewService;
import org.petmarket.utils.annotations.parametrs.ParameterId;
import org.petmarket.utils.annotations.responses.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Review", description = "the reviews API")
@Slf4j
@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping(value = "/v1/admin")
public class ReviewAdminController {

    private final ReviewService reviewService;

    @Operation(summary = "Delete Review by ID")
    @ApiResponseDeleted
    @ApiResponseBadRequest
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @ApiResponseNotFound
    @DeleteMapping("/reviews/{id}")
    public ResponseEntity<Void> deleteReview(
            @Parameter(description = "The ID of the review to delete", required = true,
                    schema = @Schema(type = "integer", format = "int64")
            )
            @PathVariable Long id) {
        log.info("Received request to delete the review with id - {}.", id);
        reviewService.deleteReview(id);
        log.info("the review with id - {} was deleted.", id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Delete all Review by advertisement ID")
    @ApiResponseDeleted
    @ApiResponseBadRequest
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @ApiResponseNotFound
    @DeleteMapping("/advertisements/{id}/reviews")
    public ResponseEntity<Void> deleteAllReviewsByAdvertisement(
            @Parameter(description = "The ID of the advertisement whose reviews you want to remove", required = true,
                    schema = @Schema(type = "integer", format = "int64")
            )
            @PathVariable Long id) {
        log.info("Received request to delete the review with advertisement id - {}.", id);
        reviewService.deleteAllReviewsByAdvertisement(id);
        log.info("the review with advertisement id - {} was deleted.", id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Delete all Review by user ID")
    @ApiResponseDeleted
    @ApiResponseBadRequest
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @ApiResponseNotFound
    @DeleteMapping("/users/{id}/reviews")
    public ResponseEntity<Void> deleteAllReviewsByUser(
            @ParameterId @PathVariable @Positive Long id) {
        log.info("Received request to delete the review with user id - {}.", id);
        reviewService.deleteAllReviewsByUser(id);
        log.info("the review with user id - {} was deleted.", id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
