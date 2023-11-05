package org.petmarket.breeds.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.petmarket.breeds.dto.BreedCommentRequestDto;
import org.petmarket.breeds.dto.BreedCommentResponseDto;
import org.petmarket.breeds.service.BreedCommentService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/breeds/comments")
@Tag(name = "Breeds", description = "API endpoints for breed administration")
public class BreedCommentController {
    private final BreedCommentService commentService;

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Add Comment (any user)", description = "Add a comment to a breed.")
    @ApiResponse(responseCode = "200", description = "Comment added successfully")
    @ApiResponse(responseCode = "400", description = "Bad request")
    @PostMapping("/{breedId}")
    public BreedCommentResponseDto addComment(@PathVariable("breedId")
                                              @Parameter(name = "breedId", description = "Breed Id", example = "1")
                                              Long breedId,
                                              @RequestBody
                                              @Valid
                                              @Schema(description = "Comment data to be added")
                                              @Parameter(name = "Comment breed Dto",
                                                      required = true)
                                              BreedCommentRequestDto requestDto,
                                              Authentication authentication) {
        return commentService.addComment(breedId, requestDto, authentication);
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Delete a comment by ID for User",
            description = "Deletes a comment with the specified ID",
            responses = {
                    @ApiResponse(responseCode = "204",
                            description = "Comment successfully deleted"),
                    @ApiResponse(responseCode = "400",
                            description = "Bad request, e.g., invalid comment ID"),
                    @ApiResponse(responseCode = "404",
                            description = "Comment not found"),
                    @ApiResponse(responseCode = "403",
                            description = "Forbidden, e.g., user does not have permission to delete the comment"),
                    @ApiResponse(responseCode = "500",
                            description = "Internal server error")
            })
    @DeleteMapping("/{commentId}")
    public void deleteMyComment(@PathVariable
                                @Parameter(name = "commentId", description = "Comment Id", example = "1")
                                Long commentId, Authentication authentication) {
        commentService.deleteMyBreedComment(commentId, authentication);
    }
}
