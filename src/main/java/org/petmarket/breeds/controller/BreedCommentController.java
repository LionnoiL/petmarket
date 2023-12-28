package org.petmarket.breeds.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.petmarket.breeds.dto.BreedCommentRequestDto;
import org.petmarket.breeds.dto.BreedCommentResponseDto;
import org.petmarket.breeds.service.BreedCommentService;
import org.petmarket.utils.annotations.parametrs.ParameterId;
import org.petmarket.utils.annotations.parametrs.ParameterPageNumber;
import org.petmarket.utils.annotations.parametrs.ParameterPageSize;
import org.petmarket.utils.annotations.parametrs.ParameterPageSort;
import org.petmarket.utils.annotations.responses.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.petmarket.utils.MessageUtils.SUCCESSFULLY_DELETED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/breeds/comments")
@Validated
@Tag(name = "Breeds", description = "API endpoints for breed administration")
public class BreedCommentController {
    private final BreedCommentService commentService;

    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "Add Comment (any user)",
            description = "Add a comment to a breed."
    )
    @ApiResponseSuccessful
    @ApiResponseBadRequest
    @ApiResponseUnauthorized
    @ApiResponseNotFound
    @PostMapping("/{breedId}")
    public BreedCommentResponseDto addComment(
            @ParameterId @PathVariable("breedId") @Positive Long breedId,
            @RequestBody
            @Valid
            @Schema(description = "Comment data to be added")
            @Parameter(name = "Comment breed Dto",
                    required = true)
            BreedCommentRequestDto requestDto,
            Authentication authentication) {
        return commentService.addComment(breedId, requestDto, authentication);
    }

    @Operation(summary = "Get all breed comments",
            description = "Get a list of all comments for a specific breed"
    )
    @ApiResponseSuccessful
    @ApiResponseBadRequest
    @ApiResponseNotFound
    @GetMapping("/{breedId}")
    public List<BreedCommentResponseDto> getAll(
            @ParameterId @PathVariable @Positive Long breedId,
            @ParameterPageNumber @RequestParam(defaultValue = "1") @Positive int page,
            @ParameterPageSize @RequestParam(defaultValue = "12") @Positive int size,
            @ParameterPageSort @RequestParam(defaultValue = "ASC") String sortDirection,
            Authentication authentication) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), "created");
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        return commentService.findAll(breedId, pageable, authentication);
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "Delete a comment by ID for User",
            description = "Deletes a comment with the specified ID"
    )
    @ApiResponse(responseCode = "204", description = SUCCESSFULLY_DELETED)
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @ApiResponseBadRequest
    @ApiResponseNotFound
    @DeleteMapping("/{commentId}")
    public void deleteMyComment(@ParameterId @PathVariable @Positive Long commentId,
                                Authentication authentication) {
        commentService.deleteMyBreedComment(commentId, authentication);
    }
}
