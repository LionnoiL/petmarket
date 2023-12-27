package org.petmarket.blog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.petmarket.blog.dto.comment.BlogPostCommentRequest;
import org.petmarket.blog.dto.comment.BlogPostCommentResponse;
import org.petmarket.blog.service.CommentService;
import org.petmarket.utils.annotations.parametrs.ParameterId;
import org.petmarket.utils.annotations.responses.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/blog/comments")
@RequiredArgsConstructor
@Validated
@Tag(name = "Blog", description = "Blog endpoints API")
public class CommentController {
    private final CommentService commentService;

    @Operation(
            summary = "Create a new comment for a blog post",
            description = "Create a new comment for a specific blog post"
    )
    @ApiResponseSuccessful
    @ApiResponseBadRequest
    @ApiResponseNotFound
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{postId}")
    public BlogPostCommentResponse createComment(
            @ParameterId @PathVariable(name = "postId") @Positive Long postId,
            @RequestBody @Valid @Parameter(description = "Blog post comment request dto", required = true)
            BlogPostCommentRequest request,
            Authentication authentication) {
        return commentService.addComment(postId, request, authentication);
    }
}
