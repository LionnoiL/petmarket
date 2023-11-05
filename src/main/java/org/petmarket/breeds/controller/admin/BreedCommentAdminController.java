package org.petmarket.breeds.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.petmarket.blog.entity.CommentStatus;
import org.petmarket.breeds.dto.BreedCommentResponseDto;
import org.petmarket.breeds.service.BreedCommentService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Stack;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/admin/breeds/comments")
@Tag(name = "Breeds", description = "API endpoints for breed administration")
public class BreedCommentAdminController {
    private final BreedCommentService commentService;

    @Operation(summary = "Get All Comments for Admin", description = "Get a list of all breed comments.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved comments")
    @GetMapping
    public List<BreedCommentResponseDto> getAllComments(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "ASC") String sortDirection,
            @RequestParam(required = true) CommentStatus status) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), "created");
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        return commentService.findAllCommentAdmin(pageable, status);
    }

    @Operation(summary = "Change Comment Status for admin", description = "Change the status of multiple comments.")
    @ApiResponse(responseCode = "200", description = "Status updated successfully")
    @ApiResponse(responseCode = "400", description = "Bad request")
    @PutMapping("/{commentsIds}/status/{status}")
    public List<BreedCommentResponseDto> changeCommentStatus(
            @PathVariable
            @Parameter(name = "commentsIds",
                    description = "list of comments that need to be updated",
                    example = "1")
            Stack<Long> commentsIds,
            @PathVariable
            @Schema(description = "New status for comments")
            @Parameter(description = "New status", required = true)
            CommentStatus status) {
        return commentService.updateStatus(commentsIds, status);
    }

    @Operation(summary = "Delete Comment by Admin only", description = "Delete a breed comment.")
    @ApiResponse(responseCode = "204", description = "Comment deleted successfully")
    @ApiResponse(responseCode = "404", description = "Comment not found")
    @DeleteMapping("/{commentId}/delete")
    public void deleteComment(@PathVariable(name = "commentId")
                              @Parameter(name = "commentId",
                                      description = "Id of comment",
                                      example = "1")
                              Long commentId) {
        commentService.delete(commentId);
    }
}
