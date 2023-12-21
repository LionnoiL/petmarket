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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.petmarket.utils.MessageUtils.*;

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
            description = "Add a comment to a breed.",
            responses = {
                    @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION),
                    @ApiResponse(responseCode = "400", description = BAD_REQUEST)
            },
            parameters = {
                    @Parameter(name = "breedId", description = "Breed Id", example = "1")
            }
    )
    @PostMapping("/{breedId}")
    public BreedCommentResponseDto addComment(
            @PathVariable("breedId") Long breedId,
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
            description = "Get a list of all comments for a specific breed",
            responses = {
                    @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION),
                    @ApiResponse(responseCode = "500", description = SERVER_ERROR)
            },
            parameters = {
                    @Parameter(
                            name = "breedId",
                            description = "ID of the breed",
                            example = "1"),
                    @Parameter(
                            name = "page",
                            description = "Page number",
                            example = "1"),
                    @Parameter(
                            name = "size",
                            description = "Number of items per page",
                            example = "12"),
                    @Parameter(
                            name = "sortDirection",
                            description = "Sort direction",
                            example = "ASC")
            })
    @GetMapping("/{breedId}")
    public List<BreedCommentResponseDto> getAll(@PathVariable Long breedId,
                                                @RequestParam(defaultValue = "1") @Positive int page,
                                                @RequestParam(defaultValue = "12") @Positive int size,
                                                @RequestParam(defaultValue = "ASC") String sortDirection,
                                                Authentication authentication) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), "created");
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        return commentService.findAll(breedId, pageable, authentication);
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "Delete a comment by ID for User",
            description = "Deletes a comment with the specified ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = SUCCESSFULLY_DELETED),
                    @ApiResponse(responseCode = "400", description = BAD_REQUEST),
                    @ApiResponse(responseCode = "403", description = FORBIDDEN),
                    @ApiResponse(responseCode = "404", description = NOT_FOUND),
                    @ApiResponse(responseCode = "500", description = SERVER_ERROR)
            },
            parameters = {
                    @Parameter(name = "commentId", description = "Comment Id", example = "1")
            }
    )
    @DeleteMapping("/{commentId}")
    public void deleteMyComment(@PathVariable Long commentId, Authentication authentication) {
        commentService.deleteMyBreedComment(commentId, authentication);
    }

}
