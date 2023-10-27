package org.petmarket.breeds.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.petmarket.breeds.dto.BreedCommentRequestDto;
import org.petmarket.breeds.dto.BreedCommentResponseDto;
import org.petmarket.breeds.service.BreedCommentService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/breeds/comments")
@Tag(name = "Breeds", description = "API endpoints for breed comments")
public class BreedCommentController {
    private final BreedCommentService commentService;

    @Operation(summary = "Add Comment", description = "Add a comment to a breed.")
    @ApiResponse(responseCode = "200", description = "Comment added successfully")
    @ApiResponse(responseCode = "400", description = "Bad request")
    @PostMapping("/{breedId}")
    public BreedCommentResponseDto addComment(@PathVariable
                                              @Schema(description = "ID of the breed")
                                              Long breedId,
                                              @RequestBody
                                              @Valid
                                              @Schema(description = "Comment data to be added")
                                              BreedCommentRequestDto requestDto,
                                              Authentication authentication) {
        return commentService.addComment(breedId, requestDto, authentication);
    }
}
