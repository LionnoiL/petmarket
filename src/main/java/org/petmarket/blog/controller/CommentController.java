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
@Tag(name = "Blog", description = "Blog endpoints API")
public class CommentController {
    private final CommentService commentService;

    @Operation(
            summary = "Create a new comment for a blog post",
            description = "Create a new comment for a specific blog post",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Comment created successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "404", description = "Post not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            },
            parameters = {
                    @Parameter(
                            name = "postId",
                            description = "Post ID",
                            example = "1",
                            required = true
                    )
            }
    )
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{postId}")
    public BlogPostCommentResponse createComment(
            @PathVariable(name = "postId") Long postId,
            @RequestBody @Valid @Parameter(description = "Blog post comment request dto", required = true)
            BlogPostCommentRequest request,
            Authentication authentication) {
        return commentService.addComment(postId, request, authentication);
    }
}
