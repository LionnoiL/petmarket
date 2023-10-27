package org.petmarket.breeds.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.petmarket.blog.entity.CommentStatus;
import org.petmarket.breeds.dto.BreedCommentResponseDto;
import org.petmarket.breeds.service.BreedCommentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Stack;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/admin/breeds/comments")
@Tag(name = "Breeds", description = "API endpoints for breed comment administration")
public class BreedCommentAdminController {
    private final BreedCommentService commentService;

    @Operation(summary = "Get All Comments", description = "Get a list of all breed comments.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved comments")
    @GetMapping
    public List<BreedCommentResponseDto> getAllComments() {
        return commentService.findAllCommentAdmin();
    }

    @Operation(summary = "Change Comment Status", description = "Change the status of multiple comments.")
    @ApiResponse(responseCode = "200", description = "Status updated successfully")
    @ApiResponse(responseCode = "400", description = "Bad request")
    @PutMapping("/{commentsIds}/status/{status}")
    public List<BreedCommentResponseDto> changeCommentStatus(
            @PathVariable @Schema(description = "List of comment IDs") Stack<Long> commentsIds,
            @PathVariable @Schema(description = "New status for comments") CommentStatus status) {
        return commentService.updateStatus(commentsIds, status);
    }

    @Operation(summary = "Delete Comment", description = "Delete a breed comment.")
    @ApiResponse(responseCode = "204", description = "Comment deleted successfully")
    @ApiResponse(responseCode = "404", description = "Comment not found")
    @DeleteMapping("/{commentId}/delete")
    public void deleteComment(@PathVariable(name = "commentId")
                              @Schema(description = "ID of the comment") Long commentId) {
        commentService.delete(commentId);
    }
}
