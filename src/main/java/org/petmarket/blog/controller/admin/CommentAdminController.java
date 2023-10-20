package org.petmarket.blog.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.petmarket.blog.dto.comment.BlogPostCommentResponse;
import org.petmarket.blog.service.CommentService;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/admin/blog/comments")
@AllArgsConstructor
@Tag(name = "Admin Blog Comments Management", description = "Endpoints for managing blog comments by admin")
public class CommentAdminController {
    private final CommentService commentService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all comments", description = "Get all comments with pagination for admin")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of comments retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public List<BlogPostCommentResponse> getAllComments(@Parameter(description = "Pagination information")
                                                        Pageable pageable) {
        return commentService.findAllCommentAdmin(pageable);
    }

    @PutMapping("/{commentId}/{commentStatus}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Change comment status", description = "Change the status of a comment by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Comment status changed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Comment not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public BlogPostCommentResponse changeCommentStatus(@PathVariable(name = "commentId")
                                                       @Parameter(description = "Comment ID",
                                                               required = true) Long commentId,
                                                       @PathVariable(name = "commentStatus")
                                                       @Parameter(description = "New comment status",
                                                               required = true) String commentStatus) {
        return commentService.updateStatus(commentId, commentStatus);
    }

    @Operation(summary = "Delete a comment", description = "Delete a comment by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Comment deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Comment not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{commentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteComment(@PathVariable(name = "commentId")
                              @Parameter(description = "Comment ID",
                                      required = true) Long commentId) {
        commentService.delete(commentId);
    }
}
