package org.petmarket.blog.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.petmarket.blog.dto.comment.BlogPostCommentResponse;
import org.petmarket.blog.entity.CommentStatus;
import org.petmarket.blog.service.CommentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Stack;

import static org.petmarket.utils.MessageUtils.*;

@RestController
@RequestMapping("/v1/admin/blog/comments")
@AllArgsConstructor
@Tag(name = "Blog", description = "Blog endpoints API")
public class CommentAdminController {
    private final CommentService commentService;

    @Operation(
            summary = "Get all comments",
            description = "Get all comments with pagination for admin",
            responses = {
                    @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION),
                    @ApiResponse(responseCode = "500", description = SERVER_ERROR)
            }
    )
    @GetMapping
    public List<BlogPostCommentResponse> getAllComments() {
        return commentService.findAllCommentAdmin();
    }

    @Operation(
            summary = "Change comment status",
            description = "Change the status of a comment by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION),
                    @ApiResponse(responseCode = "400", description = BAD_REQUEST),
                    @ApiResponse(responseCode = "404", description = NOT_FOUND),
                    @ApiResponse(responseCode = "500", description = SERVER_ERROR)
            },
            parameters = {
                    @Parameter(
                            name = "commentsIds",
                            description = "List of blog comments IDs",
                            example = "1",
                            required = true
                    )
            }
    )
    @PutMapping("/{commentsIds}/updateStatus")
    public List<BlogPostCommentResponse> changeCommentStatus(
            @PathVariable Stack<Long> commentsIds,
            @RequestParam CommentStatus status) {
        return commentService.updateStatus(commentsIds, status);
    }

    @Operation(
            summary = "Delete a comment",
            description = "Delete a comment by ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = SUCCESSFULLY_DELETED),
                    @ApiResponse(responseCode = "404", description = NOT_FOUND),
                    @ApiResponse(responseCode = "500", description = SERVER_ERROR)
            },
            parameters = {
                    @Parameter(
                            name = "commentId",
                            description = "Comment ID",
                            example = "1",
                            required = true
                    )
            }
    )
    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable(name = "commentId") Long commentId) {
        commentService.delete(commentId);
    }
}
