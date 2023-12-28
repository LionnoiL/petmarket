package org.petmarket.blog.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.petmarket.blog.dto.comment.BlogPostCommentResponse;
import org.petmarket.blog.entity.CommentStatus;
import org.petmarket.blog.service.CommentService;
import org.petmarket.utils.annotations.parametrs.ParameterId;
import org.petmarket.utils.annotations.responses.*;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Stack;

@RestController
@RequestMapping("/v1/admin/blog/comments")
@AllArgsConstructor
@Validated
@Tag(name = "Blog", description = "Blog endpoints API")
public class CommentAdminController {
    private final CommentService commentService;

    @Operation(
            summary = "Get all comments",
            description = "Get all comments with pagination for admin"
    )
    @ApiResponseSuccessful
    @ApiResponseBadRequest
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @GetMapping
    public List<BlogPostCommentResponse> getAllComments() {
        return commentService.findAllCommentAdmin();
    }

    @Operation(
            summary = "Change comment status",
            description = "Change the status of a comment by ID",
            parameters = {
                    @Parameter(
                            name = "commentsIds",
                            description = "List of blog comments IDs",
                            example = "1",
                            required = true
                    )
            }
    )
    @ApiResponseSuccessful
    @ApiResponseBadRequest
    @ApiResponseNotFound
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @PutMapping("/{commentsIds}/updateStatus")
    public List<BlogPostCommentResponse> changeCommentStatus(
            @PathVariable Stack<Long> commentsIds,
            @RequestParam CommentStatus status) {
        return commentService.updateStatus(commentsIds, status);
    }

    @Operation(
            summary = "Delete a comment",
            description = "Delete a comment by ID"
    )
    @ApiResponseDeleted
    @ApiResponseBadRequest
    @ApiResponseNotFound
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{commentId}")
    public void deleteComment(@ParameterId @PathVariable(name = "commentId") @Positive Long commentId) {
        commentService.delete(commentId);
    }
}
