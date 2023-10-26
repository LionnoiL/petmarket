package org.petmarket.breeds.controller.admin;

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
public class BreedCommentAdminController {
    private final BreedCommentService commentService;

    @GetMapping
    public List<BreedCommentResponseDto> getAllComments() {
        return commentService.findAllCommentAdmin();
    }

    @PutMapping("/{commentsIds}/updateStatus")
    public List<BreedCommentResponseDto> changeCommentStatus(
            @PathVariable Stack<Long> commentsIds,
            @RequestParam CommentStatus status) {
        return commentService.updateStatus(commentsIds, status);
    }

    @DeleteMapping("/{commentId}/delete")
    public void deleteComment(@PathVariable(name = "commentId") Long commentId) {
        commentService.delete(commentId);
    }
}
