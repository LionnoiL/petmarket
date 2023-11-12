package org.petmarket.breeds.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.petmarket.breeds.dto.BreedCommentRequestDto;
import org.petmarket.breeds.dto.BreedCommentResponseDto;
import org.petmarket.breeds.service.BreedCommentService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @Operation(summary = "Get all breed comments",
            description = "Get a list of all comments for a specific breed",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Successful operation"),
                    @ApiResponse(responseCode = "500",
                            description = "Internal server error")
            },
            parameters = {
                    @Parameter(
                            name = "breedId",
                            description = "ID of the breed",
                            in = ParameterIn.PATH,
                            example = "1"),
                    @Parameter(
                            name = "page",
                            description = "Page number",
                            in = ParameterIn.QUERY,
                            example = "1"),
                    @Parameter(
                            name = "size",
                            description = "Number of items per page",
                            in = ParameterIn.QUERY,
                            example = "12"),
                    @Parameter(
                            name = "sortDirection",
                            description = "Sort direction",
                            in = ParameterIn.QUERY,
                            example = "ASC")
            })
    @GetMapping("/{breedId}")
    public List<BreedCommentResponseDto> getAll(@PathVariable Long breedId,
                                                @RequestParam(defaultValue = "1") int page,
                                                @RequestParam(defaultValue = "12") int size,
                                                @RequestParam(defaultValue = "ASC") String sortDirection,
                                                Authentication authentication) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), "created");
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        return commentService.findAll(breedId, pageable, authentication);
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
