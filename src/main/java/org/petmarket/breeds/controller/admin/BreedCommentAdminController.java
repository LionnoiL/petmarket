package org.petmarket.breeds.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.petmarket.blog.entity.CommentStatus;
import org.petmarket.breeds.dto.BreedCommentResponseDto;
import org.petmarket.breeds.service.BreedCommentService;
import org.petmarket.utils.annotations.parametrs.ParameterPageNumber;
import org.petmarket.utils.annotations.parametrs.ParameterPageSize;
import org.petmarket.utils.annotations.parametrs.ParameterPageSort;
import org.petmarket.utils.annotations.responses.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Stack;

import static org.petmarket.utils.MessageUtils.SUCCESSFULLY_OPERATION;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/admin/breeds/comments")
@Validated
@Tag(name = "Breeds", description = "API endpoints for breed administration")
public class BreedCommentAdminController {
    private final BreedCommentService commentService;

    @Operation(
            summary = "Get All Comments for Admin",
            description = "Get a list of all breed comments."
    )
    @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION, content = {
            @Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(
                            implementation = BreedCommentResponseDto.class))
            )
    })
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @ApiResponseBadRequest
    @GetMapping
    public List<BreedCommentResponseDto> getAllComments(
            @ParameterPageNumber @RequestParam(defaultValue = "1") @Positive int page,
            @ParameterPageSize @RequestParam(defaultValue = "12") @Positive int size,
            @ParameterPageSort @RequestParam(defaultValue = "ASC") String sortDirection,
            @RequestParam CommentStatus status) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), "created");
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        return commentService.findAllCommentAdmin(pageable, status);
    }

    @Operation(
            summary = "Change Comment Status for admin",
            description = "Change the status of multiple comments.",
            parameters = {
                    @Parameter(name = "commentsIds",
                            description = "List of comments that need to be updated", example = "1")
            }
    )
    @ApiResponseSuccessful
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @ApiResponseBadRequest
    @ApiResponseNotFound
    @PutMapping("/{commentsIds}/status/{status}")
    public List<BreedCommentResponseDto> changeCommentStatus(
            @PathVariable Stack<Long> commentsIds,
            @PathVariable CommentStatus status) {
        return commentService.updateStatus(commentsIds, status);
    }

    @Operation(
            summary = "Delete Comment by Admin only",
            description = "Delete a breed comment."
    )
    @ApiResponseDeleted
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @ApiResponseBadRequest
    @ApiResponseNotFound
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{commentId}/delete")
    public void deleteComment(@PathVariable(name = "commentId") Long commentId) {
        commentService.delete(commentId);
    }
}
