package org.petmarket.blog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.petmarket.blog.dto.comment.BlogPostCommentRequest;
import org.petmarket.blog.dto.comment.BlogPostCommentResponse;
import org.petmarket.blog.service.CommentService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/blog/comments")
@RequiredArgsConstructor
@Tag(name = "Blog Comments", description = "Endpoints for managing blog comments")
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/{postId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Create a new comment for a blog post",
            description = "Create a new comment for a specific blog post")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Comment created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Comment not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public BlogPostCommentResponse createComment(
            @PathVariable(name = "postId") @Parameter(description = "Post ID", required = true) Long postId,
            @RequestBody @Valid BlogPostCommentRequest request,
            Authentication authentication) {
        return commentService.addComment(postId, request, authentication);
    }
}
